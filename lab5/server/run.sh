#!/bin/bash

docker rm -f timer
docker run -d \
  --name timer \
  --restart=always \
  --cpus="8.4" \
  --memory="2g" \
  -p 8000:8000 \
  timer-server
