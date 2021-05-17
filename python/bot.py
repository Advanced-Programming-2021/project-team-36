from telnetlib import Telnet

import requests

bot_token = "1876192370:AAFQG3r2zT8Ai1rx1BAKKWac0XOkl6aJ0Zo"

def __request(method_name, files=None, **params):
    return requests.post(
        url=f"https://api.telegram.org/bot{bot_token}/{method_name}",
        data=params,
        proxies={
            'https': '192.168.43.1:8080'
        },
        files=files
    ).json().get('result')


offset = -1


def send_message(message, **params):
    return __request("sendMessage", text=message, **params)


def get_updates():
    global offset
    updates = __request("getUpdates", offset=offset+1)
    for update in updates:
        offset = update['update_id']
    return updates


message_pile = []


def get_next_update():
    global message_pile
    while len(message_pile) == 0:
        message_pile += get_updates()
    _update = message_pile[0]
    message_pile = message_pile[1:]
    return _update

with Telnet('127.0.0.1', 5000) as tn:
    while True:
        update = get_next_update()
        print(update)
        if 'message' in update and 'text' in update['message']:
            print('got message', update['message']['text'])
            tn.write(bytes(update['message']['text'][1:] + "\n", 'utf-8'))
            java = tn.read_until(b"tamaaam\n").decode('utf-8')
            print("message is ", java) 
            response = send_message(java, chat_id=update['message']['chat']['id'])
            print("response: ", response)
