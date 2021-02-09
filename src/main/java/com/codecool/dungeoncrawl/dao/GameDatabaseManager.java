package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private PlayerModel playerModel;
    private GameStateDao gameStateDao;
    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
    }

    public void savePlayer(Player player) throws SQLException {
        PlayerModel model = new PlayerModel(player);
        setup();
        playerDao.add(model);

    }

    public List<String> getPlayerNames() throws SQLException {
        List<String> allPlayerNames= new ArrayList<String>();
        for(PlayerModel player :playerDao.getAll()){
            allPlayerNames.add(player.getPlayerName());
        }
        return allPlayerNames;
    }



    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = "dungeon";
        String user = "vali";
        String password = "codecool";

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
    public GameDatabaseManager loadGame(String map, Date date, PlayerModel player) throws SQLException {
        GameState game = new GameState(map, date, player);
        setup();
//        gameStateDao.add(game);

        System.out.println(game.getCurrentMap());
        System.out.println(game.getSavedAt());
        System.out.println(game.getPlayer());
        return null;
    }

}
