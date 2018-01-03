#!/bin/bash
export ADMIN_USERNAME="weblogic"
export ADMIN_PASSWORD="welcome1"

# Create an empty domain
sed -i -e "s|ADMIN_PASSWORD|${ADMIN_PASSWORD}|g" /u01/oracle/create-wls-domain.py
wlst.sh -skipWLSModuleScanning ${ORACLE_HOME}/create-wls-domain.py
mkdir -p ${DOMAIN_HOME}/servers/AdminServer/security/
echo "username=${ADMIN_USERNAME}" > ${DOMAIN_HOME}/servers/AdminServer/security/boot.properties
echo "password=${ADMIN_PASSWORD}" >> ${DOMAIN_HOME}/servers/AdminServer/security/boot.properties
