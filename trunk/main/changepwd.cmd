@rem============================================================================
@rem This script is only meant to be used in the development environment.
@rem After setting appropriate environment variables, this script calls
@rem ./scripts/changepwd.cmd, which is the distribution keygen script
@rem============================================================================

@echo off

if "%JAVA_HOME%" == "" set JAVA_HOME=:\j2sdk1.4.2_04
set JMANAGE_HOME=.\build
set JMANAGE_LIB_ROOT=.

call ./scripts/changepwd.cmd
