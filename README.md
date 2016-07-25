# Game Puzzle


## runnig service
Service requires Java 8 JDK and maven 3

```Shell
mvn clean install
./run.sh on Linux and run.bat on Windows (or directly java -jar target/executable-puzzle-0.0.1-SNAPSHOT.jar)

```

## Usage
Now service started and ready for STDIN input data

```Shell
Saved games: [bunker_simple]
Please, choose game to play (input name):
```

Input one saved game name from list of saved games

Than game will start

For example:
```Shell
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|  |~~~~~~~~
~~|A! |~~~~~~~~

Health:100, Experience:0
```

symbol "!" - indicate human player, otherwise - player is bot

Control symbols: "e + Enter" - forward, "d + Enter" - back, "s + Enter" - left, "f + Enter" - right

Attack symbols: "ae + Enter" - attack forward, "d + Enter" - attack back, "s + Enter" - attack left, "f + Enter" - attack right

Save game: "z + Enter"

Exit game: "q + Enter"

## Customization notes
Maps, Characters and Initial state of game can be customized


weapons.csv - list of available weapons in character.csv, contains (id, strength, distance)

characters.csv - list of available charactrers, contains (id, visible sign, weapon id)

point_types.csv - list of available types of point on map, contains (id, visible symbol, is transparent flag, is walkable flag)

maps.csv - map description, contains header(size x, size y), points (type, x, y) or points range (type, from x, from y, type, to x, toy)

games.csv - current initial game state description, (point(type, x, y), person(character id, health, experience, is bot flag))
