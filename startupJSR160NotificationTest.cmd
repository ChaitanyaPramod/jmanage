@rem============================================================================
@rem This script is used to start JSR160 application for testing.
@rem============================================================================

@echo off
rem TODO: use libs from build directory
rem TODO: create bootstrap.jar and remove /web-inf/classes

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04
set JMANAGE_PORT=9090
set JMANAGE_HOME=.\build
set JMANAGE_LIB_ROOT=.

set JSR160_JARS=./ext/jmxri-1_2_1.jar;./ext/jmxremote-1_0_1.jar

set LIB_JARS=./lib/javax.servlet.jar;./lib/org.mortbay.jetty.jar;./lib/jasper-compiler.jar;./lib/jasper-runtime.jar;./lib/ant.jar;./lib/jdom.jar

set JMANAGE_CLASSPATH=%JMANAGE_HOME%/testapp/classes;%JMANAGE_HOME%/classes;./ext/;%JSR160_JARS%

%JAVA_HOME%/bin/java -ea -classpath %JMANAGE_CLASSPATH% org.jmanage.testapp.jsr160.NotificationListenerTest %1



