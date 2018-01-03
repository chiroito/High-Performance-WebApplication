#!/bin/bash
. setEnv.sh

# Application Server
for target in `seq 1 ${ap_container_num}`; do
docker stop weblogic${target}
done

# In-memory Data Grid
docker stop coherence1

# Database
docker stop postgres4webapp

docker ps
