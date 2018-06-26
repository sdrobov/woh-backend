#!/bin/bash

docker stop woh_backend 2>&1 > /dev/null
docker run \
    -d \
    --rm \
    --name=woh_backend \
    -p 8080:8080 \
    --net=host \
    -e DATABASE_HOST=localhost \
    -e DATABASE_NAME=woh \
    -e DATABASE_USER=woh \
    -e DATABASE_PASSWORD=qwerty \
    -e MONGO_HOST=localhost \
    -e MONGO_DB=woh \
registry.gitlab.com/woh/backend-spring