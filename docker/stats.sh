#!/bin/bash
. setEnv.sh

# Database and In-memory Data Grid
stats_target="postgres4webapp coherence1"

# Application Server
for target in `seq 1 ${ap_container_num}`; do
stats_target="${stats_target} weblogic${target}"
done

docker stats ${stats_target} | tee docker_stats.log
