#!/bin/sh
CURR_DIR=`dirname $0`
INCLUDE="$CURR_DIR/setcp.sh"
. $INCLUDE

echo "Project_Home: $PINGERLOD_PROJECT_HOME"
echo "Classpath: $PINGERLOD_CP"

#This should run only once a year
#Here is the place to define the specific options to run the program
PINGER_ARGS="measurementParameters=1,timestamp=1"

java -cp $PINGERLOD_BIN:$PINGERLOD_CP $PINGERLOD_MAINCOMPILEDCLASS $PINGER_ARGS