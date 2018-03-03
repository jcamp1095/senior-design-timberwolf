# /usr/bin/env python
# Download the twilio-python library from twilio.com/docs/libraries/python
from flask import Flask, request
from twilio.twiml.messaging_response import MessagingResponse
import googlemaps
from datetime import datetime

app = Flask(__name__)

gmaps = googlemaps.Client(key='AIzaSyDREJYIfMrsNcZQCs09OalqjfHIdRsmHdA')

@app.route("/", methods=['GET'])
def hello():
    return "hello world"
    
@app.route("/sms", methods=['POST'])
def sms_reply():

    # Start our response
    number = ""
    message_body = ""
    resp = MessagingResponse()

    if request:
        number = request.form['From']
        message_body = request.form['Body']

    # Add a message
    resp.message("Ahoy! Thanks so much for your message.\n" +
                 "Your Number is: " + number + "\nYour message was: " +  
                 message_body)

    return str(resp)

if __name__ == "__main__":
    app.run(debug=True)