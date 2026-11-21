@echo off
where java >nul 2>nul
if errorlevel 1 (
  echo JDK 21 is required.
  pause
  exit /b 1
)
where gradle >nul 2>nul
if errorlevel 1 (
  echo Gradle 9.2.1+ was not found.
  echo Install Gradle 9.2.1+ or use the included GitHub Actions workflow.
  pause
  exit /b 1
)
gradle --version
gradle build --no-daemon
if errorlevel 1 exit /b %errorlevel%
echo Build complete. Mod JAR: build\libs\potatoclient-1.0.0.jar
pause
