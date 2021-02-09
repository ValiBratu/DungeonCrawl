package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;
    private GameStateDao gameStateDao;
    private PlayerDao playerDao;

    private PlayerModel playerModel;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void add(GameState state) {

    }

    @Override
    public void update(GameState state) {

    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()){
            String sql = "SELECT current_map, saved_at, player_id FROM public.game_state WHERE id=?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(!rs.next()){
                return null;
            }

            String currentMap = rs.getString(2);
            Date savedAt = rs.getDate(3);
            int playerId = rs.getInt(1);
            PlayerModel player = playerDao.get(playerId);

            GameState gameState = new GameState(currentMap, savedAt, player);
            gameState.setId(id);
            return gameState;


        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() throws SQLException {
       Connection conn = dataSource.getConnection();

        String sql = "SELECT id, current_map, saved_at, player_id\n" +
                "\tFROM public.game_state;";
        ResultSet rs = conn.createStatement().executeQuery(sql);

        List<GameState> result = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            String map = rs.getString(2);
            Date savedAtDate = rs.getDate(3);
            int playerId = rs.getInt(4);

            PlayerModel player = playerDao.get(playerId);

            GameState  gameState = new GameState(map, savedAtDate, player);
            gameState.setId(id);
            result.add(gameState);
        }
        return result;

    }


}

