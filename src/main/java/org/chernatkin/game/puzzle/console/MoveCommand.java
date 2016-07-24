package org.chernatkin.game.puzzle.console;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Direction;

public class MoveCommand implements ConsoleCommand {
    
    private final Direction direction;

    public MoveCommand(String command) {
        direction = ConsoleUtil.extractDirection(command);
    }
    
    public CurrentMapVew execute(GameEngine engine, GamePerson person) throws GameOverException{
        return engine.move(person, direction);
    }

}
