package org.chernatkin.game.puzzle.console;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.engine.GameEngine;
import org.chernatkin.game.puzzle.engine.GameOverException;
import org.chernatkin.game.puzzle.map.CurrentMapVew;

public interface ConsoleCommand {

    public CurrentMapVew execute(GameEngine engine, GamePerson person) throws GameOverException;
}
