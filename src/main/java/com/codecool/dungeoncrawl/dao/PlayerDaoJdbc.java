package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Timestamp;
import java.util.Date;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;
    private int playerID;
    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) throws SQLException {
        // update daca exista deja numele ****

        if(checkPlayerExists(player.getPlayerName())){
            update(player);
        }else {


            // player
            try (Connection conn = dataSource.getConnection()) {
                String sql = "INSERT INTO player (player_name, hp, x, y,attack_power) VALUES (?, ?, ?, ?,?);";
                PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, player.getPlayerName());
                statement.setInt(2, player.getHp());
                statement.setInt(3, player.getX());
                statement.setInt(4, player.getY());
                statement.setInt(5, player.getAttackPower());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                player.setId(resultSet.getInt(1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //save inventory
            playerID = player.getId();
            for (String item : player.getInventory()) {
                try (Connection conn = dataSource.getConnection()) {
                    String sql = "INSERT INTO inventory (player_id, item_name, item_attackpower) VALUES (?, ?, ?);";
                    PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, playerID);
                    statement.setString(2, item);
                    statement.setInt(3, player.getItemAttackPower(item));
                    statement.executeUpdate();
                    ResultSet resultSet = statement.getGeneratedKeys();
                    resultSet.next();
                    player.setId(resultSet.getInt(1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //save game_state
            try (Connection conn = dataSource.getConnection()) {
                String sql = "INSERT INTO game_state (saved_at,player_id) VALUES (?, ?);";
                PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                Date date = new Date();
                long time = date.getTime();
                Timestamp ts = new Timestamp(time);
                statement.setTimestamp(1, ts);
                statement.setInt(2, playerID);
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                player.setId(resultSet.getInt(1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //save map
            try (Connection conn = dataSource.getConnection()) {
                String sql = "INSERT INTO maps (map_name,player_id) VALUES (?, ?);";
                PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, player.getMap());
                statement.setInt(2, playerID);
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                resultSet.next();
                player.setId(resultSet.getInt(1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public void update(PlayerModel player) throws SQLException {
        //player
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET player_name=?,hp=?,attack_power=?,x=?,y=?" +
                    "WHERE player_name=?";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getAttackPower());
            statement.setInt(4, player.getX());
            statement.setInt(5, player.getY());
            statement.setString(6,player.getPlayerName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int playerIdUpdate=player.getId();
        ArrayList<String> inventoryUpdate=player.getInventory();

        //player game_state
       Connection conn = dataSource.getConnection();
        String sql = "UPDATE game_state SET saved_at=? WHERE player_id=?;";
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        Date date = new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        statement.setTimestamp(1, ts);
        statement.setInt(2, player.getId());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()) {
            System.out.println(resultSet.getStatement());
            player.setId(resultSet.getInt(1));
        }
        //save map

        String sqlMap = "UPDATE maps SET map_name=? WHERE player_id=?;";
        PreparedStatement statementMap = conn.prepareStatement(sqlMap, Statement.RETURN_GENERATED_KEYS);

        statementMap.setString(1, player.getMap());
        statementMap.setInt(2, playerIdUpdate);
        statementMap.executeUpdate();
        ResultSet resultSetMap = statementMap.getGeneratedKeys();
        resultSetMap.next();
        System.out.println(resultSetMap.getStatement());
        player.setId(resultSetMap.getInt(1));

        // inventory
        String sqlDeleteInventory = "DELETE FROM inventory WHERE player_id=?";
        PreparedStatement statementDeleteInventory = conn.prepareStatement(sqlDeleteInventory, Statement.RETURN_GENERATED_KEYS);

        statementDeleteInventory.setInt(1, playerIdUpdate);
        statementDeleteInventory.executeUpdate();
        ResultSet resultDeleteInventory = statementDeleteInventory.getGeneratedKeys();
        resultDeleteInventory.next();
        System.out.println(resultDeleteInventory.next());
        System.out.println(resultDeleteInventory.getStatement());
        if(resultDeleteInventory.next()) {
            player.setId(resultDeleteInventory.getInt(1));
        }
        System.out.println(inventoryUpdate);

        for (String item : player.getInventory()) {

            String sqlInsert = "INSERT INTO inventory (player_id, item_name, item_attackpower) VALUES (?, ?, ?);";
            PreparedStatement statementInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            statementInsert.setInt(1, playerIdUpdate);
            statementInsert.setString(2, item);
            statementInsert.setInt(3, player.getItemAttackPower(item));
            statementInsert.executeUpdate();
            ResultSet resultSetInsert = statement.getGeneratedKeys();
            if(resultSetInsert.next()) {
                player.setId(resultSetInsert.getInt(1));
            }

        }


    }

    @Override
    public PlayerModel get(int id) {
        return null;
    }





    public boolean checkPlayerExists(String playerName){
        try (Connection conn = dataSource.getConnection()) {

            String sql = "SELECT player_name FROM public.player;";
            PreparedStatement st = conn.prepareStatement(sql);
            ArrayList<String> nameList = new ArrayList<>();
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                String name = rs.getString(1);
                nameList.add(name);
            }

           if(nameList.contains(playerName)){
               return true;
           }
           return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() throws SQLException {
        Connection conn = dataSource.getConnection();

        String sql = "SELECT *" +
                "\tFROM public.player;";
        ResultSet rs = conn.createStatement().executeQuery(sql);
        System.out.println(rs.getStatement());

        List<PlayerModel> result = new ArrayList<>();
        while (rs.next()){

            String name=rs.getString(2);
            int x=rs.getInt(5);
            int y=rs.getInt(6);
            PlayerModel player = new PlayerModel(name,x,y);
            player.setHp(rs.getInt(3));
            player.setAttackPower(rs.getInt(4));
            player.setId(rs.getInt(1));
            result.add(player);
        }
        return result;
    }


}
