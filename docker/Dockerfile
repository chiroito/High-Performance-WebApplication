FROM store/oracle/weblogic:12.2.1.3

MAINTAINER Chihiro Ito

ENV USER_MEM_ARGS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
EXPOSE 7001

COPY container-scripts/*  ${ORACLE_HOME}/

USER root
RUN chown oracle:oracle ${ORACLE_HOME}/* && \
    chmod +x ${ORACLE_HOME}/*.sh

USER oracle
RUN ${ORACLE_HOME}/create-domain.sh

USER root
COPY *.jar ${DOMAIN_HOME}/lib/
RUN chown oracle:oracle ${DOMAIN_HOME}/lib/*.jar

USER oracle
ARG INSTANCE_TYPE
RUN wlst.sh -skipWLSModuleScanning -loadProperties ${ORACLE_HOME}/${INSTANCE_TYPE}.properties -loadProperties ${ORACLE_HOME}/datasource.properties ${ORACLE_HOME}/deploy-datasource.py && \
    wlst.sh -skipWLSModuleScanning -loadProperties ${ORACLE_HOME}/${INSTANCE_TYPE}.properties ${ORACLE_HOME}/deploy-coherence-cluster.py && \
    wlst.sh -skipWLSModuleScanning -loadProperties ${ORACLE_HOME}/${INSTANCE_TYPE}.properties ${ORACLE_HOME}/config-weblogic.py && \
    wlst.sh -skipWLSModuleScanning -loadProperties ${ORACLE_HOME}/${INSTANCE_TYPE}.properties ${ORACLE_HOME}/deploy-app.py

WORKDIR $DOMAIN_HOME
CMD ["./startWebLogic.sh"]
