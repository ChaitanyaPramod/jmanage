@rem===========================================================================
@rem  Copyright 2004-2005 jManage.org. All rights reserved.
@rem===========================================================================
@echo off

call setenv.cmd
if "%JMANAGE_CLASSPATH%" == "" goto finish

set JSR160_JARS=%JMANAGE_HOME%/modules/jsr160/jmxri-1_2_1.jar;%JMANAGE_HOME%/modules/jsr160/jmxremote-1_0_1.jar;%JMANAGE_HOME%/modules/jsr160/jmxremote_optional.jar

%JAVA_HOME%/bin/java -ea -classpath %JMANAGE_CLASSPATH%;%JSR160_JARS% org.jmanage.testapp.jsr160.Startup %*

:finish