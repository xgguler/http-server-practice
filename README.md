# Pipedrive Integration Sample Application

A sample Java Http Server application which accepts different requests in parallel.

## Requirements
* Java
* Maven
* Postman
* An IDE or text editor (Intellij or Eclipse)

## Execution Steps
* Clone GitHub repository
* Build project
* Download dependencies
* Run Main.java class
* Application will be started on localhost 1337 port http://localhost:1337.
* To reach postman collection visit [here](Http Server.postman_collection.json)

## Curl Requests Definitions
* curl --location --request POST 'http://localhost:1337/' \
  --header 'Content-Type: text/plain' \
  --data-raw '1'
* curl --location --request POST 'http://localhost:1337/' \
  --header 'Content-Type: text/plain' \
  --data-raw '2'
* curl --location --request POST 'http://localhost:1337/' \
  --header 'Content-Type: text/plain' \
  --data-raw '3'
* curl --location --request POST 'http://localhost:1337/' \
  --header 'Content-Type: text/plain' \
  --data-raw '4 end'