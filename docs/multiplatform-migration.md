# SwapCampus multiplatform migration

Date: 2026-11-04 (Asia/Taipei).

## Inventory and decisions

| File family | Original blocker | Implemented solution |
| --- | --- | --- |
| ListingsViewModel, UiState, UiMessage | Android-only source-set placement | Shared commonMain; lifecycle/coroutines are multiplatform |
| Labels | R.string and Android resource IDs | Shared Compose resources and StringResource |
| Price/date formatters | java.text / java.util.Locale | Common expect declarations, Android Java actuals, iOS Foundation actuals; language is a String tag |
| Profile language selector | AppCompatDelegate | Common expect composable; Android actual selector; iOS actual Settings explanation |
| Components/screens | Android text/painter resources | Shared screens and Coil UI; placeholder moved into composeResources/drawable |
| Navigation | Android-only dependency, Bundle Int parsing, string routes | JetBrains navigation, Serializable route objects/data classes, toRoute and hasRoute |
| App entry point | Android SwapCampusApp versus login/welcome-only shared App | One common App(), shared DI/ViewModel factories, both platform launchers call it |
| Image downloads | OkHttp is Android/JVM-only | Shared Coil compose; Android OkHttp network; iOS Ktor3 with Darwin engine |
| Theme | Android dynamic colors | Shared theme contract with Android/iOS actuals |

The obsolete Android routes, Android text resources, welcome screen/texts and custom
Android Application container were removed. The Android launcher now calls App().

## Final source-set size

Counts include blank/import lines and cover Kotlin in composeApp, excluding generated files.

| Source set | Kotlin files | Lines |
| --- | ---: | ---: |
| commonMain | 20 | 799 |
| androidMain | 3 | 58 |
| iosMain | 4 | 50 |

Shared Kotlin has no Android/Java/AppCompat imports, R.string/R.drawable references,
Android resource loaders, or Bundle route parsing. The only Android UI implementations
are formatters, language selection and dynamic colors. iOS retains its three actuals
and MainViewController.

## Platform comparison

| Aspect | Android observed | iOS implementation (runtime check pending) | Reason |
| --- | --- | --- | --- |
| Theme | System dark mode and dynamic device colors | Standard light/dark Material colors | Dynamic colors use an Android API |
| Back | Shared toolbar arrow and Android system back | Shared arrow and multiplatform navigation gesture | Platform navigation integration |
| Keyboard/forms | Scrolling and IME padding verified with visible input method | Same shared scrolling/IME modifiers | Native keyboards and insets differ |
| Fonts/spacing | Android screenshots inspected | Must be compared on iPhone simulator | Native text/rendering and system metrics |
| Language | Persisted in-app system/English/French selector | Settings explanation; bundle declares en/fr | Platform-specific language control |

## Reflection

- To add Spanish, add `commonMain/composeResources/values-es/strings.xml` and translate
  the shared keys. Add Spanish to Android's locale config and selector, and to iOS
  CFBundleLocalizations. Screen translations are shared, not duplicated per platform.
- Adding photo capture/sharing would need a platform interface (expect/actual or an
  injected interface) around the native camera/photo picker/share sheet. URL-based
  image display is already shared through Coil.
- File/line counts above show the remaining platform surface. Android's launcher and
  SwiftUI host are deliberately outside commonMain.

## Local verification

- Android assembleDebug and lintDebug: passed.
- compileCommonMainKotlinMetadata and compileIosMainKotlinMetadata: passed.
- Shared-code Android import/resource scan: passed.
- iOS Info.plist, OpenStep Xcode project object references and shared scheme: parsed/validated.
- Codemagic/GitHub YAML and Bash syntax: validated locally.
- Android runtime regression after migration: see android-validation.md.

The iOS Kotlin metadata check type-checks source-set declarations; it does not link
an Apple framework, compile Swift, or run the app. Cloud linking and runtime results are tracked by the branch’s Actions runs; metadata
compilation alone does not establish native runtime success.

## iOS cloud result

GitHub Actions run [41816958615](https://github.com/lzlz11/SwapCampus/actions/runs/41816958615)
passed both Android and iOS jobs for code commit `391470ad12bc91d6c5cd0ef78aec49d49cdd621e`.
The iOS job compiled/linked Kotlin Native, built the SwiftUI Xcode host, installed the
app, launched it and verified that the process remained alive. The downloaded bundle
was also checked to contain an arm64 Mach-O executable for iPhoneSimulator.

[Verified iPhone simulator launch screenshot](ios-validation/ios-launch.png)
shows the full shared Listings UI, all three demo listings/prices, the tabs and + FAB.
Artifacts include the simulator app ZIP, startup screenshot and build/launch logs.

Two cloud setup issues were fixed: setup-android's removed default `tools` package,
and Xcode's default `arm64 x86_64` simulator architecture list. The app now explicitly
builds arm64. Kotlin Native and Gradle caches are retained, including on failed builds.
During verification GitHub reported a Git Operations/Actions incident; transient push
failures were retried. Older/duplicate workflow runs were canceled in favor of the
latest PR run. The main branch has not been merged or modified by this work.

This is native build/startup verification, not the full interactive iOS acceptance suite.
The remaining iPhone interaction scenarios below still require manual testing.

## Reproduce iOS cloud verification

Both workflows use Apple Silicon. The project's current Compose artifacts do not
resolve iosX64 variants, so both modules target iosArm64 and iosSimulatorArm64.
The former unused shared iosX64 target was removed.

1. Commit/push the completed source and CI files to the intended GitHub branch.
2. GitHub Actions: run **Android and iOS builds** (or let push trigger it).
   Codemagic: run existing workflow ID **ios-framework**, now named
   **SwapCampus - complete iOS simulator app**.
3. The Mac job builds the Xcode app, installs it into an available iPhone simulator,
   launches it, checks process survival and exports ios-launch.png, launch/build logs
   and SwapCampus-simulator.zip. This is a startup smoke check, not the full UI suite.
4. For manual acceptance in Codemagic, enable SSH/VNC when manually starting a build.
   The script opens Simulator on the chosen device; its ID is in simulator-udid.txt.
5. On the cloud Mac, exercise the pending checklist below and retain screenshots.

| Scenario | Android after migration | iOS simulator |
| --- | --- | --- |
| Launch, list and English prices | Verified | Verified by startup screenshot |
| Search/category/empty state | Verified | Pending |
| Detail/back/date | Verified | Pending |
| Guest reserve, login, return | Verified | Pending |
| Invalid and valid post | Verified | Pending |
| My Listings and edit | Verified | Pending |
| Reserve/cancel/sell/reopen/delete/snackbars | Verified | Pending |
| Profile/logout/member date | Verified | Pending |
| English/French (iPhone Settings on iOS) | Verified | Pending |
| Dark mode | Verified | Pending |

## Integration references

- [JetBrains typed navigation](https://kotlinlang.org/docs/multiplatform/compose-navigation-routing.html)
- [JetBrains direct Xcode integration](https://kotlinlang.org/docs/multiplatform/multiplatform-direct-integration.html)
- [Coil multiplatform setup](https://coil-kt.github.io/coil/getting_started/)
- [GitHub standard runner labels](https://docs.github.com/en/actions/how-tos/write-workflows/choose-where-workflows-run/choose-the-runner-for-a-job)
