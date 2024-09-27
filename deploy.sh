#!/bin/bash

# Pull the image (optional if built locally)
# docker pull s0{StudentCode}:eval1

# Remove existing containers (if any)
docker rm -f $(docker ps -aq --filter "ancestor=s0201611656:eval1")

# Deploy 30 replicas
for i in {1..30}
do
  docker run -d --name animal_service_$i -p $((40000 + $i)):40001 s0{StudentCode}:eval1
done
