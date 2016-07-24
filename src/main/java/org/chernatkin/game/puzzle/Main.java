package org.chernatkin.game.puzzle;

import java.io.Console;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.console.ConsoleCommand;
import org.chernatkin.game.puzzle.console.ConsoleUtil;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Game2DMap;
import org.chernatkin.game.puzzle.serialization.GameLoader;
import org.chernatkin.game.puzzle.serialization.GameState;

public class Main {
    
    private static GameEngine engine;
    
    private static ExecutorService botsPool;
    
    private static List<PlayerEmulator> runnedBots;
    
    private static GamePerson human;
    
    private static CurrentMapVew humanView;
    
    public static void main(String[] args) {
        
        
        try {
            runGame();
        } catch (GameOverException e) {
            System.console().printf(e.getMessage());
        } catch (IllegalArgumentException|IllegalStateException iae){
            System.console().printf(iae.getMessage());
            System.exit(1);
        }
        
        System.exit(0);
    }
    
    private static void runGame() throws GameOverException{
        
        GameLoader loader = GameLoader.loadGameData();
        
        Console console = System.console();
        Set<String> gamesNames = loader.getSavedGamesNames();
        console.printf("Saved games: " + gamesNames.stream().sorted().collect(Collectors.toList()) + "\n")
               .flush();
        
        GameState loadedGame = null;
        while(loadedGame == null){
            console.printf("Please, choose game to play (input name):").flush();
            String gameName = ConsoleUtil.readLine(console);
            loadedGame = loader.loadSavedGameState(gameName);
            if(loadedGame == null){
                console.printf("Invalid saved game name:" + gameName).flush();
            }
        }

        Game2DMap map = loader.loadMap(loadedGame.getMapName());
        if(map == null){
            throw new IllegalArgumentException("Map not found, map name:" + loadedGame.getMapName());
        }
        
        engine = new GameEngine(map, loadedGame.getPersons());
        
        human = loadedGame.getHuman();
        humanView = engine.getCurrentView(human);
        ConsoleUtil.printCurrentMapView(console, humanView);
        
        List<GamePerson> bots = loadedGame.getBots();
        botsPool = Executors.newFixedThreadPool(bots.size());
        
        runnedBots = bots.stream().map(b -> new PlayerEmulator(b, engine)).collect(Collectors.toList());
        runnedBots.forEach(p -> botsPool.submit(p));
        
        while(true){
            String command = ConsoleUtil.readLine(console);
            if(command.isEmpty()){
                continue;
            }
            
            ConsoleCommand parsedCommand = ConsoleUtil.parseCommand(command);
            if(parsedCommand == null){
                console.printf("Invalid command:" + command + "\n").flush();
                continue;
            }
            
            humanView = parsedCommand.execute(engine, human);
            ConsoleUtil.printCurrentMapView(console, humanView);
        }
    }
    

}
