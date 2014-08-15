#!/bin/sh
CURR_DIR=`dirname $0`
INCLUDE="$CURR_DIR/setcp.sh"
. $INCLUDE

echo "Project_Home: $PINGERLOD_PROJECT_HOME"
echo "Classpath: $PINGERLOD_CP"
javac -d $PINGERLOD_BIN -sourcepath $PINGERLOD_SRC -cp $PINGERLOD_CP $PINGERLOD_MAINCLASS