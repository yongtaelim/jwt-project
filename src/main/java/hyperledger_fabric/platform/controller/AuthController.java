package hyperledger_fabric.platform.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import hyperledger_fabric.platform.service.AuthService;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping("jwtTest")
	public ModelAndView jwtTest(ModelAndView mav, @RequestParam Map<String, Object> paramMap) {
		return mav;
	}
	
	@RequestMapping("register")
	public Map<String, Object> register(@RequestParam Map<String, Object> paramMap) {
		 Map<String, Object> resultMap = authService.register(paramMap);
		return resultMap;
	}
	
	@RequestMapping("login")
	public Map<String, Object> login(@RequestParam Map<String, Object> paramMap, HttpSession session) {
		 Map<String, Object> resultMap = authService.login(paramMap, session);
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
