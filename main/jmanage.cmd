@rem============================================================================
@rem This script is only meant to be used in the development environment.
@rem After setting appropriate environment variables, this script calls
@rem ./scripts/jmanage.cmd, which is the distribution CLI script
@rem============================================================================

@echo off
rem TODO: use libs from build directory

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04
set JMANAGE_PORT=9090
set JMANAGE_HOME=.\build
set JMANAGE_LIB_ROOT=.

call ./scripts/jmanage.cmd