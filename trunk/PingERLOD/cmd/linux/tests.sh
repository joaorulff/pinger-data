#!/bin/sh
CURR_DIR=`dirname $0`
INCLUDE="$CURR_DIR/../../setcp.sh"
. $INCLUDE

#Here is the place to define the specific options to run the program
PINGER_ARGS="loadTSVFilesIntoRepository=1,downloadTSVFiles=1,"

echo $PINGERLOD_BIN:$PINGERLOD_CP $PINGERLOD_MAINCOMPILEDCLASS $PINGER_ARGS "metrics=[packet_loss],ticks=[last365days]"