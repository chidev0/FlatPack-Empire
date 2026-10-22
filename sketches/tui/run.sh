#!/usr/bin/env sh
# Usage: ./run.sh [jline|lanterna] [glyph|halfblock]
# Downloads JLine and Lanterna into lib/ on first run, compiles into out/, then runs.
set -e
cd "$(dirname "$0")"
mkdir -p lib
[ -f lib/jline-3.26.3.jar ] || curl -fsSL -o lib/jline-3.26.3.jar https://repo1.maven.org/maven2/org/jline/jline/3.26.3/jline-3.26.3.jar
[ -f lib/lanterna-3.1.2.jar ] || curl -fsSL -o lib/lanterna-3.1.2.jar https://repo1.maven.org/maven2/com/googlecode/lanterna/lanterna/3.1.2/lanterna-3.1.2.jar
javac -encoding UTF-8 -cp "lib/*" -d out *.java
case "${1:-jline}" in
  lanterna) main=LanternaTui ;;
  *)        main=JLineTui ;;
esac
exec java -cp "out:lib/*" "$main" "${2:-glyph}"
