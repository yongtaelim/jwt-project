package hyperledger_fabric.platform.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenUtil {
	private static final String ISS = "ADMIN";
	private static final String ACCESS_TOKEN_SUB = "accTkn";
	private static final String REFRESH_TOKEN_SUB = "rfsTkn";
	
	private static final String ID = "id";
	private static final String ACCESS_TOKEN = "accTkn";
	private static final String SCOPE = "scope";
	
	private static final Key TOKEN_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	/**
	 * Token 인증
	 * @param paramMap
	 * @param session
	 * @return
	 */
	public static Map<String, Object> auth(HttpSession session) {
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
				paramMap.put("id", session.getAttribute(ID));
//				Map<String, Object> tknMap = mapper.getRefreshTkn(paramMap);
//				if(tknMap == null) {
//					return ResultUtil.transactionResult(MessageCode.E0000);
//				}
//				String refreshTkn = tknMap.get("refreshTkn").toString();
				String refreshTkn = "refreshTkn Test Data 넣어야한다.";
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
	public static void deleteToken(HttpSession session) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", ID);
		//Refresh Token 정보 삭제
//		authMapper.deleteToken(paramMap);
		
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
	public static void createAccessToken(HttpSession session, String id, String scope) {
		Date EXP = new Date(System.currentTimeMillis() + 60000);	// 토큰 만료 시간 1분
		
		System.out.println(byteArrayToHexString(TOKEN_KEY.getEncoded()));
		
		String accTkn = Jwts.builder()
				.setIssuer(ISS)
				.setSubject(ACCESS_TOKEN_SUB)
				.setAudience(id)
				.setId(UUID.randomUUID().toString())
				.setExpiration(EXP)
				.setIssuedAt(new Date())
				.claim(SCOPE, scope)
				.signWith(TOKEN_KEY)
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
	public static void createRefreshToken(String id, String scope) {
		Map<String, Object> tknMap = new HashMap<String, Object>();
		Date EXP = new Date(System.currentTimeMillis() + 300000);	// 토큰 만료 시간 5분
		
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
		
		tknMap.put("rfsTkn", rfsTkn);
//		authmapper.setLogin(tknMap);
		
		System.out.println("REFRESH ID : " + id);
		System.out.println("REFRESH SCOPE : " + scope);
		System.out.println("REFRESH TOKEN : " + rfsTkn);
	}
	
	private static String byteArrayToHexString(byte[] bytes){ 
		StringBuilder sb = new StringBuilder(); 
		for(byte b : bytes){ 
			sb.append(String.format("%02X", b&0xff)); 
		} 
		return sb.toString(); 
	} 
}
