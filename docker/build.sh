#!/bin/bash

docker build --build-arg INSTANCE_TYPE=weblogic -t high-performance-webapp-weblogic .
docker build --build-arg INSTANCE_TYPE=coherence -t high-performance-webapp-coherence .
