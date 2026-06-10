$ErrorActionPreference = "Stop"

# Clean
Remove-Item -Recurse -Force build,SHA,SHA.zip -ErrorAction SilentlyContinue

# Make build dirs
mkdir build -Force
mkdir build\classes -Force
mkdir build\artifacts -Force

# Compile
javac -d build\classes src\*.java

# Create jar
jar --create --file build\artifacts\SHA.jar --main-class Driver -C build\classes .

# Create runtime image
jlink --add-modules java.base,java.desktop --output build\runtime

# Package
jpackage `
  --type app-image `
  --name SHA `
  --input .\build\artifacts `
  --main-jar SHA.jar `
  --runtime-image .\build\runtime `
  --icon .\packaging\ico.ico

# Copy resources and packaging
cp -r res SHA\
cp packaging\* SHA\

# Zip it up
Compress-Archive -Path SHA -DestinationPath SHA.zip