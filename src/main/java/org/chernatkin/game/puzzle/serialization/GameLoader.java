package org.chernatkin.game.puzzle.serialization;

import java.io.File;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.chernatkin.game.puzzle.character.GameCharacter;
import org.chernatkin.game.puzzle.character.Weapon;
import org.chernatkin.game.puzzle.map.Game2DMap;
import org.chernatkin.game.puzzle.map.PointType;

public class GameLoader {
    
    private static final String WEAPONS_FILE_PATH = Paths.get("descriptors", "weapons.csv").toString();
    
    private static final String CHARACTERS_FILE_PATH = Paths.get("descriptors", "characters.csv").toString();
    
    private static final String POINT_TYPES_FILE_PATH = Paths.get("descriptors", "point_types.csv").toString();
    
    private static final String MAPS_FILE_PATH = Paths.get("descriptors", "maps").toString();
    
    private static final String GAMES_FILE_PATH = Paths.get("descriptors", "games").toString();
    
    private static final DateFormat GAME_STATE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
    
    private Map<String, Weapon> weapons;
    
    private Map<String, PointType> pointTypes;
    
    private Map<String, GameCharacter> characters;
    
    private Set<String> savedGames;
    
    private static GameLoader instance;
    
    public static GameLoader loadGameData(){
        instance = new GameLoader();
        
        instance.weapons = Collections.unmodifiableMap(CSVUtils.readWeapons(WEAPONS_FILE_PATH)
                                                                .stream()
                                                                .collect(Collectors.toMap(Weapon::getName, Function.identity())));
        
        instance.characters = Collections.unmodifiableMap(CSVUtils.readCharacters(CHARACTERS_FILE_PATH, instance.weapons)
                                         .stream()
                                         .collect(Collectors.toMap(GameCharacter::getName, Function.identity())));
        
        instance.pointTypes = Collections.unmodifiableMap(CSVUtils.readPointTypes(POINT_TYPES_FILE_PATH)
                                         .stream()
                                         .collect(Collectors.toMap(PointType::getId, Function.identity())));

        File savedGamesDir = new File(GAMES_FILE_PATH);
        if(!savedGamesDir.exists() && !savedGamesDir.isDirectory()){
            throw new IllegalStateException("Directory with saved games not found, requred path=" + GAMES_FILE_PATH);
        }
        
        File[] games = savedGamesDir.listFiles((dir, name) -> name.endsWith(".csv"));
        
        instance.savedGames = Collections.unmodifiableSet(Stream.of(games)
                                                                .map(g -> g.getName().replace(".csv", ""))
                                                                .collect(Collectors.toSet()));
        
        return instance;
    }

    public Map<String, Weapon> getWeapons() {
        return weapons;
    }

    public Map<String, GameCharacter> getCharacters() {
        return characters;
    }

    public Game2DMap loadMap(String name) {
        return CSVUtils.read2DMap(generateCsvPath(MAPS_FILE_PATH, name), pointTypes);
    }

    public Set<String> getSavedGamesNames() {
        return savedGames;
    }
    
    public GameState loadSavedGameState(String name){
        return CSVUtils.readGameState(generateCsvPath(GAMES_FILE_PATH, name), pointTypes, characters);
    }
    
    public static void saveGameState(GameState state){
        String dateTime = GAME_STATE_DATE_FORMAT.format(new Date());
        String fileName = generateCsvPath(GAMES_FILE_PATH, state.getMapName() + '_' + dateTime);
        CSVUtils.saveGameState(state, fileName);
    }
    
    private static final String generateCsvPath(String basePath, String fileName){
        return Paths.get(basePath, fileName).toString() + ".csv";
    }

}
