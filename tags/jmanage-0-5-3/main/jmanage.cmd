@rem============================================================================
@rem This script is only meant to be used in the development environment.
@rem After setting appropriate environment variables, this script calls
@rem ./scripts/jmanage.cmd, which is the distribution CLI script
@rem============================================================================

@echo off

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04
set JMANAGE_HOME=.\build
set JMANAGE_LIB_ROOT=.

@rem debug
set DEBUG_OPTIONS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_shmem,server=y,suspend=n,address=jmanageclidebug

call ./scripts/jmanage.cmd %*