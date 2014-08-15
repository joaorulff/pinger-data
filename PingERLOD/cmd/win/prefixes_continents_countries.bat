#!/bin/sh
CURR_DIR=`dirname $0`
INCLUDE="$CURR_DIR/setcp.sh"
. $INCLUDE

echo "Project_Home: $PINGERLOD_PROJECT_HOME"
echo "Classpath: $PINGERLOD_CP"

#Set up the prefixes, generating the RDF file for them
#Instantiate all the continents
#Instantiate all the countries, downloading the JSON file from Geonames and setting DBPedia

#Here is the place to define the specific options to run the program
PINGER_ARGS="setupprefixes=1,generate=1 continents=1 countries=1,file=0,setdbpedia=1"

java -cp $PINGERLOD_BIN:$PINGERLOD_CP $PINGERLOD_MAINCOMPILEDCLASS $PINGER_ARGS