package hyperledger_fabric.platform.utils;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

	private static MessageSourceAccessor messageSourceAccessor = null;
	
	public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
		this.messageSourceAccessor = messageSourceAccessor;
	}
	
	public static String getMessage(String code) {
		return messageSourceAccessor.getMessage(code);
	}
	
}
