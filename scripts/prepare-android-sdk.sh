#!/usr/bin/env bash
set -euo pipefail
export ANDROID_HOME="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
if [[ -z "$ANDROID_HOME" ]]; then
  echo "Android SDK is missing. Install command-line tools and set ANDROID_HOME." >&2
  exit 1
fi
SDKMANAGER="$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager"
if [[ ! -x "$SDKMANAGER" ]]; then SDKMANAGER="$(command -v sdkmanager)"; fi
# This project uses API 37, whose installed SDK package is android-37.0.
if [[ ! -f "$ANDROID_HOME/platforms/android-37.0/android.jar" || ! -d "$ANDROID_HOME/build-tools/36.0.0" ]]; then
  yes | "$SDKMANAGER" --licenses >/dev/null || true
  "$SDKMANAGER" "platforms;android-37.0" "build-tools;36.0.0"
fi
