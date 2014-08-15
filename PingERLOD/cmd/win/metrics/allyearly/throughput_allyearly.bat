CALL "../../setcp.bat"
SETLOCAL
SET "PINGER_ARGS=debug=0 loadTSVFilesIntoRepository=1,downloadTSVFiles=0,metrics=[throughput],ticks=[allyearly]"
java -cp %PINGERLOD_BIN%;%PINGERLOD_CP% %PINGERLOD_MAINCOMPILEDCLASS% %PINGER_ARGS%
ENDLOCAL