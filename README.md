# Sorceress's Heartful Adventure v1.1

Sorceress's Heartful Adventure (SHA) is the second entry in the Eucatastrophe series of vertical bullet hell shoot-em-up games. This repository includes all resources needed to compile and build the game locally. The underlying engine is called the Sorcery Engine, and it is written using Java and is engineered around ECS principles.

Instructions on how to play the game can be found in the `packaging/README` directory.

## Requirements

To run the game, the following minimum requirements are stated:
- Windows 8.1
- 2GB RAM

To build the game, users require OpenJDK 25.0.3, although earlier versions will most likely work.

## Installation

Install SHA as follows using Powershell:

```
git clone https://www.github.com/ickdanny/Sorceresss-Heartful-Adventure
cd Sorceresss-Heartful-Adventure
.\build.ps1
```

## Usage

The build script should create the directory `EU02_SHA` and the archive `EU02_SHA.zip`. To launch the game, navigate to `EU02_SHA` and run `EU02_SHA.exe`.
