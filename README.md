# Robogeddon
A secure the area game focused on resource automation

### Note: This is not being actively developed. There is a new (WIP) version written in Rust. 
### New Repo: [devorc/robogeddon](https://github.com/DevOrc/robogeddon) 


### Running the game
To run the client:
```
gradlew packSprites run
```

To run the server:
```
gradlew server:runServer
```

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
