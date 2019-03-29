package hyperledger_fabric.platform.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public interface AuthMapper {
	public Map<String, Object> chkUser(Map<String, Object> paramMap);
	
	public Map<String, Object> getUser(Map<String, Object> paramMap);
	
	public void insertToken(Map<String, Object> paramMap);
	
	public void registerUser(Map<String, Object> paramMap);
	
	public void deleteToken(Map<String, Object> paramMap);
}
