package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Spider extends Actor {
    private int health = 3;
    public Spider(Cell cell) {

        super(cell);

    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public String getTileName() {
        return "spider";
    }

    @Override
    public void minusHealth(int minus){
        health = health - minus;
        if(health <= 0){
            this.getCell().setActor(null);
        }
    }



}