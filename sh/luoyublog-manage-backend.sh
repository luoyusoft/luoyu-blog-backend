#!/bin/bash
curl http://luoyublog.com/api/luoyublog/manage/actuator/shutdown -X POST
sleep 20
docker rm luoyublog-manage-backend
docker rmi luoyublog-manage-backend:latest
docker run -dit --name luoyublog-manage-backend -v /usr/local/docker/luoyublog-manage-backend/log:/usr/local/project/luoyublog-manage-backend/log -v /usr/local/docker/luoyublog-manage-backend/log/debug:/usr/local/project/luoyublog-manage-backend/log/debug -v /usr/local/docker/luoyublog-manage-backend/log/info:/usr/local/project/luoyublog-manage-backend/log/info -v /usr/local/docker/luoyublog-manage-backend/log/warn:/usr/local/project/luoyublog-manage-backend/log/warn -v /usr/local/docker/luoyublog-manage-backend/log/error:/usr/local/project/luoyublog-manage-backend/log/error -p 8801:8801 luoyusoft/luoyublog-manage-backend:latest