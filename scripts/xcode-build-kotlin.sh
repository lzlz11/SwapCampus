#!/usr/bin/env bash
set -euo pipefail
if [[ "${OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED:-NO}" == "YES" ]]; then
  exit 0
fi
if [[ -z "${JAVA_HOME:-}" ]]; then
  export JAVA_HOME="$(/usr/libexec/java_home -v 17)"
fi
cd "${SRCROOT}/.."
bash ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
