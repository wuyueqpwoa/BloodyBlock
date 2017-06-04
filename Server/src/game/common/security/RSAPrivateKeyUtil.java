package game.common.security;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * RSA私钥工具，包含解密和签名功能
 * Created by wuy on 2017/4/7.
 */
public class RSAPrivateKeyUtil {

	final private static Logger logger = LoggerFactory.getLogger(RSAPrivateKeyUtil.class);
	final private static String ALGORITHM = "RSA";
	final private static int KEY_LENGTH = 1024;
	final private static int DECRYPT_BLOCK_SIZE = KEY_LENGTH / 8;
	final private static PrivateKey PRIVATE_KEY = getPrivateKey();
	final private static String SIGNATURE_ALGORITHM = "SHA1withRSA";

	private static PrivateKey getPrivateKey() {
		try {
			String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMiFMAVH0cNLj97r4IYnF95/vpq2a2HIeQWys5ec7/BbSOdbitYWub7Eh7NuaQbLdsOFDUbBF2W74L+EhuDWfUHJP3Y+G4iEenWMv46vGMghLaCEYNuroUYrd93SxkjraxH5qm02M25X7fEMga0gm51TWF7Re184+dQBABhcpq2LAgMBAAECgYA5cFV6HcGLlNiIfb7aDta6iJM71CdcYkWwiSkCtBTTIAL5KvLRMr9QUxGgJ290X9IMXc+YxfUC0lel5LuSKj7El9N9T6ZD2SLL/QAVNZ+Z1A0UOcLa0d1OY5FxsiyBh1iPEO0TCaK3VpBAYT6Z/e8PnqcdGB0g+uKK1iwk3L9SKQJBAP2+EueNaR3H12D3h5MMtqHi3HYm7YV+1opgnhg9xW9Ug/qWBtDBb5OSzdalkG5jzG5NIgXAdMd9yydTiwzlevcCQQDKTeTzwAaeNBv/rfqfV8zGA0PtnrzevsM8rFIaNHYtatZACuVQ7PF0MSa1Tsrv+j06UTXaCCp3i7F1CUDPyUkNAkBSjKjHzhiZiK9IK59etq8f4ZdJlQaG/Km9YQtt5YK64msBdheHiLbM8uCmaTygvTT/2XYtGtyjHgiepa5CJtInAkAgoNOOZKgsl+b0I9FKJdUHEru/9Vws6MOAY6KSS2fMB3EqvxchKDYBMXQ2xdCBR/DcGQEAe1SuFz+1yppDaQl5AkEAm0o+VyWV4CtyC2nVRJuN+hKReFlqFSmFLcunx6fTiVRI0SGdwoF75Iuu7lBShLY9ZZ8vZ2U2KjZjtot7+L2UiQ==";
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyBase64));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			logger.error("getPrivateKey error:", e);
		}
		return null;
	}

	/**
	 * 解密
	 *
	 * @param cipherBytes 密文
	 * @return 明文
	 */
	public static byte[] decrypt(byte[] cipherBytes) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
			ByteArrayOutputStream plainBytes = new ByteArrayOutputStream();
			for (int i = 0, j; i < cipherBytes.length; i += j) {
				j = Math.min(DECRYPT_BLOCK_SIZE, cipherBytes.length - i);
				plainBytes.write(cipher.doFinal(cipherBytes, i, j));
			}
			return plainBytes.toByteArray();
		} catch (Exception e) {
			logger.error("decrypt error:", e);
		}
		return null;
	}

	/**
	 * 签名
	 *
	 * @param plainBytes 明文
	 * @return 签名
	 */
	public static byte[] sign(byte[] plainBytes) {
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(PRIVATE_KEY);
			signature.update(plainBytes);
			return signature.sign();
		} catch (Exception e) {
			logger.error("sign error:", e);
		}
		return null;
	}

	// 将Key打印成Python需要的格式
	private static String formatKeyForPython(String name, String key) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("'-----BEGIN ").append(name).append(" KEY-----\\n'\\\n");
		int length = key.length();
		for (int i = 0, j = 64; i < length; i = j, j += 64) {
			if (j > length) {
				j = length;
			}
			stringBuilder.append("'").append(key.substring(i, j)).append("\\n'\\\n");
		}
		stringBuilder.append("'-----END ").append(name).append(" KEY-----\\n'\n");
		return stringBuilder.toString();
	}

	// 产生公钥和私钥
	public static void createKey() throws NoSuchAlgorithmException, KeyStoreException {
		// 生成两套公私钥，服务器拿公钥A和私钥B，客户端拿公钥B和私钥A
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(KEY_LENGTH, new SecureRandom());
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
		String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
		System.out.println("公钥Base64：" + publicKey);
		System.out.println("公钥Python：\\\n" + formatKeyForPython("PUBLIC", publicKey));
		System.out.println("私钥Base64：" + privateKey);
		System.out.println("私钥python：\\\n" + formatKeyForPython("RSA PRIVATE", privateKey));
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException {
		createKey();

		byte[] plainBytes = "HelloWorld哈哈".getBytes();
		System.out.println("plainBytes length:" + plainBytes.length + ", plainBytes:" + ByteUtils.toHexString(plainBytes));

		byte[] cipherBytes = RSAPublicKeyUtil.encrypt(plainBytes);
		System.out.println("cipherBytes length:" + cipherBytes.length + ", cipherBytes:" + ByteUtils.toHexString(cipherBytes));

		plainBytes = RSAPrivateKeyUtil.decrypt(cipherBytes);
		System.out.println("plainBytes length:" + plainBytes.length + ", plainBytes:" + ByteUtils.toHexString(plainBytes));
	}
}
