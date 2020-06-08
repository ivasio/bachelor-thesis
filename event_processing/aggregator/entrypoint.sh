#!/bin/bash

CONF_FILE="${FLINK_HOME}/conf/flink-conf.yaml"

# setting jobmanager.rpc.address
if grep -E "^jobmanager\.rpc\.address:.*" "${CONF_FILE}" > /dev/null; then
    sed -i -e "s/jobmanager\.rpc\.address:.*/jobmanager.rpc.address: ${JOB_MANAGER_RPC_ADDRESS}/g" "${CONF_FILE}"
else
    echo "jobmanager.rpc.address: ${JOB_MANAGER_RPC_ADDRESS}" >> "${CONF_FILE}"
fi

# setting rest.address
if grep -E "^rest\.address:.*" "${CONF_FILE}" > /dev/null; then
    sed -i -e "s/rest\.address:.*/rest.address: ${JOB_MANAGER_RPC_ADDRESS}/g" "${CONF_FILE}"
else
    echo "rest.address: ${JOB_MANAGER_RPC_ADDRESS}" >> "${CONF_FILE}"
fi

sleep 10
flink run ./aggregator-*.jar