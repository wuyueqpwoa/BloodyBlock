package game.common.security;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密和解密工具
 * Created by wuy on 2017/5/22.
 */
public class AESUtil {

	final private static Logger logger = LoggerFactory.getLogger(AESUtil.class);
	final private static String ALGORITHM = "AES";
	final private static String ALGORITHM_MODE = "AES/CBC/PKCS5Padding";
	final private static int KEY_LENGTH = 128;
	final private static IvParameterSpec IV = new IvParameterSpec("1q2w3e4r5t6y7u8i".getBytes());

	/**
	 * 产生随机密钥
	 *
	 * @return 密钥
	 */
	public static SecretKey createSecretKey() {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
			keygen.init(KEY_LENGTH, new SecureRandom());
			return keygen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 *
	 * @param plainBytes 明文
	 * @return 密文
	 */
	public static byte[] encrypt(byte[] plainBytes, SecretKey key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
			cipher.init(Cipher.ENCRYPT_MODE, key, IV);
			return cipher.doFinal(plainBytes);
		} catch (Exception e) {
			logger.error("encrypt error:", e);
		}
		return null;
	}

	/**
	 * 解密
	 *
	 * @param cipherBytes 密文
	 * @return 明文
	 */
	public static byte[] decrypt(byte[] cipherBytes, SecretKey key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);
			cipher.init(Cipher.DECRYPT_MODE, key, IV);
			return cipher.doFinal(cipherBytes);
		} catch (Exception e) {
			logger.error("decrypt error:", e);
		}
		return null;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SecretKey key = AESUtil.createSecretKey();
		System.out.println("SecretKey:" + ByteUtils.toHexString(key.getEncoded()));

		byte[] plainBytes = ByteUtils.fromHexString("83a169a56c6f67696ea170b882a76163636f756e74a3313233a870617373776f7264a131a163ad66726f6d5f6c735f6c6f67696e");
//		byte[] plainBytes = "HelloWorld哈哈".getBytes();
		System.out.println("plainBytes length:" + plainBytes.length + ", plainBytes:" + ByteUtils.toHexString(plainBytes));

		byte[] cipherBytes = AESUtil.encrypt(plainBytes, key);
		System.out.println("cipherBytes length:" + cipherBytes.length + ", cipherBytes:" + ByteUtils.toHexString(cipherBytes));

		plainBytes = AESUtil.decrypt(cipherBytes, key);
		System.out.println("plainBytes length:" + plainBytes.length + ", plainBytes:" + ByteUtils.toHexString(plainBytes));
	}
}
