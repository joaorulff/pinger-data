#!/bin/sh
CURR_DIR=`dirname $0`
INCLUDE="$CURR_DIR/setcp.sh"
. $INCLUDE

echo "Project_Home: $PINGERLOD_PROJECT_HOME"
echo "Classpath: $PINGERLOD_CP"

#This Should Run ~ Twice a Day

#Generate NODE_DETAILS Json file
#Download the NodesCF


#Here is the place to define the specific options to run the program
PINGER_ARGS="generateNodeDetails=1,downloadNodesCF=1,all=0"

java -cp $PINGERLOD_BIN:$PINGERLOD_CP $PINGERLOD_MAINCOMPILEDCLASS $PINGER_ARGS