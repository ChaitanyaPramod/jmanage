@rem===========================================================================
@rem  Copyright 2004-2005 jManage.org
@rem
@rem  Licensed under the Apache License, Version 2.0 (the "License");
@rem  you may not use this file except in compliance with the License.
@rem  You may obtain a copy of the License at
@rem
@rem      http://www.apache.org/licenses/LICENSE-2.0
@rem
@rem  Unless required by applicable law or agreed to in writing, software
@rem  distributed under the License is distributed on an "AS IS" BASIS,
@rem  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem  See the License for the specific language governing permissions and
@rem  limitations under the License.
@rem===========================================================================
@echo off

if "%JAVA_HOME%" == "" goto javaHomeNotSet

if "%JMANAGE_HOME%" == "" goto jmanageHomeNotSet

echo JMANAGE_HOME is %JMANAGE_HOME%

if "%JMANAGE_LIB_ROOT%" == "" set JMANAGE_LIB_ROOT=%JMANAGE_HOME%

set LIB_JARS=%JMANAGE_LIB_ROOT%/lib/javax.servlet.jar;%JMANAGE_LIB_ROOT%/lib/org.mortbay.jetty.jar;%JMANAGE_LIB_ROOT%/lib/jasper-compiler.jar;%JMANAGE_LIB_ROOT%/lib/jasper-runtime.jar;%JMANAGE_LIB_ROOT%/lib/ant.jar;%JMANAGE_LIB_ROOT%/lib/jdom.jar;%JMANAGE_LIB_ROOT%/lib/xml-apis.jar;%JMANAGE_LIB_ROOT%/lib/xmlrpc-1.2-b1.jar;%JMANAGE_LIB_ROOT%/lib/castor.jar;%JMANAGE_LIB_ROOT%/lib/commons-beanutils.jar;%JMANAGE_LIB_ROOT%/lib/commons-logging.jar
set EXT_JARS=

set JMANAGE_CLASSPATH=%LIB_JARS%;%EXT   _JARS%;%JMANAGE_HOME%/classes

@echo on

%JAVA_HOME%/bin/java -ea -classpath %JMANAGE_CLASSPATH% %DEBUG_OPTIONS% -Djava.util.logging.config.file=%JMANAGE_HOME%/config/logging.properties -Djmanage.root=%JMANAGE_HOME% org.jmanage.webui.Startup
goto finish

:javaHomeNotSet
echo Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory.
goto finish

:jmanageHomeNotSet
echo Please set JMANAGE_HOME environment variable pointing to jManage installation directory.
goto finish

:finish
