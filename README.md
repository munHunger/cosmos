# Git repo for the Cosmos system. 
This repo is intended for a micro-service based Movie/TV management system. Each service including the frontend web-interface has its own folder in the root of the repo. 

# Backlog
1: Refactor the common package to be more "modern"
2: Finish prefetch cache function (create testing)
3: Create Database access objects for each model.
4: Add status to each movie object (wanted, released, downloading, done...)
5: Add test cases for movies
6: Add an endpoint to movies for searching for single movie.
7: Investigate centralized logging functionality. Should be a microservice. Think appdynamics.

# Building
To build any service, run the gradle publish script for common as well as the gradle war script for any given service to build.
For deployment move the war file to the tomcat webapps folder. Note that each service must be deployed with an application context matching its service name. i.e. the movies service should have the application context "/movies". This is achieved by renaming the war file to "movies" in the tomcat webapps folder.
