@rem============================================================================
@rem This script is only meant to be used in the development environment.
@rem After setting appropriate environment variables, this script calls
@rem ./scripts/startup.cmd, which is the distribution startup script
@rem============================================================================

@echo off
rem TODO: use libs from build directory
rem TODO: create bootstrap.jar and remove /web-inf/classes

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04
set JMANAGE_PORT=9090
set JMANAGE_HOME=.\build
set JMANAGE_LIB_ROOT=.

set LIB_JARS=./lib/javax.servlet.jar;./lib/org.mortbay.jetty.jar;./lib/jasper-compiler.jar;./lib/jasper-runtime.jar;./lib/ant.jar;./lib/jdom.jar
rem set EXT_JARS=./ext/weblogic.jar

@rem debug
set DEBUG_OPTIONS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_shmem,server=y,suspend=n,address=jmanagedebug

call ./scripts/startup.cmd

