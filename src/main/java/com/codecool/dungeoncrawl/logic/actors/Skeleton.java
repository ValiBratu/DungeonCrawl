package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import java.util.ArrayList;

public class Skeleton extends Actor {
    private int health = 9;


    public Skeleton(Cell cell) {
        super(cell);


    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }

    @Override
    public void minusHealth(int minus){
        health = health - minus;

    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if((nextCell.getType() != CellType.WALL) && (nextCell.getType() != CellType.CLOSEDDOOR) && (!(nextCell.getActor() instanceof Player) &&(!(nextCell.getActor() instanceof Ghost))&&(!(nextCell.getActor() instanceof Spider)))) {

                super.move(dx, dy);



        }
        if(health <= 0 && !(this.getCell().getActor() instanceof Player)){
            this.getCell().setActor(null);
        }


    }
}

