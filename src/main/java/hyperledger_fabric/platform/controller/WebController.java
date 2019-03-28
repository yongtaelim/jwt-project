package hyperledger_fabric.platform.controller;

import java.util.Map;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import hyperledger_fabric.platform.service.WebService;

@RestController
public class WebController {
	
	@Autowired
	private WebService webService;
	
	@RequestMapping("home")
	public ModelAndView home(@RequestParam Map<String, Object> paramMap) {
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(webService.test(paramMap));
		return mav;
	}
	
	@RequestMapping("httpTest")
	public Map<String, Object> httpTest(@RequestParam Map<String, Object> paramMap) {
		System.out.println("Web Test");
		return webService.httpTest(paramMap);
	}
	
	@RequestMapping("JWTTest")
	public Map<String, Object> JWTTest(@RequestParam Map<String, Object> paramMap) {
		System.out.println("JWTTest");
		webService.JWTTest(paramMap);
		return null;
	}
}
