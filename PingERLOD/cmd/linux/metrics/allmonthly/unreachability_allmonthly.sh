#!/bin/sh
CURR_DIR=`dirname $0`
INCLUDE="$CURR_DIR/../../setcp.sh"
. $INCLUDE

echo "Project_Home: $PINGERLOD_PROJECT_HOME"
echo "Classpath: $PINGERLOD_CP"

#Here is the place to define the specific options to run the program
PINGER_ARGS="loadTSVFilesIntoRepository=1,downloadTSVFiles=1,metrics=[unreachability],ticks=[allmonthly]"

java -Xms1g -Xmx1g -cp $PINGERLOD_BIN:$PINGERLOD_CP $PINGERLOD_MAINCOMPILEDCLASS $PINGER_ARGS