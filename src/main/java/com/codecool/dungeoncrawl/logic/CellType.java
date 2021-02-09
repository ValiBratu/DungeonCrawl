package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    KEY("key"),
    SWORD("sword"),
    CLOSEDDOOR("closeddoor"),
    OPENDOOR("opendoor"),
    HEALINGPOTION("heal"),
    LADDER("ladder"),
    AXE("axe"),
    GOLDENDOOR("goldendoor"),
    GOLDENKEY("goldenkey"),
    GOLDENEXIT("goldenexit"),
    WATER("water"),
    BRIDGEUP("bridgeup"),
    BRIDGECENTER("bridgecenter"),
    BRIDGEDOWN("bridgedown"),
    WATERRIGHT("watherright"),
    WATERSTATIC("waterstatic"),
    WATERLAME("waterlame"),
    WATERLEFTSIDE("waterleftside"),
    GREENHOUSE("greenhouse");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}

