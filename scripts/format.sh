#!/usr/bin/env bash
# Formats tracked Kotlin files with ktfmt (Kotlin coding conventions) and Markdown with Prettier.
# Usage: scripts/format.sh [--check]
# Needs Java 11+ and Node.js. On Windows, run it from Git Bash.
set -euo pipefail
cd "$(dirname "$0")/.."

KTFMT_VERSION="0.64"
PRETTIER_VERSION="3.9.9"
KTFMT_JAR="build/tools/ktfmt-$KTFMT_VERSION.jar"

check=false
if [[ "${1:-}" == "--check" ]]; then
  check=true
fi

if [[ ! -f "$KTFMT_JAR" ]]; then
  mkdir -p build/tools
  curl -fsSL -o "$KTFMT_JAR" \
    "https://repo1.maven.org/maven2/com/facebook/ktfmt/$KTFMT_VERSION/ktfmt-$KTFMT_VERSION-with-dependencies.jar"
fi

mapfile -t kotlin_files < <(git ls-files '*.kt' '*.kts')
mapfile -t markdown_files < <(git ls-files '*.md')

if $check; then
  # Run both checks so one report lists every unformatted file.
  status=0
  java -jar "$KTFMT_JAR" --kotlinlang-style --dry-run --set-exit-if-changed "${kotlin_files[@]}" ||
    status=1
  npx --yes "prettier@$PRETTIER_VERSION" --check "${markdown_files[@]}" || status=1
  exit $status
else
  java -jar "$KTFMT_JAR" --kotlinlang-style "${kotlin_files[@]}"
  npx --yes "prettier@$PRETTIER_VERSION" --write "${markdown_files[@]}"
fi
