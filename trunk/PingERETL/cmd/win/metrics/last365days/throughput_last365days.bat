CALL "../../setcp.bat"
SETLOCAL
SET "PINGER_ARGS=debug=1 loadTSVFilesIntoRepository=1,downloadTSVFiles=1,metrics=[packet_loss],ticks=[last365days]"
java -cp %PINGERLOD_BIN%;%PINGERLOD_CP% %PINGERLOD_MAINCOMPILEDCLASS% %PINGER_ARGS%
ENDLOCAL