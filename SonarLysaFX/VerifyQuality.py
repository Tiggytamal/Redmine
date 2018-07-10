#
# Copyright 2017 XEBIALABS
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#


import json
from xlrelease.HttpRequest import HttpRequest

if not sonarServer:
    raise Exception("Sonar server ID must be provided")

sonar_server_api_url = '/api/measures/component?componentKey=%s&metricKeys=alert_status' % resource
http_request = HttpRequest(sonarServer, username, password)
sonar_response = http_request.get(sonar_server_api_url)
if not sonar_response.isSuccessful():
    raise Exception("Failed to get Sonar Measures. Server return [%s], with content [%s]" % (sonar_response.status, sonar_response.response))
json_data = json.loads(sonar_response.getResponse())
data = {}
for item in json_data['component']['measures']:
    data[item['metric']] = item['value']

error_message = ""
if 'alert_status' in data and 'ERROR' == str(data["alert_status"]):
    error_message += "+ Quality Gate not green "

if error_message:
    raise Exception("Failed quality verifications:\n%s" % error_message)
print "Code qualification verified"