package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import java.sql.SQLException;
import java.util.List;

public interface PlayerDao {
    void add(PlayerModel player) throws SQLException;
    void update(PlayerModel player) throws SQLException;
    PlayerModel get(int id);
    List<PlayerModel> getAll() throws SQLException;
}
