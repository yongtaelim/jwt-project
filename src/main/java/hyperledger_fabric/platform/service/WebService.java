package hyperledger_fabric.platform.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyperledger_fabric.platform.utils.HttpConnection;

@Service
public class WebService {
	
//	@Autowired
//	private OnlineStoreMapper onlineStoreMapper;
	
	@Autowired
	private HttpConnection httpConnection;
	
	public Map<String, Object> test(Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return resultMap;
	}
	
	public Map<String, Object> httpTest(Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = httpConnection.send("httpTest", paramMap);
		return resultMap;
	}
	
	public Map<String, Object> JWTTest(Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
//		jwts
		
		return resultMap;
	}
}
