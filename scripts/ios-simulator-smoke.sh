#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
if [[ "$(uname -m)" != "arm64" ]]; then
  echo "Use an Apple Silicon Mac runner; Compose 1.12.2 does not publish iosX64 binaries." >&2
  exit 1
fi
ARTIFACTS="$PWD/iosApp/build/artifacts"
mkdir -p "$ARTIFACTS"
if [[ -z "${SIMULATOR_UDID:-}" ]]; then
  SIMULATOR_UDID="$(xcrun simctl list devices available --json | python3 -c '
import json,sys,re
r=json.load(sys.stdin)
choices=[]
for runtime,devices in r["devices"].items():
    if ".iOS-" not in runtime: continue
    version=tuple(map(int,re.findall(r"\d+",runtime)))
    for d in devices:
        if d.get("isAvailable") and d["name"].startswith("iPhone"):
            choices.append((d["state"]=="Booted",version,d["name"],d["udid"]))
if not choices: raise SystemExit("No available iPhone simulator. Install an iOS runtime in Xcode.")
print(sorted(choices,reverse=True)[0][-1])
')"
fi
printf '%s\n' "$SIMULATOR_UDID" > "$ARTIFACTS/simulator-udid.txt"
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug \
  -destination "platform=iOS Simulator,id=$SIMULATOR_UDID" \
  -derivedDataPath iosApp/build/DerivedData CODE_SIGNING_ALLOWED=NO ARCHS=arm64 ONLY_ACTIVE_ARCH=YES build \
  2>&1 | tee "$ARTIFACTS/xcodebuild.log"
APP="$PWD/iosApp/build/DerivedData/Build/Products/Debug-iphonesimulator/SwapCampus.app"
xcrun simctl boot "$SIMULATOR_UDID" 2>/dev/null || true
xcrun simctl bootstatus "$SIMULATOR_UDID" -b
open -a Simulator --args -CurrentDeviceUDID "$SIMULATOR_UDID" || true
xcrun simctl install "$SIMULATOR_UDID" "$APP"
xcrun simctl terminate "$SIMULATOR_UDID" com.example.swapcampus.ios 2>/dev/null || true
LAUNCH="$(xcrun simctl launch "$SIMULATOR_UDID" com.example.swapcampus.ios)"
printf '%s\n' "$LAUNCH" | tee "$ARTIFACTS/launch.log"
PID="${LAUNCH##* }"
sleep 8
if ! kill -0 "$PID" 2>/dev/null; then
  echo "The iOS app exited after launch; inspect simulator logs." >&2
  xcrun simctl spawn "$SIMULATOR_UDID" log show --last 2m --style compact \
    --predicate 'process == "SwapCampus"' > "$ARTIFACTS/startup-crash.log" || true
  exit 1
fi
xcrun simctl io "$SIMULATOR_UDID" screenshot "$ARTIFACTS/ios-launch.png"
ditto -c -k --sequesterRsrc --keepParent "$APP" "$ARTIFACTS/SwapCampus-simulator.zip"
echo "iOS simulator build, installation and startup succeeded."
echo "For manual UI tests, open Simulator and select device $SIMULATOR_UDID."
