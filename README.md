[![Build Status](https://travis-ci.org/alltiny/alltiny-chorus.svg?branch=master)](https://travis-ci.org/alltiny/alltiny-chorus)

Alltiny Chorus
==============

## What it is
Alltiny Chorus is a Java application for rendering sheet music on screen and to play the music using MIDI. Chorus is created for singers as support when learning new pieces of music. Chorus allows to adjust tempo and to mute selected voices.

## What it currently does not
Chorus is still work in progress. Following features are missing:
- render and interpret repetitions (also D.C., D.S.)
- render beat notations on screen (C, 3/4, ...)
- render and interpret fermatas

## What it might become
Chorus currently not supports any editing. You need to edit your music files as XML in a propriate editor. In the final version Chorus should have an edit-mode and as well be apropriate for layouting sheet music.

## How to build?
alltiny-chorus uses [gradle] for building. More specifically the gradle wrapper.
```sh
gradle wrapper
```
Running alltiny-chorus directly from the source files:
```sh
./gradlew :chorus:run
```
Building an executable jar-file:
```sh
./gradlew :chorus:bootJar
```
the executable jar file can be found in `alltiny-chorus/chorus/build/libs/`.

## How to set up my development environment?
Depending on whether you use IntelliJ IDEA or Eclipse, [gradle] can create the project files for you:
* for IntelliJ IDEA
```sh
cd alltiny-chorus
gradle idea
```

* for Eclipse
```sh
cd alltiny-chorus
gradle eclipse
```

---
[gradle]:http://www.gradle.org - An open source building tool, much like maven, but rather more flexible.
