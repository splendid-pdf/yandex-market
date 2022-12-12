# shopsystem-service

In order to use docker compose file enter command "docker compose up", look at logs and if everything is Ok go to localhost:5050 and follow the steps.
- enter "docker ps" in terminal, pick a postgres container and copy the id.
- run "docker inspect [id_of_containter]"
- grab host value
- register new server use the host you have copied in the last step
- you are good to go to interlij idea and configure your application to connect