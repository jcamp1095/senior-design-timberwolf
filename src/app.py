# /usr/bin/env python
# Download the twilio-python library from twilio.com/docs/libraries/python
from flask import Flask, request, render_template
from twilio.twiml.messaging_response import MessagingResponse
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map, icons
import googlemaps
from datetime import datetime
import os  
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from flask import send_from_directory
import image_slicer

app = Flask(__name__, template_folder="templates")
app.config['GOOGLEMAPS_KEY'] = 'AIzaSyC0zzB_Q8nHoJD4m0TNrYgV84buZdRQOnc'
GoogleMaps(app)
gmaps = googlemaps.Client(key='AIzaSyDREJYIfMrsNcZQCs09OalqjfHIdRsmHdA')
GOOGLE_CHROME_BIN = os.environ.get('GOOGLE_CHROME_BIN')
CHROMEDRIVER_PATH = os.environ.get('CHROMEDRIVER_PATH')


#chrome --headless --disable-gpu --screenshot https://www.google.com/maps/dir/42.408413,-71.1161627/42.608413,-71.1261627
#https://developers.google.com/web/updates/2017/04/headless-chrome
#https://duo.com/decipher/driving-headless-chrome-with-python

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

@app.route('/uploads/<filename>', methods=['GET', 'POST'])
def uploaded_file(filename):
    return send_from_directory('./',filename)

@app.route("/sms", methods=['POST'])
def sms_reply():
    # Start our response
    number = ""
    message_body = ""
    resp = MessagingResponse() #ewef

    if request:
        number = request.form['From']
        message_body = request.form['Body'].split('|')
        latlng = message_body[0]
        dest = message_body[1]
        ll = latlng.split(',')
        lat = ll[0]
        lng = ll[1]
        directions_result = gmaps.directions(latlng, dest, mode="driving", departure_time=datetime.now())
        print directions_result
        chrome_options = Options()
        chrome_options.binary_location = GOOGLE_CHROME_BIN
        chrome_options.add_argument("--headless")
        chrome_options.add_argument('--disable-gpu')
        chrome_options.add_argument('--no-sandbox')
        driver = webdriver.Chrome(executable_path=CHROMEDRIVER_PATH, chrome_options=chrome_options)
        driver.get("https://www.google.com/maps/dir/"+latlng+"/"+dest)
        #driver.get("https://97f64021.ngrok.io/map")
        #driver.get("https://hpneo.github.io/gmaps/examples/routes.html")
        driver.save_screenshot('output.png')
        driver.close()

        #backup plan
        image_slicer.slice('output.png', 2)
        #msg.media('https://97f64021.ngrok.io/uploads/{}'.format('output_01_02.png'))
        #TODO we can probably use the timberwold.herokuapp.com/map with pararms like ?lat=xxx etc. 

        #msg = resp.message(str(directions_result))
        msg = resp.message(str("TEST"))

        # Add a picture message
        #msg.media('https://97f64021.ngrok.io/uploads/{}'.format('output.png'))
        msg.media('https://97f64021.ngrok.io/uploads/{}'.format('output_01_02.png'))


    return str(resp)

if __name__ == "__main__":
    app.run(debug=True)