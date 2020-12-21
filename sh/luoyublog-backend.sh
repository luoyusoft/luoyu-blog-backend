#!/bin/bash
curl https://luoyublog.com/api/luoyublog/actuator/shutdown -X POST
sleep 20
docker rm luoyublog
docker rmi luoyusoft/luoyublog:latest
docker run -dit --name luoyublog -v /usr/local/docker/luoyublog-backend/log:/usr/local/project/luoyublog-backend/log -v /etc/localtime:/etc/localtime:ro -p 8800:8800 -p 9999:9999 luoyusoft/luoyublog:latest