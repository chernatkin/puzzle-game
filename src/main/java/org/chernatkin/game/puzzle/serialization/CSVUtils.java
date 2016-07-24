package org.chernatkin.game.puzzle.serialization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.chernatkin.game.puzzle.character.GameCharacter;
import org.chernatkin.game.puzzle.character.GamePerson;
import org.chernatkin.game.puzzle.character.Weapon;
import org.chernatkin.game.puzzle.map.Game2DMap;
import org.chernatkin.game.puzzle.map.Point2D;
import org.chernatkin.game.puzzle.map.PointType;

public abstract class CSVUtils {
    
    private static List<String[]> readCsvFile(String fileName) {
        List<String[]> lines = new ArrayList<>();
        
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            
            String line = null;
            while((line = br.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()){
                    continue;
                }
                lines.add(line.split(","));
            }
            
        } catch(FileNotFoundException fnfe){
            throw new IllegalArgumentException("File not found, file name=" + fileName);
            
        } catch(IOException ioe){
            throw new IllegalStateException("Can`t read file, file name=" + fileName);
        }
        
        return lines;
    }

    private static void writeCsvFile(List<String[]> lines, String fileName){
        
        try(FileWriter fw = new FileWriter(fileName)){
            for(String[] line : lines){
                fw.write(Stream.of(line).collect(Collectors.joining(",")));
                fw.write("\n");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can`t save file, file name=" + fileName);
        }
    }
    
    public static Set<Weapon> readWeapons(String fileName){
        return readCsvFile(fileName).stream().map(l -> parseWeapon(l, fileName)).collect(Collectors.toSet());
    }
    
    private static Weapon parseWeapon(String[] line, String fileName){
        checkLineLength(line, fileName, 3);
        
        try{
            return new Weapon(line[0], Integer.parseInt(line[1]), Integer.parseInt(line[2]));
        }
        catch(NumberFormatException nfe){
            throw new IllegalStateException("File " + fileName + " has invalid strength and/or distance, invalid line=" + Arrays.toString(line));
        }
    }
    
    public static Set<GameCharacter> readCharacters(String fileName, Map<String, Weapon> weapons){
        return readCsvFile(fileName).stream().map(l -> parseGameCharacter(l, fileName, weapons)).collect(Collectors.toSet());
    }
    
    private static GameCharacter parseGameCharacter(String[] line, String fileName, Map<String, Weapon> weapons){
        checkLineLength(line, fileName, 3);

        return new GameCharacter(line[0], line[1], loadObject(line[2], fileName, weapons));
    }
    
    private static void checkLineLength(String[] line, String fileName, int length){
        if(line.length != length){
            throw new IllegalStateException("File " + fileName + " has invalid number of fields, invalid line=" + Arrays.toString(line));
        }
    }
    
    private static <T> T loadObject(String name, String fileName, Map<String, T> objects){
        T obj = objects.get(name);
        
        if(obj == null){
            throw new IllegalStateException("File " + fileName + " has invalid link name=" + name);
        }
        
        return obj;
    }
    
    public static Game2DMap read2DMap(String fileName, Map<String, PointType> pointTypes){
        
        List<String[]> lines = readCsvFile(fileName);
        if(lines.isEmpty()){
            throw new IllegalStateException("2D map file " + fileName + " is empty");
        }
        String[] header = lines.get(0);
        
        try{
            int xSize = Integer.parseInt(header[0]);
            int ySize = Integer.parseInt(header[1]);
            
            List<Point2D> points = lines.subList(1, lines.size()).stream()
                                                                 .flatMap(l -> parsePoint2D(l, fileName, pointTypes).stream())
                                                                 .collect(Collectors.toList());
            
            return new Game2DMap(extractFileName(fileName).replace(".csv", ""), xSize, ySize, points);
            
        } catch(NumberFormatException nfe){
            throw new IllegalStateException("File " + fileName + " has invalid header, invalid header=" + Arrays.toString(header));
        }
    }
    
    private static List<Point2D> parsePoint2D(String[] line, String fileName, Map<String, PointType> pointTypes){
        return parsePoint2D(line, 0, line.length, fileName, pointTypes);
    }
    
    private static List<Point2D> parsePoint2D(String[] line, int offset, int limit, String fileName, Map<String, PointType> pointTypes){
        try{
            List<Point2D> points = new ArrayList<>();
            PointType type = loadObject(line[offset], fileName, pointTypes);
            if(limit == 6){
                int startX = Integer.parseInt(line[offset + 1]);
                int startY = Integer.parseInt(line[offset + 2]);
                
                int endX = Integer.parseInt(line[offset + 4]);
                int endY = Integer.parseInt(line[offset + 5]);
                
                for(int i = startX; i <= endX; i++){
                    for(int j = startY; j <= endY; j++){
                        points.add(new Point2D(type, i, j));
                    }
                }
            }
            else {
                points.add(new Point2D(type, Integer.parseInt(line[offset + 1]), Integer.parseInt(line[offset + 2])));
            }
            
            return points;
        } catch(NumberFormatException nfe){
            throw new IllegalStateException("File " + fileName + " has invalid point, invalid line=" + Arrays.toString(line));
        }
    }
    
    public static Set<PointType> readPointTypes(String fileName){
        List<String[]> lines = readCsvFile(fileName);
        
        return lines.stream().map(l -> parsePointType(l, fileName)).collect(Collectors.toSet());
    }
    
    private static PointType parsePointType(String[] line, String fileName){
        checkLineLength(line, fileName, 4);
        
        return new PointType(line[0], line[1], Boolean.parseBoolean(line[2]), Boolean.parseBoolean(line[3]));
    }
    
    public static GameState readGameState(String fileName, Map<String, PointType> pointTypes, Map<String, GameCharacter> characters){
        List<String[]> lines = readCsvFile(fileName);
        
        Map<GamePerson, Point2D> persons = new HashMap<>(lines.size(), 1);
        lines.stream().forEach(l -> persons.put(parseGamePerson(l, 3, 5, fileName, characters), 
                                                parsePoint2D(l, 0, 3, fileName, pointTypes).get(0)));
        
        String stateFile = extractFileName(fileName);
        return new GameState(stateFile.substring(0, stateFile.indexOf('_')), persons);
    }
    
    private static GamePerson parseGamePerson(String[] line, int offset, int limit, String fileName, Map<String, GameCharacter> characters){
        
        try{
            return new GamePerson(line[offset],
                                  loadObject(line[offset + 1], fileName, characters), 
                                  Integer.parseInt(line[offset + 2]), 
                                  Integer.parseInt(line[offset + 3]),
                                  Boolean.parseBoolean(line[offset + 4]));
        } catch(NumberFormatException nfe){
            throw new IllegalStateException("File " + fileName + " has invalid person, invalid line=" + Arrays.toString(line));
        }
        
    }
    
    private static String extractFileName(String path){
        return Paths.get(path).getFileName().toString();
    }
    
    public static void saveGameState(GameState state, String fileName){
        List<String[]> lines = new ArrayList<>(state.getPersons().size());
        
        for(Map.Entry<GamePerson, Point2D> entry : state.getPersons().entrySet()){
            String[] line = new String[8];
            line[0] = entry.getValue().getType().getId();
            line[1] = Integer.toString(entry.getValue().getX());
            line[2] = Integer.toString(entry.getValue().getY());
            
            line[3] = entry.getKey().getName();
            line[4] = entry.getKey().getCharacter().getName();
            line[5] = Integer.toString(entry.getKey().getHealth());
            line[6] = Integer.toString(entry.getKey().getExperence());
            line[7] = Boolean.toString(entry.getKey().isBot());
            
            lines.add(line);
        }
        
        writeCsvFile(lines, fileName);
    }
}
