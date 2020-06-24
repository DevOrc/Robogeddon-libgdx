# Robogeddon
A Capture the Flag Game focused on resource automation

### Running the game
To run the client:
```
gradlew packSprites run
```

To run the server:
```
gradlew server:runServer
```

#### Linux Issues
There are [currently issues with the JDK 11.05+ on Ubuntu](https://stackoverflow.com/questions/55847497/how-do-i-troubleshoot-inconsistency-detected-dl-lookup-c-111-java-result-12)
so any Ubuntu users will have to manually install [JDK 11.04](https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/tag/jdk-11.0.4%2B11.4)

### Pictures

![Mining Base](images/mining.png)
![Enemy Base](images/enemy_base.png)
![Main Menu](images/main_menu.png)

### Building an image
To build a Windows image (bundled JRE) simply run the following
```
gradlew client:runtime
```

The image will be in the client/build/image folder!