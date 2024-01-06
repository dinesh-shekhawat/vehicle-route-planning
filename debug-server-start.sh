#/bin/bash

# Default values
LOG_CONF_FILE=config-log-files/log4j2.xml

#HIBERNATE_HBM2DDL_AUTO=update
HIBERNATE_HBM2DDL_AUTO=validate

# Remote Debugging Port
DEBUG_PORT=5005

# Display variable values
echo "LOG_CONF_FILE: $LOG_CONF_FILE"
echo "HIBERNATE_HBM2DDL_AUTO: $HIBERNATE_HBM2DDL_AUTO"
echo "DEBUG_PORT: $DEBUG_PORT"

mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT -Dlogging.config=$LOG_CONF_FILE -Dhibernate.hbm2ddlAuto=$HIBERNATE_HBM2DDL_AUTO"
