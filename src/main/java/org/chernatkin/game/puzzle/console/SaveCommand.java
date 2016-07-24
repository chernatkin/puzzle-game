package org.chernatkin.game.puzzle.console;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.serialization.GameLoader;
import org.chernatkin.game.puzzle.serialization.GameState;

public class SaveCommand implements ConsoleCommand {

    
    public SaveCommand(String command) {
    }

    @Override
    public CurrentMapVew execute(GameEngine engine, GamePerson person) throws GameOverException {
        GameState state = new GameState(engine.getMapName(), engine.getPersons());
        GameLoader.saveGameState(state);
        return engine.getCurrentView(person);
    }
    
    

}
