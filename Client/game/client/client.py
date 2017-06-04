# coding=utf-8

from game.client.net.login_server_agent import LoginServerAgent


class Client:
	"""
	客户端
	"""

	def __init__(self):
		self._login_server_agent = LoginServerAgent()

	def get_login_server_agent(self):
		return self._login_server_agent


def main():
	client = Client()
	lsp = client.get_login_server_agent()
	lsp.start_socket_service('127.0.0.1', 18000)
	lsp.stop_socket_service()


if __name__ == '__main__':
	main()
