# SwapCampus

Kotlin / Compose Multiplatform campus marketplace. Android and iOS share the full UI,
ViewModels, typed navigation, repositories and English/French resources.

## Structure

```text
androidApp/                       Android launcher (AppCompatActivity for app locales)
composeApp/src/commonMain/         App(), all screens/components/ViewModels/routes/resources
composeApp/src/androidMain/        LanguageSelector, formatters and dynamic-theme actuals
composeApp/src/iosMain/            Language hint, Foundation formatters, theme and controller
shared/src/commonMain/            Existing French domain and in-memory repositories
iosApp/iosApp.xcodeproj/           Shared Xcode scheme iosApp
iosApp/iosApp/                     SwiftUI host and English/French bundle localization
scripts/                          Cloud SDK setup and iPhone simulator build/startup check
```

Dependency direction: `androidApp -> composeApp -> shared`; the iOS host embeds the
`ComposeApp` framework. Both launchers call common `App()`.

## Android (Windows)

Open the repository in Android Studio, sync, select **androidApp** and run.
Use JDK 17 (the Gradle daemon toolchain is pinned to 17), SDK API 37 and Build Tools 36.0.0.

```powershell
.\gradlew.bat :androidApp:assembleDebug :androidApp:lintDebug
.\gradlew.bat :composeApp:compileCommonMainKotlinMetadata :composeApp:compileIosMainKotlinMetadata
```

Metadata compilation checks shared/platform Kotlin declarations; it is not an iOS
framework link or an Xcode simulator build. Those require macOS.

## iOS (Apple Silicon Mac or Codemagic)

Open `iosApp/iosApp.xcodeproj`, select the **iosApp** scheme and an iPhone simulator.
Set `JAVA_HOME` to a JDK 17 installation and `ANDROID_HOME` to the Android SDK if needed.
The first Xcode build invokes Gradle to build/embed the framework and its Compose resources.
The iOS deployment target is 16.0. Simulator builds do not need a signing team.

For automated build, install and startup validation on a Mac:

```bash
bash scripts/prepare-android-sdk.sh
bash scripts/ios-simulator-smoke.sh
```

`codemagic.yaml` retains workflow ID **ios-framework**, but now builds the complete
simulator app, launches it, checks that it remains running, and exports a screenshot,
logs and `SwapCampus-simulator.zip` under `iosApp/build/artifacts/`.
For interactive checks, manually enable SSH/VNC when starting a Codemagic build;
the script opens Simulator on the selected iPhone.

`.github/workflows/build.yml` also defines Android and Apple Silicon macOS jobs.
Check the branch’s GitHub Actions runs for current cloud build status.

## Features and demo data

Browse/search/filter; listing details; login/logout; post/edit; owner-only deletion;
reserve/cancel/sell/reopen; My Listings; profile; English/French prices and dates;
localized success snackbars; system dark mode; top/back navigation and + action.

Android offers **Follow system / English / Français** in Profile. iOS shows a hint to
change language in iPhone Settings. The bundle declares English and French.

| Account | Email | Password |
| --- | --- | --- |
| Ada | ada@isep.fr | ada123 |
| Student 1 | student1@isep.fr | student123 |
| Student 2 | student2@isep.fr | student123 |
| Administrator | admin@isep.fr | admin123 |

These are local mock accounts. Data lives in memory and resets after process restart.
Photo URLs use shared Coil UI; Android keeps OkHttp, iOS uses Ktor/Darwin.
Firebase, persistent storage, messaging and moderation UI are outside this lab.

## Verification and workshop notes

- [Android runtime acceptance](docs/android-validation.md)
- [Migration inventory, platform comparison and iOS checklist](docs/multiplatform-migration.md)

Local test folders remain ignored, following the existing repository convention.
Migration changes are published on the dedicated `codex/cmp-migration` branch.
