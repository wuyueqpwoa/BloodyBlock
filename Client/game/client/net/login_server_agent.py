# coding=utf-8
import msgpack

from game.client.log.logger import log
from game.client.net.socket_service import SocketService


class LoginServerAgent:
	"""
	登录服务器代理
	"""

	def __init__(self):
		# 最大帧长度10Mb=1310720B
		self._socket_service = SocketService(self, 1024 * 1024 * 10)

	def start_socket_service(self, host, port):
		self._socket_service.start(host, port)

	def stop_socket_service(self):
		self._socket_service.stop()

	def to_ls_shake_hand(self, param):
		log('to_ls_shake_hand:', param)
		data = {
			'i': 'shakeHand',
			'p': msgpack.packb(param),
			'c': 'from_ls_shake_hand'
		}
		self._socket_service.send(data, self)

	def from_ls_shake_hand(self, param):
		log('from_ls_shake_hand:', param)
		self._socket_service.set_aes_key(param.get('aes_key'))

	def to_ls_login(self, param):
		log('to_ls_login:', param)
		data = {
			'i': 'login',
			'p': msgpack.packb(param),
			'c': 'from_ls_login'
		}
		self._socket_service.send(data, self)

	def from_ls_login(self, param):
		log('from_ls_login:', param)
