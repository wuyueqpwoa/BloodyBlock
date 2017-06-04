# coding=utf-8
from binascii import b2a_hex, a2b_hex

from Crypto import Random
from Crypto.Cipher import PKCS1_v1_5
from Crypto.PublicKey import RSA


class RSAUtil:
	"""
	RSA加密和解密工具，用于交换AES密钥
	"""

	def __init__(self):
		public_key_str = \
			'-----BEGIN RSA PUBLIC KEY-----\n'\
			'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIhTAFR9HDS4/e6+CGJxfef76a\n'\
			'tmthyHkFsrOXnO/wW0jnW4rWFrm+xIezbmkGy3bDhQ1GwRdlu+C/hIbg1n1ByT92\n'\
			'PhuIhHp1jL+OrxjIIS2ghGDbq6FGK3fd0sZI62sR+aptNjNuV+3xDIGtIJudU1he\n'\
			'0XtfOPnUAQAYXKatiwIDAQAB\n'\
			'-----END RSA PUBLIC KEY-----\n'
		private_key_str = \
			'-----BEGIN RSA PRIVATE KEY-----\n'\
			'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM5Zb+fBEn4nBWAt\n'\
			'Ja3rapN/idNg3RRXXUuZhFOAAuDJSZD6l3VGiRhYLjppvNVoB7FPjVsx+R/Q4udx\n'\
			'nW+xkZmyLxTn/0wLf8sEWX5Pv89V0fKiJ2+N8vQjRmbLoFn16TkqKdlJKOrlyDmd\n'\
			'JIS2mAKT/Vv0HefkyjOWKke+ptylAgMBAAECgYA6BOW1GRFVDS2yxB7zDt1Riau8\n'\
			'FmEzBfmCdzDAtZ6Zi582t278l4+Wm7yrye1BZ9W54i9Rs/RjIL2b69Mjg573QdDh\n'\
			'U4Xn50LRIOC3FApSS3k99BJjIsnksXPE99gCLhoCX+4xrQwH7sNWSYaup69ZvM1o\n'\
			'641OAu7/z2/2KtAVgQJBAO4p+qfjlW4Ujj6qOV3yEMMhEcLK/ugYHlYle9F17p+P\n'\
			'8V6Y0d62s8Bi3SUy1yo727JqR1pR/LWF9seDojYwsgUCQQDdzYQbW+Q44bHWArys\n'\
			'w/kltZ+vDTUS3r8Gw0aJAqTvSlbXfg6b3czpaudXaGp7uvovtG08iDKzYrUqykz4\n'\
			'cmIhAkEA0UJDQaaekjUvcnIAq8HX5os03tGbvXQLm6edrkIPo1VHP9XIeQZQ09ZJ\n'\
			'9rhAA5TKiW4GOZ7WCuylE1JegXyxvQJBAIz4Uu9kjv/MClAfuzT+9ToPXmdOuJ2+\n'\
			'rqCougrYgGWJ1PnvJ2/PH653AIPgXFU5eEubnBXt5nN21/hTTWpWoKECQFDj/QaR\n'\
			'uwIrTbaL665Cq3GCwqENsUIg4f7WS8n2u4Z4nVaT/MwOvlhmMx1nsZ1WItlDSBzL\n'\
			'tVkiz5ZPmer07WI=\n'\
			'-----END RSA PRIVATE KEY-----\n'
		self._sentinel = Random.new().read
		self._cipher_encrypt = PKCS1_v1_5.new(RSA.importKey(public_key_str))
		self._cipher_decrypt = PKCS1_v1_5.new(RSA.importKey(private_key_str))

	def encrypt(self, plain_bytes):
		"""
		加密
		:param plain_bytes: 明文
		:return: 密文
		"""
		return self._cipher_encrypt.encrypt(plain_bytes)

	def decrypt(self, cipher_bytes):
		"""
		解密
		:param cipher_bytes: 密文
		:return: 明文
		"""
		return self._cipher_decrypt.decrypt(cipher_bytes, self._sentinel)


if __name__ == '__main__':
	rsa_util = RSAUtil()

	plain_bytes = b'HelloWorld哈哈'
	print 'plain_bytes length:', len(plain_bytes), 'plain_bytes:', b2a_hex(plain_bytes)

	cipher_bytes = rsa_util.encrypt(plain_bytes)
	# cipher_bytes = a2b_hex(b'')
	print 'cipher_bytes length:', len(cipher_bytes), 'cipher_bytes:', b2a_hex(cipher_bytes)

	plain_bytes = rsa_util.decrypt(cipher_bytes)
	print 'plain_bytes length:', len(plain_bytes), 'plain_bytes:', b2a_hex(plain_bytes)
