from flask import Flask, request, render_template
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map, icons
import googlemaps
from datetime import datetime
import json

app = Flask(__name__, template_folder="templates")

app.config['GOOGLEMAPS_KEY'] = 'AIzaSyC0zzB_Q8nHoJD4m0TNrYgV84buZdRQOnc'
gmaps = googlemaps.Client(key='AIzaSyDREJYIfMrsNcZQCs09OalqjfHIdRsmHdA')

GoogleMaps(app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route("/map", methods=['GET'])
def fullmap():
    latlng = request.args.get('latlng')
    dest = request.args.get('dest')
    
    directions_result = gmaps.directions(latlng, dest, mode="driving", departure_time=datetime.now())[0]
    lat = latlng.split(',')[0]
    lng = latlng.split(',')[1]

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
        lat = lat,
        lng = lng,
        #markers=[
        #     {
        #         'icon': '//maps.google.com/mapfiles/ms/icons/green-dot.png',
        #         'lat': 37.4419,
        #         'lng': -122.1419,
        #         'infobox': "Hello I am <b style='color:green;'>GREEN</b>!"
        #     },
        #     {
        #         'icon': '//maps.google.com/mapfiles/ms/icons/blue-dot.png',
        #         'lat': 37.4300,
        #         'lng': -122.1400,
        #         'infobox': "Hello I am <b style='color:blue;'>BLUE</b>!"
        #     },
        #     {
        #         'icon': icons.dots.yellow,
        #         'title': 'Click Here',
        #         'lat': 37.4500,
        #         'lng': -122.1350,
        #         'infobox': (
        #             "Hello I am <b style='color:#ffcc00;'>YELLOW</b>!"
        #             "<h2>It is HTML title</h2>"
        #             "<img src='//placehold.it/50'>"
        #             "<br>Images allowed!"
        #         )
        #     }
        # ],
        polyline = directions_result['overview_polyline']
    )

    return render_template('example_fullmap.html', fullmap=fullmap)

if __name__ == "__main__":
    app.run(debug=True)