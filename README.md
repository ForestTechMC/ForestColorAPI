# ForestColorAPI
![badge](https://img.shields.io/github/v/release/ForestTechMC/ForestColorAPI)
[![badge](https://jitpack.io/v/ForestTechMC/ForestColorAPI.svg)](https://jitpack.io/#ForestTechMC/ForestColorAPI)
![badge](https://img.shields.io/github/downloads/ForestTechMC/ForestColorAPI/total)
![badge](https://img.shields.io/github/last-commit/ForestTechMC/ForestColorAPI)
![badge](https://img.shields.io/badge/platform-spigot%20%7C%20bungeecord-lightgrey)
[![badge](https://img.shields.io/discord/896466173166747650?label=discord)](https://discord.gg/2PpdrfxhD4)
[![badge](https://img.shields.io/github/license/ForestTechMC/ForestColorAPI)](https://github.com/ForestTechMC/ForestColorAPI/blob/master/LICENSE.txt)

**[JavaDoc 1.1](https://foresttechmc.github.io/ForestColorAPI/1.1/)**

Small and effective Color API for your plugins.\
Only 1.16+ version of spigot support!

## Table of contents

* [Getting started](#getting-started)
* [Example of patterns](#example-of-patterns)
* [Using the color API](#using-color-api)
* [License](#license)

## Getting started

Make sure you reloaded maven or gradle in your project.

### Add ForestColorAPI to your project

[![badge](https://jitpack.io/v/ForestTechMC/ForestColorAPI.svg)](https://jitpack.io/#ForestTechMC/ForestColorAPI)

You need to add this dependency into your plugin, then look at under the dependencies example

<details>
    <summary>Maven</summary>

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.ForestTechMC</groupId>
        <artifactId>ForestColorAPI</artifactId>
        <version>VERSION</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
</details>

<details>
    <summary>Gradle</summary>

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.ForestTechMC:ForestColorAPI:VERSION'
}
```
</details>

### Example of patterns

If you want to use gradient you need to use pattern like `{/#b36000}Super gradient!{/#72cc00}`
For normal RGB `{#72cc00}Super color!`


### Using Color API

```java
// Example of removing chars, legacy colors, and patterns
String cleanMessage = ColorAPI.clear("{/#b36000}Super gradient{/#72cc00} &3Some color");
// Output -> "Super gradient Some color" message without patterns, colors, chars...
// You can use separate methods to clear only some kind of coloring

// Example of universal usage for colorize
player.sendMessage(ColorAPI.colorize("{/#b36000}Super gradient{/#72cc00} {#b36000}Super RGB color"));

// Example of gradient method
player.sendMessage(ColorAPI.colorizeGradient("{/#b36000}Super gradient{/#72cc00}"));

// Example of RGB method
player.sendMessage(ColorAPI.colorizeRGB("{#b36000}Super RGB color"));

// Example of classic method
player.sendMessage(ColorAPI.colorizeClassic("&3Some color"));

// Example of selecting colorize type <GRADIENT | RGB | CLASSIC>
player.sendMessage(ColorAPI.colorizeType(ColorizeType.RGB, "{#b36000}Super RGB color"));
```

## License
ForestRedisAPI is licensed under the permissive MIT license. Please see [`LICENSE.txt`](https://github.com/ForestTechMC/ForestColorAPI/blob/master/LICENSE.txt) for more information.