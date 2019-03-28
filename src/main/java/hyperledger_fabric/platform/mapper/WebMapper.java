package hyperledger_fabric.platform.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public interface WebMapper {
	public Map<String, Object> test(Map<String, Object> paramMap);
}
