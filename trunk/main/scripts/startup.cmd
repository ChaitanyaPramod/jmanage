@echo off

if "%JAVA_HOME%" == "" goto javaHomeNotSet

if "%JMANAGE_HOME%" == "" goto jmanageHomeNotSet

echo JMANAGE_HOME is %JMANAGE_HOME%

if "%JMANAGE_LIB_ROOT%" == "" set JMANAGE_LIB_ROOT=%JMANAGE_HOME%

if "%JMANAGE_PORT%" == "" set JMANAGE_PORT=9090

set LIB_JARS=%JMANAGE_LIB_ROOT%/lib/javax.servlet.jar;%JMANAGE_LIB_ROOT%/lib/org.mortbay.jetty.jar;%JMANAGE_LIB_ROOT%/lib/jasper-compiler.jar;%JMANAGE_LIB_ROOT%/lib/jasper-runtime.jar;%JMANAGE_LIB_ROOT%/lib/ant.jar;%JMANAGE_LIB_ROOT%/lib/jdom.jar;%JMANAGE_LIB_ROOT%/lib/xml-apis.jar
set EXT_JARS=

set JMANAGE_CLASSPATH=%LIB_JARS%;%EXT_JARS%;%JMANAGE_HOME%/classes

@echo on

%JAVA_HOME%/bin/java -ea -classpath %JMANAGE_CLASSPATH% %DEBUG_OPTIONS% -Djmanage.port=%JMANAGE_PORT% -Djmanage.root=%JMANAGE_HOME% org.jmanage.webui.Startup
goto finish

:javaHomeNotSet
echo Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory.
goto finish

:jmanageHomeNotSet
echo Please set JMANAGE_HOME environment variable pointing to jManage installation directory.
goto finish

:finish
