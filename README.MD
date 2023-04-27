## Requirements

Azul JDK 17 (Java FX included) 

Windows x64 https://www.azul.com/downloads/?version=java-17-lts&os=windows&architecture=x86-64-bit&package=jdk-fx#zulu


Mac ARM M1 https://www.azul.com/downloads/?version=java-17-lts&os=macos&architecture=arm-64-bit&package=jdk-fx#zulu

## Getting Started

1. Compile the game with all source files to `bin` folder.

```shell
javac src/*.java src/game/*.java src/ui/*.java -d bin
```

2. Change directory to bin folder

```shell
cd bin
```

3. Start the game with

```shell
java Wege
```

## Unit Test

1. Download JUnit Test Console

```shell
curl https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.2/junit-platform-console-standalone-1.9.2.jar -o junit-platform-console-standalone-1.9.2.jar 
```

2. Compile test classes

```shell
javac -cp target:junit-platform-console-standalone-1.9.2.jar -d out test/game/*.java src/game/*.java
```

3. Run JUnit Tests

```shell
java -jar junit-platform-console-standalone-1.9.2.jar --class-path out \
  --select-class game.CardGenerationTest \
  --select-class game.WegeCLITest \
  --select-class game.WegeDeckTest \
  --select-class game.WegeGameSettingTest \
  --select-class game.WegePlayingBoardTest \
  --select-class game.WegeGameMasterTest
```