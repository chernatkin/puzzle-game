package org.chernatkin.game.puzzle.console;

import java.io.Console;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.map.CurrentMapVew;
import org.chernatkin.game.puzzle.map.Direction;
import org.chernatkin.game.puzzle.map.Point2D;

public class ConsoleUtil {
    
    private static final Map<String, Direction> COMMAND_TO_DIRECTION = new HashMap<>(4);
    
    private static final Map<String, Function<String, ConsoleCommand>> CMD_MAPPING = new HashMap<>(8);
    
    static{
        COMMAND_TO_DIRECTION.put("e",  Direction.NORTH);
        COMMAND_TO_DIRECTION.put("s",  Direction.WEST);
        COMMAND_TO_DIRECTION.put("d",  Direction.SOUTH);
        COMMAND_TO_DIRECTION.put("f",  Direction.EAST);
        
        for(String cmd : COMMAND_TO_DIRECTION.keySet()){
            CMD_MAPPING.put(cmd, MoveCommand::new);
        }
        
        CMD_MAPPING.put("a", AttackCommand::new);
        CMD_MAPPING.put("z", SaveCommand::new);
        CMD_MAPPING.put("q", ExitCommand::new);
    }
    
    
    
    public static void printCurrentMapView(Console console, CurrentMapVew view){
        if(view == null){
            return;
        }
        PrintWriter pw = console.writer();
        
        Point2D[][] map = view.getVisibleMap();
        GamePerson human = view.getVisiblePersons().get(view.getCurrentPersonPoint());
        
        for(int i = map.length - 1; i >= 0; i--){
            Point2D[] row = map[i];
            for(int j = 0; j < row.length; j++){
                Point2D p = row[j];
                if(p == null){
                    pw.append("~");
                    continue;
                }
                
                GamePerson person = view.getVisiblePersons().get(p);
                
                if(!p.getType().isTransparent() || !p.getType().isWalkable() || person == null){
                    pw.append(p.getType().getSymbol());
                    continue;
                }

                pw.append(person.getCharacter().getSymbol());
                if(!person.isBot()){
                    pw.append("!");
                }
            }
            pw.println();
        }
        pw.println();
        pw.print("Health:");
        pw.print(human.getHealth());
        pw.print(", Experience:");
        pw.print(human.getExperence());
        pw.println();
        
        pw.flush();
    }
    
    public static String readLine(Console console){
        String line = console.readLine();
        if(line == null){
            return "";
        }
        
        return line.trim();
    }
    
    public static Direction extractDirection(String command){
        return COMMAND_TO_DIRECTION.get(command);
    }
    
    public static ConsoleCommand parseCommand(String command){
        return Optional.ofNullable(CMD_MAPPING.get(command.substring(0, 1)))
                       .map(f -> f.apply(command))
                       .orElse(null);
    }

}
