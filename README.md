# SwapCampus_ZhengLi

Android baseline for **TP1 CRUD**, reusing the French domain from **TD2 Advanced Kotlin**.
This is the working Android starting point for the Compose Multiplatform tutorial (Step 0).

## Project structure

```text
androidApp/                       Android launcher only
  src/main/kotlin/.../            MainActivity, SwapCampusApplication
  src/main/res/                   Launcher icons, Android window theme
composeApp/                       TP1 UI module (Android target at this stage)
  src/androidMain/kotlin/.../ui/
    AppContainer.kt               Repository construction and demo data
    SwapCampusApp.kt              NavHost and bottom navigation
    viewmodel/                    AuthViewModel, ListingsViewModel, UiState
    screens/                      Login, profile, listings, form, detail, my listings
    components/                   Reusable listing card and photo
    navigation/                   Route names and integer arguments
    i18n/                         Android login error labels
    theme/                        SwapCampusTheme
  src/androidMain/res/            English/French login resources, photo placeholder
shared/
  src/commonMain/kotlin/.../      Existing TD2 French domain and date helper
    repository/                   Repository interfaces, in-memory implementations, demo data
  src/androidMain/               Android platform implementation
  src/iosMain/                   Existing iOS platform implementation
```

Dependencies: `androidApp -> composeApp -> shared`. ViewModels depend on repository
interfaces, never on `CatalogueAnnonces`. TD2 entities (`Annonce`, `CompteUtilisateur`,
`EtatAnnonce`, `Message`, moderation classes, enums and catalog) retain their French
names and existing data shape. `compteUtilisateur.kt` is renamed `CompteUtilisateur.kt`
to match the Kotlin class name.

The former `app` module is no longer included by Gradle. Its ignored local test/cache
folders may still exist on this machine. No source code should be added there.

## Build and run (Windows)

Toolchain: JDK 17, Kotlin/Compose Compiler 2.4.21, AGP 9.2.1, Gradle 9.4.1,
compile/target SDK 37, minimum SDK 24. Android UI uses Compose BOM 2026.09.00.
Compose Multiplatform 1.12.2 remains declared for the later UI migration.

```powershell
.\gradlew.bat --version
.\gradlew.bat :androidApp:assembleDebug
.\gradlew.bat :androidApp:lintDebug
```

Use Android Studio **2025.3.4 or newer** for AGP 9.2. IDEA can also invoke the Wrapper
from its terminal; its Android IDE integration depends on compatible plugins.
Sync/reload Gradle after this module reorganization and select **androidApp** as the
Android run configuration. JDK 17 is pinned by `gradle/gradle-daemon-jvm.properties`;
the local `.gradle/config.properties` points to the installed JDK 17 as well.

APK: `androidApp/build/outputs/apk/debug/androidApp-debug.apk`.

## Step 0 acceptance

1. Launch the app on an Android device/emulator (API 24 or newer).
2. Browse the seeded listings as a guest. Search keywords and choose category chips.
3. Open **Profile -> Log in**.
4. Empty fields and an incorrect password display explicit errors.
5. Log in using **ada@isep.fr / ada123**; the profile shows **Welcome, Ada!**.
6. Log out and confirm that posting/editing requires authentication.
7. Log back in; verify post, owner-only edit/delete, and the reservation/sale/cancel flow.

Other existing accounts remain available:

| Role | Email | Password |
|---|---|---|
| Student | student1@isep.fr | student123 |
| Student | student2@isep.fr | student123 |
| Administrator | admin@isep.fr | admin123 |

These are mock plaintext credentials as explicitly required by TP1. Data is kept in
memory and resets when the Android process restarts. Firebase, persistent storage,
messaging UI and moderation UI are later work, outside this baseline.

Business validation (nonblank title, positive finite price) is in `shared`.
Authentication and ownership checks also run in the listings ViewModel. Successful
mutations refresh all listings while retaining browse filters. My Listings and detail
use the full collection, so an active browse filter cannot hide owned listings or details.

## Local tests

The user requested that test sources remain local and untracked. The test directories
are ignored in `.gitignore`, including those in the new modules. To run local tests:

```powershell
.\gradlew.bat :composeApp:testAndroidHostTest :shared:testAndroidHostTest
```

A fresh checkout will not contain these local-only tests. Build outputs are ignored too.
No Git commit or push is performed by this change.

## Later iOS migration

This baseline intentionally keeps UI in `composeApp/src/androidMain`, as TP1 requests.
The CMP tutorial later moves login, its ViewModel, container, theme and texts to
`commonMain`, adds the iOS UI target/entry point, and creates `iosApp`. Only `shared`
currently declares iOS targets. iOS compilation/simulator verification needs a cloud Mac
(e.g. Codemagic or a suitable GitHub Actions runner). These cloud workflows are not
configured by this baseline change.
