package hyperledger_fabric.platform.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	
	public static final String AES = "AES";
	public static final String AES_MODE = "AES/CBC/PKCS5Padding";
	public static final int KEY_SIZE = 16;	// 16 Bytes
	public static final String AES_KEY = "cnYUpP/TTzjKvqAoMrmKjQ==";
	public static final String AES_IV = "VplFSrNlgErN60Ab+s8sCA==";
	
	public static String encrypt(String symmetricKey, String initialVector, String plainTxt) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		plainTxt = Base64.getEncoder().encodeToString(plainTxt.getBytes());
		byte[] keyData = Base64.getDecoder().decode(symmetricKey);
		
	    SecretKey secureKey = new SecretKeySpec(keyData, AES);
     	Cipher c = Cipher.getInstance(AES_MODE);
     	c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(Base64.getDecoder().decode(initialVector)));
	 
     	byte[] encrypted = c.doFinal(plainTxt.getBytes("UTF-8"));
     	String result = new String(Base64.getEncoder().encodeToString(encrypted));
		return result;
	}
	
	public static String decrypt(String symmetricKey, String initialVector, String cipherTxt) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] keyData = Base64.getDecoder().decode(symmetricKey);
		
		SecretKey secureKey = new SecretKeySpec(keyData, AES);
		Cipher c = Cipher.getInstance(AES_MODE);
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(Base64.getDecoder().decode(initialVector)));
	 
		byte[] byteStr = Base64.getDecoder().decode(cipherTxt);
	 
		String result = new String(Base64.getDecoder().decode(c.doFinal(byteStr)));
		return result;
	}
}
