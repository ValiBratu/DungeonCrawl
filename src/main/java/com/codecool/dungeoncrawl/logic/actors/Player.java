package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;

import java.util.ArrayList;

public class Player extends Actor {
    private String name;
    public int attackPower = 2;
    private int health = 15;
    private int x;
    private int y;
    public ArrayList<String> inventory = new ArrayList<String>();
    private String map;

    public String getTileName() {
        return "player";
    }

//    Skeleton skeleton = new Skeleton(getCell(), getHealth());

    @Override
    public void move(int dx, int dy) {

        x=getCell().getX();
        y=getCell().getY();
        System.out.println(x+" "+y);
        Cell nextCell = getCell().getNeighbor(dx, dy);

        if ((nextCell.getActor() instanceof Skeleton)||(nextCell.getActor() instanceof Ghost)||(nextCell.getActor() instanceof Spider)){
            System.out.println("Skeleton life: " + nextCell.getActor().getHealth());
            nextCell.getActor().minusHealth(attackPower);
            this.health = health -2;
            return;
        }

        if((nextCell.getType() != CellType.WALL) && (nextCell.getType() != CellType.CLOSEDDOOR) && (nextCell.getType() != CellType.GOLDENDOOR)) {
            super.move(dx, dy);

        }
        if ((nextCell.getType() == CellType.CLOSEDDOOR) && inventory.contains("key")){
            nextCell.setType(CellType.OPENDOOR);
        }
        if ((nextCell.getType()==CellType.GOLDENDOOR) && inventory.contains("goldenkey")){
            nextCell.setType((CellType.GOLDENEXIT));
        }





    }
    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void minusHealth(int minus){
        health = health - minus;
        if(health <= 0){
            this.getCell().setActor(null);
        }
    }
    public void increaseHealth(int plus){
        this.health+=plus;
    }


    public void setHealth(int health){
        this.health=health;
    }

    public Player(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }


    public int getAttackPower() {
        return attackPower;
    }

    public ArrayList<String> getInventory(){
        return inventory;
    }

    public void setMap(String map){
        this.map=map;
    }
    public String getMap(){
        return map;
    }
}

