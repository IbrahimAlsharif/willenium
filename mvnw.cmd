@ECHO OFF
SETLOCAL

set WRAPPER_DIR=%~dp0.mvn\wrapper
set WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
set WRAPPER_PROPERTIES=%WRAPPER_DIR%\maven-wrapper.properties

if not exist "%WRAPPER_JAR%" (
  echo Downloading Maven wrapper jar...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $p='%WRAPPER_PROPERTIES%'; $u=(Get-Content $p | Where-Object { $_ -match '^wrapperUrl=' } | Select-Object -First 1).Split('=')[1]; New-Item -ItemType Directory -Force -Path '%WRAPPER_DIR%' | Out-Null; Invoke-WebRequest -UseBasicParsing -Uri $u -OutFile '%WRAPPER_JAR%'"
  if errorlevel 1 (
    echo Failed to download Maven wrapper jar.
    exit /b 1
  )
)

if "%JAVA_HOME%"=="" (
  set JAVA_EXE=java
) else (
  set JAVA_EXE=%JAVA_HOME%\bin\java
)

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%~dp0." -cp "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
