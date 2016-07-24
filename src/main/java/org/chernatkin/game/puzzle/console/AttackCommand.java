package org.chernatkin.game.puzzle.console;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Direction;

public class AttackCommand implements ConsoleCommand {

    private final Direction direction;
    
    public AttackCommand(String command) {
        direction = ConsoleUtil.extractDirection(command.substring(1, 2));
    }

    @Override
    public CurrentMapVew execute(GameEngine engine, GamePerson person) throws GameOverException {
        return engine.applyWeapon(person, direction, person.getCharacter().getWeapon());
    }

    
}
