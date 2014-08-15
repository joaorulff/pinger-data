#!/bin/sh
CURR_DIR=`dirname $0`
INCLUDE="$CURR_DIR/setcp.sh"
. $INCLUDE

echo "Project_Home: $PINGERLOD_PROJECT_HOME"
echo "Classpath: $PINGERLOD_CP"

#This should run ~ twice a day
#Here is the place to define the specific options to run the program
PINGER_ARGS="generateMonitoring=1,generateGrouped=1,monitoringMonitoredGrouped=1,generateMonitoringGroupedForTSV=1,generateMonitoringMonitored=0"

java -cp $PINGERLOD_BIN:$PINGERLOD_CP $PINGERLOD_MAINCOMPILEDCLASS $PINGER_ARGS