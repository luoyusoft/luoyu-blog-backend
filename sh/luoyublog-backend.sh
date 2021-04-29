#!/bin/bash
curl https://jinhx.cc/api/actuator/shutdown -X POST
sleep 20
docker rm luoyublog
docker rmi luoyusoft/luoyublog:latest
docker run -dit --name luoyublog -v /usr/local/docker/luoyublog-backend/log:/usr/local/project/luoyublog-backend/log -v /etc/localtime:/etc/localtime:ro -p 8800:8800 -p 9999:9999 -p 465:465 luoyusoft/luoyublog:latest