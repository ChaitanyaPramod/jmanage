@echo off
rem TODO: use libs from build directory
rem TODO: create bootstrap.jar and remove /web-inf/classes

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04
set JMANAGE_PORT=9090
set JMANAGE_ROOT=./build


set LIB_JARS=./lib/javax.servlet.jar;./lib/org.mortbay.jetty.jar;./lib/jasper-compiler.jar;./lib/jasper-runtime.jar;./lib/ant.jar;./lib/jdom.jar
set EXT_JARS=./ext/weblogic61.jar

set CLASSPATH=%LIB_JARS%;%EXT_JARS%;./build/WEB-INF/classes

@echo on
%JAVA_HOME%/bin/java -classpath %CLASSPATH% -Djmanage.port=%JMANAGE_PORT% -Djmanage.root=%JMANAGE_ROOT% org.jmanage.webui.Startup


