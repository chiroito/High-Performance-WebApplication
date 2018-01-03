#!/bin/bash
. setEnv.sh

# Database
docker rm postgres4webapp
#docker rmi `docker images --format "{{.ID}} {{.Repository}}" | grep postgres | awk '{print $1}'`

# In-memory Data Grid
docker rm coherence1
docker rmi `docker images --format "{{.ID}} {{.Repository}}" | grep high-performance-webapp-coherence | awk '{print $1}'`

# Application Server
for target in `seq 1 ${ap_container_num}`; do
docker rm weblogic${target}
done
docker rmi `docker images --format "{{.ID}} {{.Repository}}" | grep high-performance-webapp-weblogic | awk '{print $1}'`

docker network rm webapp-net
