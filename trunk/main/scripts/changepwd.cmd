@echo off

if "%JAVA_HOME%" == "" goto javaHomeNotSet

if "%JMANAGE_HOME%" == "" goto jmanageHomeNotSet

if "%JMANAGE_LIB_ROOT%" == "" set JMANAGE_LIB_ROOT=%JMANAGE_HOME%

set JMANAGE_CLASSPATH=%JMANAGE_HOME%/classes;%JMANAGE_LIB_ROOT%/lib/jdom.jar

java -classpath %JMANAGE_CLASSPATH% -Djmanage.root=%JMANAGE_HOME% org.jmanage.core.tools.ChangeAdminPassword
goto finish

:javaHomeNotSet
echo Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory.
goto finish

:jmanageHomeNotSet
echo Please set JMANAGE_HOME environment variable pointing to jManage installation directory.
goto finish

:finish
