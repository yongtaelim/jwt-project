package hyperledger_fabric.platform.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyperledger_fabric.platform.mapper.AuthMapper;
import hyperledger_fabric.platform.utils.MessageCode;
import hyperledger_fabric.platform.utils.ResultUtil;
import hyperledger_fabric.platform.utils.TokenUtil;

@Service
public class AuthService {
	
	@Autowired
	private AuthMapper authmapper;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	
	public Map<String, Object> register(Map<String, Object> paramMap) {
		authmapper.registerUser(paramMap);
		return ResultUtil.transactionResult(MessageCode.S);
	}
	/**
	 * 사용자 로그인
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> login(Map<String, Object> paramMap, HttpSession session) {
		Map<String, Object> userMap = authmapper.chkUser(paramMap);
		if(userMap == null) {
			return ResultUtil.transactionResult(MessageCode.E0002);
		}
		
		String id = userMap.get("userId").toString();
		String scope = userMap.get("scope").toString();
		
		tokenUtil.createAccessToken(session, id, scope);
		tokenUtil.createRefreshToken(id, scope);
		return ResultUtil.transactionResult(MessageCode.S);
		
	}

	/**
	 * 사용자 로그아웃
	 * @param paramMap
	 * @param session
	 * @return
	 */
	public Map<String, Object> logout(Map<String, Object> paramMap, HttpSession session) {
		tokenUtil.deleteToken(session);
		return ResultUtil.transactionResult(MessageCode.S);
	}
	
	/**
	 * transaction Test
	 * @param paramMap
	 * @param session
	 * @return
	 */
	public Map<String, Object> transactionTest(Map<String, Object> paramMap, HttpSession session) {
		return tokenUtil.auth(session);
	}
	
}
