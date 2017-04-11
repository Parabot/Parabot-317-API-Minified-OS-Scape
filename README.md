Parabot-317-API-Minified
========================

A 317 OS-Scape API for Parabot v2

### About

This API is specifically meant for OS-Scape.

### Issues
If you've an issues regarding the hooks, please report them [here](https://github.com/Parabot/Parabot-317-API-Minified/issues).

#### Maven
Parabot supports Maven as of September 2015. All information is included in the POM.xml.
The API that is supported by Parabot is also published on a Maven repository.
If you'd like to have either or both the client and the API in your project, use the following repository and dependency tags:
```
    <repositories>
        <repository>
            <id>git-parabot</id>
            <name>Parabot's Git based repo</name>
            <url>https://maven.parabot.org/</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>org.parabot</groupId>
            <artifactId>client</artifactId>
            <version>2.6.3</version>
        </dependency>
        <dependency>
            <groupId>org.parabot</groupId>
            <artifactId>317-api-os-scape</artifactId>
            <version>0.1</version>
        </dependency>
    </dependencies>
```
