#!/usr/bin/env sh
set -eu
if ! command -v java >/dev/null 2>&1; then
  echo 'JDK 21 is required.' >&2
  exit 1
fi
if ! command -v gradle >/dev/null 2>&1; then
  echo 'Gradle 9.2.1+ was not found. Install Gradle 9.2.1+ or use the included GitHub Actions workflow.' >&2
  exit 1
fi
gradle --version
gradle build --no-daemon
echo 'Build complete. Mod JAR: build/libs/potatoclient-1.0.0.jar'
