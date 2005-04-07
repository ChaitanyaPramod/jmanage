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

set JMANAGE_CLASSPATH=%JMANAGE_HOME%/classes;%JMANAGE_LIB_ROOT%/lib/jdom.jar

%JAVA_HOME%\bin\java -classpath %JMANAGE_CLASSPATH% -Djmanage.root=%JMANAGE_HOME% org.jmanage.core.tools.EncryptedKeyGenerator

goto finish

:javaHomeNotSet
echo Please set JAVA_HOME environment variable. JAVA_HOME must point to a JDK 1.4 installation directory.
goto finish

:jmanageHomeNotSet
echo Please set JMANAGE_HOME environment variable pointing to jManage installation directory.
goto finish

:finish
