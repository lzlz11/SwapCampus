# Android validation

Date: 2026-11-04 (Asia/Taipei).

Re-run after full commonMain migration, Serializable routes and unified App() entry point.
Device: Android Studio Pixel 10 AVD, Android API 37, x86_64.
Application: `com.example.swapcampus_zheng_li`.

## Implemented Android requirements

- Route-dependent top app bar and accessible back button on secondary screens.
- Bottom navigation with icons on Listings, My Listings and Profile only.
- Accessible + floating action button on Listings and My Listings.
- Shared English/French text resources, localized categories, roles, prices and dates.
- Persisted in-app language choice: system, English or French.
- Profile membership date and scrollable content.
- Seven localized success snackbars: post, edit, reserve, cancel reservation, sell, reopen and delete.
- Form/login IME padding and scrolling; list bottom padding keeps the FAB away from the final item.
- Edge-to-edge setup with readable system bar icons and Scaffold insets.

## Build checks

`gradlew.bat :androidApp:assembleDebug :androidApp:lintDebug`: **BUILD SUCCESSFUL**.
English/French resources: all 82 shared keys match, with matching format placeholders.
`git diff --check`: passed.

## Runtime acceptance

| Scenario | Result | Evidence checked |
| --- | --- | --- |
| Launch and navigation | PASS | Seeded list, title, three tabs, + FAB, no back arrow at top level |
| Search and category | PASS | Keyword, category and combined filter; empty message; clear restores list |
| Detail and back | PASS | Seller, localized date/price, toolbar back; secondary screens hide tabs |
| Logged-out reservation | PASS | Login prompt, empty/incorrect credential errors, returns to original detail and reserves |
| Posting | PASS | Guest FAB gate; login returns to pending form; empty title, abc/negative price errors; 12,50 accepted |
| My Listings and editing | PASS | Only current owner listings, + FAB, saved edits, owner cannot reserve own listing |
| Status and deletion | PASS | Two demo accounts exercised reservation, owner cancellation, sale, reopening and confirmed deletion; canceling deletion preserves item |
| Profile | PASS | Name, email, localized role, Member since date and logout |
| Language | PASS | English/French text, dates and prices; both survive force-stop/relaunch; Follow system clears override |
| Dark mode | PASS | List, detail and form inspected; top/back/FAB visible; on-screen IME shown and submit reachable |
| Shared image resources | PASS | Coil photo path and shared vector placeholder tested with an unavailable image URL |
| Success messages | PASS | All seven types checked; publish/edit survive navigation; French publish/delete checked |

Representative values: `€15.00` / `15,00 €`; `Oct 1, 2026` / `1 oct. 2026`.

## Screenshots

- [Light list with FAB and tabs](android-validation/01-listings-light.png)
- [Light detail with back arrow](android-validation/02-detail-light.png)
- [French detail](android-validation/03-detail-french.png)
- [Dark list](android-validation/04-listings-dark.png)
- [Dark form](android-validation/07-form-dark.png)
- [Shared image placeholder](android-validation/09-shared-image-placeholder.png)

## Scope and final state

Checks cover this Android emulator, not a physical device or every supported API level.
The existing repositories keep data in memory; process restart resets demo data.
Temporary QA listings were removed/reset. Final state: English, logged out, original three listings;
system night-mode and keyboard setting restored.

Full commonMain migration and typed multiplatform navigation are implemented. Shared and iOS source-set Kotlin metadata compilation passed. Apple framework linking, Swift compilation and iPhone simulator startup passed in [cloud run 41816958615](https://github.com/lzlz11/SwapCampus/actions/runs/41816958615). Full interactive iOS acceptance is still pending. See [migration notes](multiplatform-migration.md).
