#!/bin/sh

# set work dir
OLD_PWD=`pwd`

cd $(dirname $0)

WORK_DIR=`pwd`

# parse out job name
JOB_NAME="$1"

if [ "X$JOB_NAME" = "X" ] ; then 
	echo "Error: job name not defined"
	echo "Usage $0 job_name [job_params ...]"	
	exit 2
fi
	
# determine environment from the current directory
export ENV=`pwd | cut -d / -f 4`

# define JAVA_HOME directory
if [ -z "$JAVA_HOME" ] ; then 
	JAVA_HOME="/srv/sasbin/$ENV/colman/batch/jdk"
fi	

# prepare the environment variables
. ./config_common/setenv_common.sh
. ./config_offline/setenv_offline.sh

## deleted 21200428 fujira RI-4395
##if [ "X$CONNECTION_STRING_SINGLE" != "X" ] ; then
##    if [ "$JOB_NAME" = "ClusterProcessing1" -o "$JOB_NAME" = "CopySnapshotToWorkSchemaExt" -o "$JOB_NAME" = "CopyOnlineSchemaToSnapshotExt" ] ; then
##	export DB_CONNECTION_STRING=$CONNECTION_STRING
##	else 
##	export DB_CONNECTION_STRING=$CONNECTION_STRING_SINGLE
##    fi
##fi
##echo "Used DB_CONNECTION_STRING: $DB_CONNECTION_STRING"


# run the job
echo "Running job $JOB_NAME on offline schema (environment $ENV)"
$JAVA_HOME/bin/java -Xmx10240m -XX:MaxPermSize=456m  -cp $WORK_DIR/addons:$WORK_DIR/addons/*.jar:$WORK_DIR/env_config:$WORK_DIR/config_common:$WORK_DIR/config_offline:$WORK_DIR/config_ifaces:$WORK_DIR/package/dependency-jars/*:$WORK_DIR/package/cscollateral-batch.jar -Dlogback.configurationFile="$WORK_DIR/config_common/logback.xml" -Dcolman.log.root="$COLMAN_LOG_ROOT" -Dcolman.epa.environment="$ENV" org.springframework.batch.core.launch.support.CommandLineJobRunner $JOB_NAME.xml $@ -forceParallelRun=true </dev/null

RETVAL=$?

cd $OLD_PWD

exit $RETVAL
