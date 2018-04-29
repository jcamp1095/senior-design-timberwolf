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
import MercatorProjection
import polyline as pline
import math
from time import sleep
import base64

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

@app.route('/uploads/<filename>', methods=['GET', 'POST'])
def uploaded_file(filename):
    return send_from_directory('./',filename)

@app.route("/sms", methods=['POST'])
def sms_reply():
    # Start our response
    number = ""
    message_body = ""
    resp = MessagingResponse() #ewef
    w = 300
    h = 500

    if request:
        number = request.form['From']
        message_body = request.form['Body'].split('|')
        start = message_body[0]
        dest = message_body[1]
        getimage = message_body[2]

        if getimage == '1':
            [os.remove(os.path.join("./",f)) for f in os.listdir("./") if f.endswith(".png")]
            encoded = base64.b64encode(start + dest)
            encoded = str(encoded) + '.png'
            chrome_options = Options()

            chrome_options.add_argument("--headless")
            chrome_options.add_argument("--window-size=300,500")
            chrome_options.add_argument("--disable-gpu")
            chrome_options.add_argument("--disable-application-cache")
            chrome_options.add_argument("--incognito")
            driver = webdriver.Chrome(executable_path=os.path.abspath("chromedriver"),   chrome_options=chrome_options)
            #driver.get("https://www.google.com/maps/dir/"+start+"/"+dest)
            #driver.get("https://timberwolf.herokuapp.com/map")

            driver.get("https://c79d375b.ngrok.io/map?start="+start+"&dest="+dest)
            driver.save_screenshot(encoded)
            driver.close()

            msg = resp.message("image")

            msg.media('https://aa497db1.ngrok.io/uploads/{}'.format(encoded))

        else:
            directions_result = gmaps.directions(start, dest, mode="driving", departure_time=datetime.now())

            lats = list()
            lngs = list()

            lines = pline.decode(directions_result[0]['overview_polyline']['points'])
            
            for line in lines:
                lats.append(line[0])
                lngs.append(line[1])

            GLOBE_WIDTH = 256 #a constant in Google's map projection
            west = min(lngs)
            east = max(lngs)
            angle = east - west
            if angle < 0:
                angle += 360

            north = max(lats)
            south = min(lats)

            anglens = abs(north) - abs(south)
            if anglens < 0:
                anglens += 360
            
            zoomew = math.floor(math.log(w * 360 / angle / GLOBE_WIDTH) / 0.6931471805599453)
            zoomns = math.floor(math.log(h * 360 / anglens / GLOBE_WIDTH) / 0.6931471805599453)
            zoom = min(zoomew, zoomns)

            centerPoint = MercatorProjection.G_LatLng((min(lats)+max(lats))/2, (min(lngs)+max(lngs))/2)
            corners = MercatorProjection.getCorners(centerPoint, zoom, w, h)
            msg = resp.message("E: " + str(corners['E']) + " N: " + str(corners['N']) + " S: " + str(corners['S']) + " W: " + str(corners['W'])) 


    return str(resp)

if __name__ == "__main__":
    app.run(debug=True)
   #app.run(host='127.0.0.1', port=8000, debug=True)


