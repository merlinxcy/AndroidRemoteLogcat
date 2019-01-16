from flask import Flask,request
import base64

app = Flask(__name__)

@app.route('/log', methods=["POST"])
def logmonitor():
    data = request.form['logdata']
    # print(data)
    print(base64.b64decode(data))
    return 'ok'


if __name__ == '__main__':
    app.run(port=12345,host='0.0.0.0')
