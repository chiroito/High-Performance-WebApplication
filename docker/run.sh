#!/bin/bash
. setEnv.sh

docker network create --attachable webapp-net
docker network ls

# Database
docker run -d --name postgres4webapp --hostname postgres4webapp -p 5432:5432 --net=webapp-net --cpus ${ds_cpu_num} -m ${ds_mem_size} postgres

# In-memory Data Grid
docker run -d --name coherence1 --hostname coherence1 -p 8001:7001 --net=webapp-net --cpus ${ds_cpu_num} -m ${ds_mem_size} high-performance-webapp-coherence:latest

# Application Server
for target in `seq 1 ${ap_container_num}`; do
docker run -d --name weblogic${target} --hostname weblogic${target} -p 700${target}:7001 --net=webapp-net --cpus ${ap_cpu_num} -m ${ap_mem_size} high-performance-webapp-weblogic:latest
done

docker ps
