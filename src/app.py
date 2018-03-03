# /usr/bin/env python
# Download the twilio-python library from twilio.com/docs/libraries/python
from flask import Flask, request, render_template
from twilio.twiml.messaging_response import MessagingResponse
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map, icons
import googlemaps
from datetime import datetime

app = Flask(__name__, template_folder="templates")
app.config['GOOGLEMAPS_KEY'] = 'AIzaSyC0zzB_Q8nHoJD4m0TNrYgV84buZdRQOnc'
GoogleMaps(app)
gmaps = googlemaps.Client(key='AIzaSyDREJYIfMrsNcZQCs09OalqjfHIdRsmHdA')

@app.route("/", methods=['GET'])
def hello():
    return "hello world"

@app.route("/map", methods=['GET'])
def fullmap():
    fullmap = Map(
        identifier="fullmap",
        varname="fullmap",
        style=(
            "height:100%;"
            "width:100%;"
            "top:0;"
            "left:0;"
            "position:absolute;"
            "z-index:200;"
        ),
        lat=37.4419,
        lng=-122.1419,
        markers=[
            {
                'icon': '//maps.google.com/mapfiles/ms/icons/green-dot.png',
                'lat': 37.4419,
                'lng': -122.1419,
                'infobox': "Hello I am <b style='color:green;'>GREEN</b>!"
            },
            {
                'icon': '//maps.google.com/mapfiles/ms/icons/blue-dot.png',
                'lat': 37.4300,
                'lng': -122.1400,
                'infobox': "Hello I am <b style='color:blue;'>BLUE</b>!"
            },
            {
                'icon': icons.dots.yellow,
                'title': 'Click Here',
                'lat': 37.4500,
                'lng': -122.1350,
                'infobox': (
                    "Hello I am <b style='color:#ffcc00;'>YELLOW</b>!"
                    "<h2>It is HTML title</h2>"
                    "<img src='//placehold.it/50'>"
                    "<br>Images allowed!"
                )
            }
        ],
        # maptype = "TERRAIN",
        # zoom="5"
    )
    return render_template('example_fullmap.html', fullmap=fullmap)


@app.route("/sms", methods=['POST'])
def sms_reply():
    # Start our response
    number = ""
    message_body = ""
    resp = MessagingResponse()

    if request:
        number = request.form['From']
        message_body = request.form['Body'].split('|')
        latlng = message_body[0]
        dest = message_body[1]
        directions_result = gmaps.directions(latlng, dest, mode="driving", departure_time=datetime.now())



    # Add a message    
    resp.message("Ahoy! Thanks so much for your message.\n" +
                 "Your Number is: " + number + "\nYour message was: " +  
                 message_body)

    return str(resp)

if __name__ == "__main__":
    app.run(debug=True)