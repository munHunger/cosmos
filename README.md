# Git repo for the Cosmos system. 
This repo is intended for a micro-service based Movie/TV download system. Each service including the frontend web-interface has its own folder in the root of the repo. 

# Colors
Color-schema for the project is based on Dark-grey/orange

Orange: <span style="color:#FF5733">#FF5733</span>

Grey: <span style="color:#474747">#474747</span>

# Building
To build any service, run the grade build script for common as well as the gradle publish script before trying to build a service.
For each service: run the gradle war script and deploy the war to a tomcat server. note that each service must be deployed with an application context matching its service name, i.e. hte movies service should have the application context "/movies"
