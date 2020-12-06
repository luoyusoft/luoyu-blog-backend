#!/bin/bash
curl https://luoyublog.com/api/luoyublog/actuator/shutdown -X POST
sleep 20
docker rm luoyublog-backend
docker rmi luoyusoft/luoyublog-backend:latest
docker run -dit --name luoyublog-backend -v /usr/local/docker/luoyublog-backend/log:/usr/local/project/luoyublog-backend/log -p 8800:8800 -p 9999:9999 luoyusoft/luoyublog-backend:latest