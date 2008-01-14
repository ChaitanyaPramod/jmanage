@rem============================================================================
@rem This script is only meant to be used in the development environment.
@rem After setting appropriate environment variables, this script calls
@rem ./build/bin/startup.cmd, which is the distribution startup script
@rem============================================================================

@echo off

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04

@rem debug
set DEBUG_OPTIONS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n

cd build\bin

call startup.cmd %*

cd ..\..