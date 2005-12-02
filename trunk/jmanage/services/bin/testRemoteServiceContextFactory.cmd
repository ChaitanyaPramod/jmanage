@rem===========================================================================
@rem  Copyright 2004-2005 jManage.org. All rights reserved.
@rem===========================================================================
@echo off

set JMANAGE_CLASSPATH=../lib/client/jmanage-client.jar;../lib/jmxri-1_2_1.jar

"%JAVA_HOME%/bin/java" -ea -classpath %JMANAGE_CLASSPATH% %DEBUG_OPTIONS% org.jmanage.core.services.RemoteServiceContextFactory %*

:finish
