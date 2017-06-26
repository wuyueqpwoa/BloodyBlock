# coding=utf-8

from game.client.client import Client

"""
测试与服务器握手，并且得到AES密钥
"""


def main():
	client = Client()
	lsp = client.get_login_server_agent()
	lsp.start_socket_service('127.0.0.1', 18100)
	lsp.to_ls_shake_hand({})
	lsp.to_ls_login({
		'account': '123',
		'password': '1'
	})
	lsp.stop_socket_service()


if __name__ == '__main__':
	main()
