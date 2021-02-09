package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;

public class PlayerModel extends BaseModel {
    private String playerName;
    private int hp;
    private int x;
    private int y;
    private int attackPower;
    private ArrayList<String> inventory = new ArrayList<String>();
    private String item;
    private String map;

    public PlayerModel(String playerName, int x, int y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
    }

    public PlayerModel(Player player) {
        this.playerName = player.getName();
        this.x = player.getX();
        this.y = player.getY();
        this.attackPower =player.getAttackPower();
        this.hp = player.getHealth();
        this.inventory=player.getInventory();
        this.map=player.getMap();

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAttackPower(){
        return attackPower;
    }

    public void setAttackPower(int attackPower){
        this.attackPower=attackPower;
    }

    public  ArrayList<String> getInventory(){
        return inventory;
    }
    
    public int getItemAttackPower(String item){
        switch (item){
            case "sword":
                return 1;
            case "axe":
                return 2;
        }
        return 0;
    }
    public String getItemByName(String name){
        if(inventory.contains(name)){
            return name;
        }
        return null;
    }

    public String getMap(){
        return map;
    }

    public String getItem(){
        return item;
    }



}
