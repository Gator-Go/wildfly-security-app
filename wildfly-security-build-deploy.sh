#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"   # ~/wildfly
APP="$ROOT/wildfly-security-app"
BUILDER="$ROOT/wildfly-builder"

cd "$BUILDER"
git pull

cd "$ROOT"
cp -R "$BUILDER"/* "$APP"

cd "$APP"
git pull

if [[ -f update_and_commit.sh ]]; then
  chmod +x update_and_commit.sh
  sed -i 's/\r$//' update_and_commit.sh
  ./update_and_commit.sh
fi

rm -rf security
groovy WildFlyBuilder

cd Extender
groovy SecurityExtender

cd ../security
mvn clean package -e wildfly:deploy