package hyperledger_fabric.platform.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import hyperledger_fabric.platform.mapper.AuthMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenUtil {
	private final String LOCAL = "local";
	private final String HMAC_HASH = "HmacSHA256";
	
	private final String ISS = "ADMIN";
	private final String ACCESS_TOKEN_SUB = "accTkn";
	private final String REFRESH_TOKEN_SUB = "rfsTkn";
	
	private final String ID = "id";
	private final String ACCESS_TOKEN = "accTkn";
	private final String SCOPE = "scope";
	
	private Key TOKEN_KEY;
	
	@Autowired
	private AuthMapper authMapper;
	
	@PostConstruct
	private void setTokenKey() {
		if(LOCAL.equals(System.getProperty("dtso")))
			TOKEN_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		else 
			TOKEN_KEY = getTokenKey();
	}
	
	/**
	 * Token 인증
	 * @param paramMap
	 * @param session
	 * @return
	 */
	public Map<String, Object> auth(HttpSession session) {
		//session 체크
		if(StringUtils.isEmpty(session.getAttribute(ID)) || StringUtils.isEmpty(session.getAttribute(ACCESS_TOKEN)) || StringUtils.isEmpty(session.getAttribute(SCOPE))) { 
			return ResultUtil.transactionResult(MessageCode.E0000);
		}
		
		//ACCESS TOKEN 체크
		try {
			String accTkn = session.getAttribute(ACCESS_TOKEN).toString();
			Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(accTkn);
		} 
		//ACCESS TOKEN 만료
		catch(ExpiredJwtException e) {
			try {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("userId", session.getAttribute(ID));
				Map<String, Object> tknMap = authMapper.getUser(paramMap);
				if(tknMap == null) {
					return ResultUtil.transactionResult(MessageCode.E0000);
				}
				String refreshTkn = tknMap.get("token").toString();
				Jws<Claims> refreshJwsClaims = Jwts.parser().setSigningKey(TOKEN_KEY).parseClaimsJws(refreshTkn);
				String scope = refreshJwsClaims.getBody().get(SCOPE).toString();
				createAccessToken(session, refreshJwsClaims.getBody().getAudience().toString(), scope);
			} catch(ExpiredJwtException e1) {
				return ResultUtil.transactionResult(MessageCode.E0001);
			} catch(Exception e1) {
				return ResultUtil.transactionResult(MessageCode.E0000);
			}
		}
		//ACCESS TOKEN 인증 실패
		catch (Exception e) {
			return ResultUtil.transactionResult(MessageCode.E0000);
		}
		return ResultUtil.transactionResult(MessageCode.S);
	}

	/**
	 * Token 삭제
	 * @param session
	 */
	public void deleteToken(HttpSession session) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", session.getAttribute(ID));
		//Refresh Token 정보 삭제
		authMapper.deleteToken(paramMap);
		
		//Access Token 정보 삭제
		session.removeAttribute(ID);
		session.removeAttribute(SCOPE);
		session.removeAttribute(ACCESS_TOKEN);
	}
	
	/**
	 * JWT Access 토큰 생성
	 * @param tokenUserId
	 * @return
	 */
	public void createAccessToken(HttpSession session, String id, String scope) {
		Date EXP = new Date(System.currentTimeMillis() + 1000);	// 토큰 만료 시간 1분
		
		System.out.println(byteArrayToHexString(TOKEN_KEY.getEncoded()));
		
		String accTkn = Jwts.builder()
				.setIssuer(ISS)							//토큰 발급자
				.setSubject(ACCESS_TOKEN_SUB)			//Token 제목
				.setAudience(id)						//토큰 대상자
				.setId(UUID.randomUUID().toString())	//토큰 ID
				.setExpiration(EXP)						//토큰 만료시간
				.setIssuedAt(new Date())				//토큰이 발급된 시간
				.claim(SCOPE, scope)					//claim 설정
				.signWith(TOKEN_KEY)					//Key를 이용하여 Hashing
				.compact();
		
		session.setAttribute(ID, id);
		session.setAttribute(SCOPE, scope);
		session.setAttribute(ACCESS_TOKEN, accTkn);
		
		System.out.println("ACCESS ID : " + id);
		System.out.println("ACCESS SCOPE : " + scope);
		System.out.println("ACCESS TOKEN : " + accTkn);
	}
	
	/**
	 * JWT Refresh 토큰 생성
	 * @param tokenUserId
	 * @return
	 */
	public void createRefreshToken(String id, String scope) {
		Map<String, Object> tknMap = new HashMap<String, Object>();
		Date EXP = new Date(System.currentTimeMillis() + 15000);	// 토큰 만료 시간 5분
		
		System.out.println(byteArrayToHexString(TOKEN_KEY.getEncoded()));
		
		String rfsTkn = Jwts.builder()
				.setIssuer(ISS)
				.setSubject(REFRESH_TOKEN_SUB)
				.setAudience(id)
				.setId(UUID.randomUUID().toString())
				.setExpiration(EXP)
				.setIssuedAt(new Date())
				.claim(SCOPE, scope)
				.signWith(TOKEN_KEY)
				.compact();
		
		tknMap.put("userId", id);
		tknMap.put("token", rfsTkn);
		authMapper.insertToken(tknMap);
		
		System.out.println("REFRESH ID : " + id);
		System.out.println("REFRESH SCOPE : " + scope);
		System.out.println("REFRESH TOKEN : " + rfsTkn);
	}
	
	private String byteArrayToHexString(byte[] bytes){ 
		StringBuilder sb = new StringBuilder(); 
		for(byte b : bytes){ 
			sb.append(String.format("%02X", b&0xff)); 
		} 
		return sb.toString(); 
	} 
	
	private Key getTokenKey() {
		FileChannel fileChannel;
		String baseSecretPath = "서버 경로";
		String keyStr = "";
		String separatorStr = FileSystems.getDefault().getSeparator();
		String tokenPath = baseSecretPath+separatorStr+"token_key";
		try {
			fileChannel = FileChannel.open(Paths.get(tokenPath));
			ByteBuffer buffers = ByteBuffer.allocateDirect(100);
			Charset charset = Charset.forName("UTF-8");
			
			int byteCnt = 0;
			while(true){
				byteCnt=fileChannel.read(buffers);
				if(byteCnt==-1)break;
				buffers.flip();
				keyStr += charset.decode(buffers).toString();
				buffers.clear();
			}
			fileChannel.close();

		}
		catch(IOException e) {
			System.out.println("getTokenKey = " + e.getMessage());
		}
		return new SecretKeySpec(Base64Utils.decodeFromString(keyStr), HMAC_HASH);
	}
}
