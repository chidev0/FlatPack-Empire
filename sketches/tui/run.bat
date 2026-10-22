@echo off
rem Usage: run.bat [jline|lanterna] [glyph|halfblock]
cd /d "%~dp0"
if not exist lib mkdir lib
if not exist lib\jline-3.26.3.jar curl -fsSL -o lib\jline-3.26.3.jar https://repo1.maven.org/maven2/org/jline/jline/3.26.3/jline-3.26.3.jar
if not exist lib\lanterna-3.1.2.jar curl -fsSL -o lib\lanterna-3.1.2.jar https://repo1.maven.org/maven2/com/googlecode/lanterna/lanterna/3.1.2/lanterna-3.1.2.jar
javac -encoding UTF-8 -cp "lib/*" -d out *.java || exit /b 1
set MAIN=JLineTui
if /i "%1"=="lanterna" set MAIN=LanternaTui
set STYLE=%2
if "%STYLE%"=="" set STYLE=glyph
java -cp "out;lib/*" %MAIN% %STYLE%
