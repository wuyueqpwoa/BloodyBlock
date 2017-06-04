# coding=utf-8
import socket
import msgpack

from game.client.log.logger import log
from game.client.security.aes_util import AESUtil
from binascii import b2a_hex, a2b_hex, b2a_base64, a2b_base64

from game.client.security.rsa_util import RSAUtil


class SocketService:
	"""
	套接字服务
	"""

	def __init__(self, client, buff_size):
		self._client = client
		self._buff_size = buff_size
		self._socket = None
		self._rsa_util = RSAUtil()
		self._aes_util = None

	def start(self, host, port):
		"""
		启动连接
		"""
		self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self._socket.connect((host, port))

	def stop(self):
		"""
		停止
		"""
		self._socket.close()

	def set_aes_key(self, aes_key):
		"""
		设置AES密钥
		:param aes_key: AES密钥
		"""
		self._aes_util = AESUtil()
		self._aes_util.set_key(aes_key)

	def send(self, data, context=None):
		"""
		发送数据
		:param data: 数据(Java层LogicMessage)
		:param context: 回调上下文
		"""
		log('send data', data)
		data_pack = msgpack.packb(data)
		print_bytes('data_pack', data_pack)
		# AES初始化后，则使用AES加密
		if self._aes_util:
			data_pack = self._aes_util.encrypt(data_pack)
			print_bytes('aes_data_pack', data_pack)
		else:
			data_pack = self._rsa_util.encrypt(data_pack)
			print_bytes('rsa_data_pack', data_pack)
		message = b2a_base64(data_pack)
		print_bytes('send message', message)
		self._socket.sendall(message)

		# 如果有回调上下文，并且设置了回调函数名，则等待回调
		if context and data.get('c'):
			receive_data = self.receive()
			method_name = receive_data.get('i')
			param_pack = receive_data.get('p')
			if param_pack:
				param = msgpack.unpackb(param_pack)
			else:
				param = {}
			log('param', param)
			getattr(context, method_name)(param)

	def receive(self):
		"""
		接收数据
		:return: 数据
		"""
		message = self._socket.recv(self._buff_size)
		print_bytes('receive message', message)
		data_pack = a2b_base64(message)
		# AES初始化后，则使用AES解密
		if self._aes_util:
			print_bytes('aes_data_pack', data_pack)
			data_pack = self._aes_util.decrypt(data_pack)
		else:
			print_bytes('rsa_data_pack', data_pack)
			data_pack = self._rsa_util.decrypt(data_pack)
		print_bytes('data_pack', data_pack)
		data = msgpack.unpackb(data_pack)
		log('receive data', data)
		return data


def print_bytes(name, data):
	pass
	# log(name, len(data), b2a_hex(data))
