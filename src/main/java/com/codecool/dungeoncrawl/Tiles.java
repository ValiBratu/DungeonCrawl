package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(3, 2));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(1, 0));
        tileMap.put("player", new Tile(27, 4));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("key",new Tile(17,23));
        tileMap.put("sword",new Tile(0,31));
        tileMap.put("closeddoor", new Tile(0,9));
        tileMap.put("opendoor", new Tile(2,9));
        tileMap.put("heal",new Tile(26,22));
        tileMap.put("spider",new Tile(30,5));
        tileMap.put("ghost",new Tile(26,6));
        tileMap.put("ladder",new Tile(2,6));
        tileMap.put("axe",new Tile(8,29));
        tileMap.put("goldenkey",new Tile (16,23));
        tileMap.put("goldendoor",new Tile(23,11));
        tileMap.put("goldenexit",new Tile(23,10));
        tileMap.put("water",new Tile(8,4));
        tileMap.put("bridgeup",new Tile(10,15));
        tileMap.put("bridgecenter", new Tile(11,15));
        tileMap.put("bridgedown",new Tile(12,15));
        tileMap.put("watherright",new Tile(10,4));
        tileMap.put("waterstatic",new Tile(11,5));
        tileMap.put("waterlame",new Tile(8,5));
        tileMap.put("waterleftside",new Tile(9,5));
        tileMap.put("greenhouse",new Tile(19,10));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
