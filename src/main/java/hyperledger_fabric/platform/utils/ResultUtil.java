package hyperledger_fabric.platform.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ResultUtil {
	private static final String RESULT_CODE = "rsltCd";
	private static final String RESULT_MESSAGE = "rsltMsg";
	
	private static Logger logger = Logger.getLogger(ResultUtil.class.getName());
	
	public static Map<String, Object> transactionResult(String code) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(RESULT_CODE, code);
		if(!MessageCode.S.equals(code))
			resultMap.put(RESULT_MESSAGE, MessageUtil.getMessage(code));
		logger.info(MessageUtil.getMessage(code));
		return resultMap;
	}
}
