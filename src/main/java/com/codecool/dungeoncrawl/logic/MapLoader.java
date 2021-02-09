package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Spider;
import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    private static String name;


    public static GameMap loadMap(String mapText) {
       name=mapText;
        InputStream is = MapLoader.class.getResourceAsStream(mapText);
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine();
        GameMap map = new GameMap(width, height, CellType.EMPTY,mapText);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.setSkeleton(new Skeleton(cell));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell, "test"));
                            break;
                        case 'w':
                            cell.setType(CellType.SWORD);
                            break;
                        case 'k':
                            cell.setType(CellType.KEY);
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSEDDOOR);
                            break;
                        case 'o':
                            cell.setType(CellType.OPENDOOR);
                            break;
                        case 'h':
                            cell.setType(CellType.HEALINGPOTION);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            map.setGhost(new Ghost(cell));
                            break;
                        case 'a':
                            cell.setType((CellType.FLOOR));
                            map.setSpider(new Spider(cell));
                            break;
                        case 'l':
                            cell.setType(CellType.LADDER);
                            break;
                        case 'x':
                            cell.setType(CellType.AXE);
                            break;
                        case 'z':
                            cell.setType(CellType.GOLDENDOOR);
                            break;
                        case 'j':
                            cell.setType(CellType.GOLDENKEY);
                            break;
                        case 'r':
                            cell.setType(CellType.WATER);
                            break;
                        case 'b':
                            cell.setType(CellType.BRIDGEUP);
                            break;
                        case 'n':
                            cell.setType(CellType.BRIDGECENTER);
                            break;
                        case 'm':
                            cell.setType(CellType.BRIDGEDOWN);
                            break;
                        case 'c':
                            cell.setType(CellType.WATERRIGHT);
                            break;
                        case 'p':
                            cell.setType(CellType.WATERSTATIC);
                            break;
                        case 'f':
                            cell.setType(CellType.WATERLAME);
                            break;
                        case 'v':
                            cell.setType(CellType.WATERLEFTSIDE);
                            break;
                        case 'y':
                            cell.setType(CellType.GREENHOUSE);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }


    public static String getName() {
        return name;
    }
}
