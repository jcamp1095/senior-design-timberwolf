<<<<<<< HEAD
# Senior Capstone Project: SMS-based Navigation
## Team Timberwolf
### Joe Campbell, Michael Morisi, Tommy Tang

# Project Goal
With the increasing prevalence of smartphones in the world, there is a
corresponding increase in the amount of citizens of developing countries who possess a
smartphone, but the country they reside in doesn't have a fittingly advanced enough
cellular infrastructure to be able to use cellular data in apps. This makes the usage of
many types of common applications, including map and navigation apps, extremely difficult.
We developed this app in part to make it possible for those without reliable cellular
service to be able to use navigation apps.

# Main Software Components
The frontend of our app is a Java Android application, and the backend is written in Flask.
Twilio is employed in the app to handle the sending and receiving of text messages between
the frontend and backend. These texts dictate the route that will be generated and returned
to the user based on the start and end destinations they specify. 

# Building and running
1. After cloning the repository, change into the src directory

2. Set up a virtual environment (if not done already) by running `virtualenv --no-site-packages .`

3. Activate the environment by running `source bin/activate`

4. Install dependencies by running `bin/pip install -r requirements.txt`

5. Run the app by running `python app.py`

6. In a different tab, run Ngrok through the command `./ngrok http 5000`

7. On the Twilio console, edit the "A message comes in" field under the Messaging section with the secure Forwarding URL in the Ngrok window.

# Future Work
Integrating MMS messages into the app that we've built (e.g. the pictures we generate) was the main challenge
we encountered in our work, and while we correctly generate the image with the correct route, and while we
are able to track our location as they travel all without using cellular data, getting an MMS message into
our app is extremely difficult.
