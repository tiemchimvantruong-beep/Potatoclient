# Potato Client — Fabric 1.21.11

A client-side Fabric mod foundation with a ClickGUI, module registry, and JSON configuration.

## Target versions

- Minecraft Java: 1.21.11
- Java: 21
- Fabric Loader: 0.18.1+
- Fabric API: 0.141.1+1.21.11
- Fabric Loom: 1.14-SNAPSHOT
- Gradle: 9.2.1+ (Loom 1.14 requires Gradle 9.2 or newer)

## Build locally

1. Install a JDK 21 distribution.
2. Install Gradle 9.2.1 or newer.
3. Open a terminal in the project folder.
4. Run `gradle build --no-daemon` (or run `BUILD-WINDOWS.bat` on Windows / `./BUILD-LINUX-MAC.sh` on Linux or macOS).
5. The mod JAR is generated at `build/libs/potatoclient-1.0.0.jar`.

## Build automatically with GitHub Actions

The workflow at `.github/workflows/build.yml` builds the mod on every push to `main`/`master`, on pull requests, and when manually started.

To use it, put this project in a GitHub repository and push it. Open the repository's **Actions** tab, select **Build Potato Client**, and wait for the run to finish. Under the completed run, download the artifact named `PotatoClient-Fabric-1.21.11`. It contains `potatoclient-1.0.0.jar`.

## Install

Copy `potatoclient-1.0.0.jar` into `.minecraft/mods/`. The matching Fabric Loader and Fabric API must also be installed for Minecraft 1.21.11.

## Current feature scope

Implemented: Right Shift opens the ClickGUI, category tabs, module search, module toggles, draggable panel, accent color config value, and automatic JSON config persistence.

The names in the module registry (ESP, OreSim, combat-related toggles, Freecam, etc.) are currently UI placeholders, not implemented gameplay features. No anti-cheat bypass, packet spoofing, or server-evasion code is included.
