# Senior Capstone Project - SMS   Navigation

##### Team Timberwolf
##### Michael Morisi, Tommy Tang, Joe Campbell
##
##### Sponser: Fahad Dogar
##


##### Overview

The overall goal of this project is to provide the ability to communicate with a 
backend server via SMS communication. To prove the technology, we will develop a navigation application which relies on using SMS to push a user’s location and receive feedback regarding their surrounding location.

##### Setup Instructions

1. After pulling from the repository, change into the src directory

2. Set up a virtual environment by running `virtualenv --no-site-packages .`

3. Activate the environment by running `source bin/activate`

4. Install dependencies by running `bin/pip install -r requirements.txt`

5. Run the app by running `python run.py`

6. In a different tab, run Ngrok through the command `./ngrok http 5000`

7. On the Twilio console, edit the "A message comes in" field under the Messaging section with the secure Forwarding URL in the Ngrok window.