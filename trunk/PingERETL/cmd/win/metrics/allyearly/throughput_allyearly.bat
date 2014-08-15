CALL "../../setcp.bat"
SETLOCAL
SET "PINGER_ARGS=debug=0 downlaodCSVFiles=1,downloadedCSVDirectory=c:/downloadedCSV/,monitorNodes=[pinger.slac.stanford.edu],metrics=[throughput],ticks=[allyearly]"
java -cp %PINGERLOD_BIN%;%PINGERLOD_CP% %PINGERLOD_MAINCOMPILEDCLASS% %PINGER_ARGS%
ENDLOCAL