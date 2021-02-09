package com.codecool.dungeoncrawl.logic;
import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Spider;


public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;
    private Skeleton skeleton;
    private Ghost ghost;
    private Spider spider;
    // mapNAme
    private String mapName;


    public GameMap(int width, int height, CellType defaultCellType,String mapName) {
        this.mapName=mapName;
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
    public void setSkeleton(Skeleton skeleton){
        this.skeleton=skeleton;
    }

    public Skeleton getSkeleton(){
        return skeleton;
    }

    public Ghost getGhost(){
        return ghost;
    }
    public void setGhost(Ghost ghost){
        this.ghost=ghost;
    }
    public Spider getSpider(){
        return spider;
    }
    public void setSpider(Spider spider){
        this.spider=spider;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getMapName(){
        return mapName;
    }
}
