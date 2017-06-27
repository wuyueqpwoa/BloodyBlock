package game.util.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA公钥工具，包含加密和验签功能
 * Created by wuy on 2017/4/7.
 */
public class RSAPublicKeyUtil {

	final private static Logger logger = LoggerFactory.getLogger(RSAPublicKeyUtil.class);
	final private static String ALGORITHM = "RSA";
	final private static int KEY_LENGTH = 1024;
	final private static int ENCRYPT_BLOCK_SIZE = KEY_LENGTH / 8 - 11;
	final private static PublicKey PUBLIC_KEY = getPublicKey();
	final private static String SIGNATURE_ALGORITHM = "SHA1withRSA";

	private static PublicKey getPublicKey() {
		try {
			String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOWW/nwRJ+JwVgLSWt62qTf4nTYN0UV11LmYRTgALgyUmQ+pd1RokYWC46abzVaAexT41bMfkf0OLncZ1vsZGZsi8U5/9MC3/LBFl+T7/PVdHyoidvjfL0I0Zmy6BZ9ek5KinZSSjq5cg5nSSEtpgCk/1b9B3n5MozlipHvqbcpQIDAQAB";
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyBase64));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			logger.error("getPublicKey error:", e);
		}
		return null;
	}

	/**
	 * 加密
	 *
	 * @param plainBytes 明文
	 * @return 密文
	 */
	public static byte[] encrypt(byte[] plainBytes) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
			ByteArrayOutputStream cipherBytes = new ByteArrayOutputStream();
			for (int i = 0, j; i < plainBytes.length; i += j) {
				j = Math.min(ENCRYPT_BLOCK_SIZE, plainBytes.length - i);
				cipherBytes.write(cipher.doFinal(plainBytes, i, j));
			}
			return cipherBytes.toByteArray();
		} catch (Exception e) {
			logger.error("encrypt error:", e);
		}
		return null;
	}

	/**
	 * 验证签名
	 *
	 * @param plainBytes 明文
	 * @param signBytes  签名
	 * @return 验签成功返回true；否则返回false
	 */
	public static boolean verify(byte[] plainBytes, byte[] signBytes) {
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(PUBLIC_KEY);
			signature.update(plainBytes);
			return signature.verify(signBytes);
		} catch (Exception e) {
			logger.error("verify error:", e);
		}
		return false;
	}
}
