@echo off

if "%JAVA_HOME%" == "" goto javaHomeNotSet

if "%JMANAGE_HOME%" == "" goto jmanageHomeNotSet

echo JMANAGE_HOME is %JMANAGE_HOME%

if "%JMANAGE_LIB_ROOT%" == "" set JMANAGE_LIB_ROOT=%JMANAGE_HOME%

set LIB_JARS=%JMANAGE_LIB_ROOT%/lib/javax.servlet.jar;%JMANAGE_LIB_ROOT%/lib/org.mortbay.jetty.jar;%JMANAGE_LIB_ROOT%/lib/jasper-compiler.jar;%JMANAGE_LIB_ROOT%/lib/jasper-runtime.jar;%JMANAGE_LIB_ROOT%/lib/ant.jar;%JMANAGE_LIB_ROOT%/lib/jdom.jar;%JMANAGE_LIB_ROOT%/lib/xml-apis.jar;%JMANAGE_LIB_ROOT%/lib/xmlrpc-1.2-b1.jar;%JMANAGE_LIB_ROOT%/lib/castor.jar
set EXT_JARS=

set JMANAGE_CLASSPATH=%LIB_JARS%;%EXT_JARS%;%JMANAGE_HOME%/classes

@echo on
%JAVA_HOME%/bin/java -ea -classpath %JMANAGE_CLASSPATH% -Djmanage.root=%JMANAGE_HOME% org.jmanage.cmdui.CommandInterface
goto finish

:javaHomeNotSet
echo Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory.
goto finish

:jmanageHomeNotSet
echo Please set JMANAGE_HOME environment variable pointing to jManage installation directory.
goto finish

:finish
