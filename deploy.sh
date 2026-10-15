#!/usr/bin/env bash
# Build the production web bundle and deploy it to Vercel.
# Usage: ./deploy.sh          (production)
#        ./deploy.sh preview  (preview URL)
set -euo pipefail
cd "$(dirname "$0")"

./gradlew :composeApp:wasmJsBrowserDistribution

DIST=composeApp/build/dist/wasmJs/productionExecutable
if [[ "${1:-}" == "preview" ]]; then
  vercel deploy "$DIST" --yes --name battlemarked
else
  vercel deploy "$DIST" --yes --name battlemarked --prod
fi
