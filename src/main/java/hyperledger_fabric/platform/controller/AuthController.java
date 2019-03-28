package hyperledger_fabric.platform.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hyperledger_fabric.platform.service.AuthService;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping("login")
	public Map<String, Object> login(@RequestParam Map<String, Object> paramMap, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = authService.login(paramMap, session);
		return resultMap;
	}
	
	@RequestMapping("transactionTest")
	public Map<String, Object> transactionTest(@RequestParam Map<String, Object> paramMap, HttpSession session) {
		Map<String, Object> resultMap = authService.transactionTest(paramMap, session);
		return resultMap;
	}
	
	@RequestMapping("logout")
	public Map<String, Object> logout(@RequestParam Map<String, Object> paramMap, HttpSession session) {
		Map<String, Object> resultMap = authService.logout(paramMap, session);
		return resultMap;
	}
}
