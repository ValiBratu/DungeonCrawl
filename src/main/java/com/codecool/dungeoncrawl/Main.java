
package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.dao.GameStateDaoJdbc;
import com.codecool.dungeoncrawl.dao.PlayerDaoJdbc;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class Main extends Application {
    GameMap map;
    GameMap map2 ;
    Canvas canvas;
    Stage stage = null;
    BorderPane borderPane = null;
    GraphicsContext context = null;
    Label healthLabel ;
    Label inventoryLabel ;
    Label attackPower ;
    Label skeletonHealth;
    Label ghostHealth;
    Label spiderHealth;
    TextField userInput ;
    Label welcomeLabel ;
    Label nameLabel ;

    Button pickItems;
    Button changeLevel;
    Button saveButton;
    Button loadGameButton;
    Button submit;
    ListView<GameStateDaoJdbc> listView;
    GameDatabaseManager dbManager;
    ComboBox<String> comboBox;
    Scene sceneLoad;

    String mapGame;
    Date date;
    PlayerModel player;


    Button exportButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        map = MapLoader.loadMap("/map.txt");
        map2 = MapLoader.loadMap("/map2.txt");
        canvas = new Canvas(
                map.getWidth() * Tiles.TILE_WIDTH,
                map.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();
        healthLabel = new Label();
        inventoryLabel =new Label();
        attackPower = new Label();
        skeletonHealth = new Label();
        ghostHealth = new Label();
        spiderHealth = new Label();
        userInput = new TextField();
        welcomeLabel = new Label("Welcome: ");
        nameLabel = new Label();
        pickItems=new Button("Pick up");
        changeLevel =new Button("Change Level");
        saveButton = new Button("Save");
        loadGameButton = new Button("Load Game");

        stage=primaryStage;
        exportButton = new Button("Export");

        ////////////////////////////////////////////
        setupDbManager();
        GridPane ui = new GridPane();

        ui.setPrefWidth(250);

        ui.setPrefWidth(300);

        ui.setPadding(new Insets(20));
        ui.setVgap(10);
        borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();

        map.getPlayer().setMap(map.getMapName());

        scene.addEventFilter(KeyEvent.KEY_PRESSED,
                keyEvent -> {
                    try {
                        onKeyPressed(keyEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
        userInput.setMaxWidth(50);
        nameLabel.textProperty().bind(userInput.textProperty());

        ui.add(welcomeLabel, 0,0);
        ui.add(nameLabel, 0, 1);
        ui.add(new Label("Player Name: "), 0,2);
        ui.add(new Label("Health: "), 0, 3);
        ui.add(new Label("Inventory: "), 0, 4);

        ui.add(new Label("Attack Power: "), 0, 6);
        ui.add(new Label("Skeleton Health: "),0,9);
        ui.add(new Label("Ghost Health: "),0,10);
        ui.add(new Label("Spider Health: "),0,11);
        ui.add(userInput, 1, 2);
        ui.add(healthLabel, 1, 3);
        ui.add(inventoryLabel, 0, 5);
        ui.add(attackPower, 1,6);
        ui.add(changeLevel,0,8);
        ui.add(skeletonHealth,1,9);
        ui.add(ghostHealth,1,10);
        ui.add(spiderHealth,1,11);
        ui.add(saveButton,0,13);
        ui.add(loadGameButton, 0, 15);
        ui.add(exportButton,0,16);
        userInput.setOnAction(e-> {
            if(!userInput.getText().equals("")){
                map.getPlayer().setName(userInput.getText());
                System.out.println(map.getPlayer().getName());
                userInput.setDisable(true);
                System.out.println(userInput.getText());
            }
        });

        changeLevel.setOnAction(e->{
            // map2 transition
            if (map.getPlayer().getCell().getType() == CellType.LADDER){

                map2.getPlayer().inventory=map.getPlayer().inventory;
                map2.getPlayer().inventory.remove("key");
                map2.getPlayer().setHealth(map.getPlayer().getHealth());
                map2.getPlayer().attackPower=map.getPlayer().attackPower;
                GameMap mapTemp =map;
                map=map2;
                map2=mapTemp;
                map.getPlayer().setMap(map.getMapName());
                canvas = new Canvas(
                        map.getWidth() * Tiles.TILE_WIDTH,
                        map.getHeight() * Tiles.TILE_WIDTH);
                borderPane.setCenter(canvas);
                context=canvas.getGraphicsContext2D();
                stage.sizeToScene();
                try {
                    refresh();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }


            }
        });

        //Save Button
        saveButton.setOnAction(e->{
            Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
            saveAlert.setTitle("Save Game");
            saveAlert.setHeaderText("Do you want to save the game?");
            saveAlert.setResizable(false);
            saveAlert.setContentText("Press OK to save the game");
            Optional<ButtonType> result = saveAlert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);
            if(button == ButtonType.OK){
                // save method later
                GameDatabaseManager test = new GameDatabaseManager();
                try {
                    test.savePlayer(map.getPlayer());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println("ok saving");
            }
            else {
                System.out.println("cancelled!");
            }
        });
        //export

        exportButton.setOnAction(e ->{
            Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
            saveAlert.setTitle("Export Game");
            saveAlert.setHeaderText("Do you want to export the game?");
            saveAlert.setResizable(false);
            saveAlert.setContentText("Press OK to export to JSON file.");
            Optional<ButtonType> result = saveAlert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);
            if(button == ButtonType.OK){
                try {
                    serialization();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("ok saving");
            }
            else {
                System.out.println("cancelled!");
            }

        });

        // load button
        // load button
        loadGameButton.setOnAction(e-> {
            Stage window = new Stage();
            window.setTitle("Choose a saved game");
            submit = new Button("Submit");

            comboBox = new ComboBox<>();

            GameDatabaseManager test3 = new GameDatabaseManager();
            try {
                test3.setup();

                for(String name :test3.getPlayerNames()){
                    comboBox.getItems().add(name);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


//            comboBox.getItems().addAll(dataGameSaved.getId());
            //            comboBox.getItems().addAll((GameDatabaseManager) loadSaveGame.getAll());

            comboBox.setPromptText("Choose a game");

            comboBox.setEditable(true);

            comboBox.setOnAction(a->
                    System.out.println("User selected: " + comboBox.getValue() ));

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20, 20, 20, 20));
            layout.getChildren().addAll(comboBox, submit);

            sceneLoad = new Scene(layout, 300, 300);
            window.setScene(sceneLoad);
            window.show();

        });

        ui.add(pickItems,0,7);

        pickItems.setOnAction(e -> {
            if(map.getPlayer().getCell().getType() != CellType.FLOOR) {
                if (map.getPlayer().getCell().getType() == CellType.HEALINGPOTION){
                    map.getPlayer().increaseHealth(5);
                    map.getPlayer().getCell().setType(CellType.FLOOR);
                    try {
                        refresh();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return;
                }
                if (map.getPlayer().getCell().getType() == CellType.SWORD){
                    map.getPlayer().attackPower += 1;
                }
                if(map.getPlayer().getCell().getType() ==CellType.AXE){
                    map.getPlayer().attackPower+=2;
                }


                map.getPlayer().inventory.add(map.getPlayer().getCell().getType().getTileName());
                map.getPlayer().getCell().setType(CellType.FLOOR);
                try {
                    refresh();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                System.out.println(map.getPlayer().inventory);
            }
        });
    }



//    private void onKeyReleased(KeyEvent keyEvent) {
//        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
//        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
//        if (exitCombinationMac.match(keyEvent)
//                || exitCombinationWin.match(keyEvent)
//                || keyEvent.getCode() == KeyCode.ESCAPE) {
//            exit();
//        }
//    }

    private void onKeyPressed(KeyEvent keyEvent) throws Exception {
        ArrayList<Integer> monsterMoves = new ArrayList<Integer>();
        monsterMoves.add(-1);
        monsterMoves.add(0);
        monsterMoves.add(1);
        Random rand= new Random();

        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                map.getSkeleton().move(monsterMoves.get(rand.nextInt(monsterMoves.size())),monsterMoves.get(rand.nextInt(monsterMoves.size())));
                try {
                    map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size())) - 2, monsterMoves.get(rand.nextInt(monsterMoves.size())) + 1);
                }catch (Exception e){
                    map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size())) + 2, monsterMoves.get(rand.nextInt(monsterMoves.size())) -1);
                }
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                map.getSkeleton().move(monsterMoves.get(rand.nextInt(monsterMoves.size())),monsterMoves.get(rand.nextInt(monsterMoves.size())));
                try {
                    map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size())) - 1, monsterMoves.get(rand.nextInt(monsterMoves.size())) + 2);
                }catch (Exception e){
                    map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size())) + 1, monsterMoves.get(rand.nextInt(monsterMoves.size())) - 2);
                }
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                map.getSkeleton().move(monsterMoves.get(rand.nextInt(monsterMoves.size())),monsterMoves.get(rand.nextInt(monsterMoves.size())));
                try{
                map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size()))+1,monsterMoves.get(rand.nextInt(monsterMoves.size()))-1);
                }catch (Exception e){
                    map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size()))-1,monsterMoves.get(rand.nextInt(monsterMoves.size()))+1);
                }
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                map.getSkeleton().move(monsterMoves.get(rand.nextInt(monsterMoves.size())),monsterMoves.get(rand.nextInt(monsterMoves.size())));
                try {
                    map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size())) + 2, monsterMoves.get(rand.nextInt(monsterMoves.size())) + 2);
                }catch (Exception e){
                    map.getGhost().move(monsterMoves.get(rand.nextInt(monsterMoves.size())) - 2, monsterMoves.get(rand.nextInt(monsterMoves.size())) - 2);
                }

                refresh();
                break;
