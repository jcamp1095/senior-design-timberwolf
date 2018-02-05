# /usr/bin/env python
# Download the twilio-python library from twilio.com/docs/libraries/python
from twilio.rest import Client

# Find these values at https://twilio.com/user/account
account_sid = "AC7c0c1eaf04d5f4e68b50afbf7a80a9dc" #TODO put in environment variable
auth_token = "a603491a820e6270c24ea9879e6b0e11"

client = Client(account_sid, auth_token)

client.api.account.messages.create(
    to="+19784967323",
    from_="+16178588563",
    body="Hello there!")