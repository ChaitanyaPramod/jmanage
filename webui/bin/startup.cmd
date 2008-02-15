@rem===========================================================================
@rem  Copyright 2004-2005 jManage.org. All rights reserved.
@rem===========================================================================
@echo off

call setenv.cmd
if "%JMANAGE_CLASSPATH%" == "" goto finish

"%JAVA_HOME%/bin/java" -ea -classpath "%JMANAGE_CLASSPATH%" %DEBUG_OPTIONS% -DJMANAGE_ROOT="%JMANAGE_HOME%" -Djava.security.policy=java.policy -Djava.util.logging.config.file="%JMANAGE_HOME%/config/logging.properties" org.jmanage.webui.Startup %*

:finish