//            case S:
//                Player player = map.getPlayer();
//                dbManager.savePlayer(player);
//                break;
        }

        if(map.getPlayer().getCell().getType() == CellType.KEY ||
                map.getPlayer().getCell().getType() == CellType.SWORD ||
                map.getPlayer().getCell().getType()==CellType.HEALINGPOTION ||
                map.getPlayer().getCell().getType() == CellType.AXE ||
                map.getPlayer().getCell().getType() == CellType.GOLDENKEY) {

            pickItems.setDisable(false);
        }else{
            pickItems.setDisable(true);
        }
        if (map.getPlayer().getCell().getType()==CellType.LADDER){
            changeLevel.setDisable(false);
        }
        else{
            changeLevel.setDisable(true);
        }
        if (map.getPlayer().getCell().getType()==CellType.GOLDENEXIT){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("You Win!");
            alert.setHeaderText("You have completed the game!");
            alert.setResizable(false);
            alert.setContentText("Press OK to Play Again");
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                System.out.println("Ok pressed");
                stage.close();

                start(stage);
            } else {
                System.out.println("canceled");
            }
        }
    }



    private void refresh() throws Exception {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        inventoryLabel.setText("" + map.getPlayer().inventory);
        attackPower.setText("" + map.getPlayer().attackPower);
        skeletonHealth.setText(""+map.getSkeleton().getHealth());
        ghostHealth.setText(""+map.getGhost().getHealth());
        spiderHealth.setText(""+map.getSpider().getHealth());

        if (map.getPlayer().getHealth() < 1){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("GAME OVER");
            alert.setHeaderText("You have been killed!");
            alert.setResizable(false);
            alert.setContentText("Press OK to Play Again");
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                System.out.println("Ok pressed");
                stage.close();

                start(stage);


            } else {
                System.out.println("canceled");
            }
        }
    }
    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
    private void serialization() throws IOException {
        LocalDate todayLocalDate = LocalDate.now( ZoneId.of( "America/Montreal" ) );
        java.sql.Date sqlDate = java.sql.Date.valueOf( todayLocalDate );
        PlayerModel playermodel = new PlayerModel(map.getPlayer());
        GameState state = new GameState(MapLoader.getName(),sqlDate,playermodel);
        String jsonString = new Gson().toJson(state);
        Gson gson = new Gson();
        Writer writer = Files.newBufferedWriter(Paths.get("gamesave.json"));

        // convert user object to JSON file
        gson.toJson(jsonString, writer);

        // close writer
        writer.close();

    }

}




