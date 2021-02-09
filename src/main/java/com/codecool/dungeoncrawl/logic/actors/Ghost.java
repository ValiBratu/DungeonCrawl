package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Ghost extends Actor {
    private int health = 7;
    public Ghost(Cell cell) {

        super(cell);

    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public String getTileName() {
        return "ghost";
    }

    @Override
    public void minusHealth(int minus){
        health = health - minus;

    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if((nextCell.getType() != CellType.WALL) && (nextCell.getType() != CellType.CLOSEDDOOR) && (!(nextCell.getActor() instanceof Player) &&(!(nextCell.getActor() instanceof Skeleton))&&(!(nextCell.getActor() instanceof Spider))&&(nextCell.getType()==CellType.FLOOR))) {

            super.move(dx, dy);



        }
        if(health <= 0 && !(this.getCell().getActor() instanceof Player)){
            this.getCell().setActor(null);
        }


    }
}