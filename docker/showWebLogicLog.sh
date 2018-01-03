#!/bin/bash
target=$1

docker exec ${target} tail -300 /u01/oracle/user_projects/domains/base_domain/servers/AdminServer/logs/AdminServer.log
