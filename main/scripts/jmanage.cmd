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

if "%JMANAGE_HOME%" == "" set JMANAGE_HOME=..
if not exist "%JMANAGE_HOME%\config\jmanage.properties" goto jmanageHomeNotSet

if "%JMANAGE_LIB_ROOT%" == "" set JMANAGE_LIB_ROOT=%JMANAGE_HOME%

set LIB_JARS=%JMANAGE_LIB_ROOT%/lib/javax.servlet.jar;%JMANAGE_LIB_ROOT%/lib/org.mortbay.jetty.jar;%JMANAGE_LIB_ROOT%/lib/jasper-compiler.jar;%JMANAGE_LIB_ROOT%/lib/jasper-runtime.jar;%JMANAGE_LIB_ROOT%/lib/ant.jar;%JMANAGE_LIB_ROOT%/lib/jdom.jar;%JMANAGE_LIB_ROOT%/lib/xml-apis.jar
set EXT_JARS=

set JMANAGE_CLASSPATH=%LIB_JARS%;%EXT_JARS%;%JMANAGE_HOME%/classes

%JAVA_HOME%/bin/java -ea -classpath %JMANAGE_CLASSPATH% %DEBUG_OPTIONS% -Djmanage.root=%JMANAGE_HOME% org.jmanage.cmdui.Main %*
goto finish

:javaHomeNotSet
echo Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory.
goto finish

:jmanageHomeNotSet
echo Please set JMANAGE_HOME environment variable pointing to jManage installation directory.
goto finish

:finish
