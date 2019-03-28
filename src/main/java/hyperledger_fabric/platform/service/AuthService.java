package hyperledger_fabric.platform.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import hyperledger_fabric.platform.utils.MessageCode;
import hyperledger_fabric.platform.utils.ResultUtil;
import hyperledger_fabric.platform.utils.TokenUtil;

@Service
public class AuthService {
	
//	@Autowired
//	private AuthMapper authmapper;
	
	
	/**
	 * 사용자 로그인
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> login(Map<String, Object> paramMap, HttpSession session) {
		Map<String, Object> userMap = new HashMap<String, Object>();
//		userMap = authMapper.getUser(paramMap);
		String id = "test"; 
		String scope = "admin";
		TokenUtil.createAccessToken(session, id, scope);
		TokenUtil.createRefreshToken(id, scope);
		return ResultUtil.transactionResult(MessageCode.S);
	}

	public Map<String, Object> logout(Map<String, Object> paramMap, HttpSession session) {
		TokenUtil.deleteToken(session);
		return ResultUtil.transactionResult(MessageCode.S);
	}
	
	public Map<String, Object> transactionTest(Map<String, Object> paramMap, HttpSession session) {
		TokenUtil.auth(session);
		return ResultUtil.transactionResult(MessageCode.S);
	}
	
}
