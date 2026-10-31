@echo off
chcp 65001 >nul
setlocal
cd /d "%~dp0"

rem If JAVA_HOME is not visible yet (e.g. VS Code started before it was set),
rem fall back to a JDK under %USERPROFILE%\.jdks
if not defined JAVA_HOME for /d %%d in ("%USERPROFILE%\.jdks\*") do if exist "%%d\bin\javac.exe" set "JAVA_HOME=%%d"

rem Put JAVA_HOME\bin first on PATH, so javac is found even if PATH is stale.
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"

if not exist out mkdir out

javac -encoding UTF-8 -d out src\tasklist\*.java
if errorlevel 1 (
    echo.
    echo [ERROR] Compile failed. Check the code under src\tasklist
    exit /b 1
)

java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstdin.encoding=UTF-8 -cp out tasklist.Main %*
