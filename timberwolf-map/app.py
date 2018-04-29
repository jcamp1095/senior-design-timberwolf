from flask import Flask, request, render_template
from flask_googlemaps import GoogleMaps
from flask_googlemaps import Map, icons
import googlemaps
from datetime import datetime
from flask import jsonify
import polyline as pline
import math

app = Flask(__name__, template_folder="templates")

app.config['GOOGLEMAPS_KEY'] = 'AIzaSyC0zzB_Q8nHoJD4m0TNrYgV84buZdRQOnc'
gmaps = googlemaps.Client(key='AIzaSyDREJYIfMrsNcZQCs09OalqjfHIdRsmHdA')

GoogleMaps(app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route("/map", methods=['GET'])
def fullmap():
    start = request.args.get('start')
    dest = request.args.get('dest')
    w = 300
    h = 500
    directions_result = gmaps.directions(start, dest, mode="driving", departure_time=datetime.now())[0]
    lats = list()
    lngs = list()
    lines = pline.decode(directions_result['overview_polyline']['points'])
    
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


    plinemap = Map(
        identifier="plinemap",
        varname="plinemap",
        style=(
            "height:"+str(h)+"px;"
            "width:"+str(w)+"px;"
            "top:0;"
            "left:0;"
            "position:absolute;"
            "z-index:200;"
        ),
        zoom=zoom,
        lat=(min(lats)+max(lats))/2,
        lng=(min(lngs)+max(lngs))/2,
        polylines=[lines]
    )

    return render_template('example_fullmap.html', plinemap=plinemap)

if __name__ == "__main__":
    app.run(host='127.0.0.1', port=8000, debug=True)