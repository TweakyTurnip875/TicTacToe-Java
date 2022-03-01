package com.noah.Main;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.paint.*;

public class Main extends Application {
    // --- global variables ---
    private char turn = 'X';

    Cell[][] cell = new Cell[3][3];

    GridPane pane;
    GridPane buttons;
    Text status;
    String[] colors = {
    		"lightgreen", 
    		"white", 
    		"pink", 
    		"lightblue", 
    		"lightyellow", 
    		"DeepSkyBlue", 
    		"LightCyan",
    		"MediumSlateBlue",
    		"SpringGreen",
    		"PaleGoldenRod",
    		"PowderBlue"
    };

    Button restartBtn = new Button("Restart");
    Button quitBtn = new Button("Quit");

    // --- start method ---
    @Override
    public void start(Stage stage) throws Exception {
        status = new Text("X's Turn to play!");
        
        // base button styles
        String btnColors = "#653345";
        
        configBtnStyle(restartBtn, 130, btnColors, "1");
        configBtnStyle(quitBtn, 130, btnColors, "1");
        
        restartBtn.setEffect(new Glow(0.3));
        quitBtn.setEffect(new Glow(0.3));
        
        // restart btn event handlers
        restartBtn.setOnMouseClicked(e -> handleRestartClick());
        restartBtn.setOnMouseEntered(e -> restartBtn.setStyle(
            "-fx-background-color: #303338;" +
            " -fx-border-color: white;" +
            " -fx-border-width: 2;" +
            " -fx-border-radius: 10px;"
            
        ));
        restartBtn.setOnMouseExited(e -> configBtnStyle(restartBtn, 130, btnColors, "1"));
        
        // quit btn event handlers
        quitBtn.setOnMouseClicked(e -> System.exit(0));
        quitBtn.setOnMouseEntered(e -> quitBtn.setStyle(
        	"-fx-background-color: #303338;" +
        	" -fx-border-color: white;" +
        	" -fx-border-width: 2;" +
        	" -fx-border-radius: 10px;" +
        	" -fx-color: white"
        	
        ));
        quitBtn.setOnMouseExited(e -> configBtnStyle(quitBtn, 130, btnColors, "1"));
        
        // button alignment
        buttons = new GridPane();

        buttons.add(restartBtn, 0, 0);
        buttons.add(quitBtn, 1, 0);
        buttons.setMargin(restartBtn, new Insets(10));
        buttons.setMargin(restartBtn, new Insets(10));


        // cell array rendered as grid
        pane = new GridPane();
        pane.setCursor(Cursor.OPEN_HAND);

        String color = colors[(int)(Math.random() * colors.length)];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pane.add(cell[i][j] = new Cell(), i, j);
                cell[i][j].setStyle("-fx-border-color: " + color + ";");
            }
        }
        
        
        // item alignment
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.setTop(status);
        borderPane.setAlignment(status, Pos.CENTER);
        borderPane.setMargin(status, new Insets(20));
        borderPane.setMargin(pane, new Insets(20));
        borderPane.setMargin(buttons, new Insets(0, 0, 0, 110));
        borderPane.setBottom(buttons);
        borderPane.setStyle("-fx-background-color: #303338");

        // status msg styles
        status.setFill(Color.AQUA);
        status.setScaleX(1.5);
        status.setScaleY(1.5);
        status.setEffect(new Glow(0.9));
        
        // add everything to scene
        Scene s = new Scene(borderPane, 500, 280);
        s.setFill(Color.web("#303338"));
        
        // set stage
        stage.setTitle("Tic Tac Toe!");
        stage.setScene(s);
        stage.setResizable(false);
        stage.show();
    }
    
    // --- Main Class Event Methods ---
    public void handleRestartClick() {
        turn = 'X';
        status.setText("X's turn to play!");
        status.setFill(Color.AQUA);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell[i][j].getToken() != ' ') {
                    cell[i][j].setToken(' ');
                }
            }
        }
    }
    // --- Button Config Methods
    public void configBtnStyle(Button btn, double width, String color, String opacity) {
        btn.setPrefWidth(width);
        //#7a7e80
        btn.setCursor(Cursor.HAND);
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + color + "; -fx-opacity: " + opacity + "; -fx-border-width: 2; -fx-border-color: transparent;");


    }
    // --- Game State Methods ---
    public boolean isTied() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cell[i][j].getToken() == ' ') {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isWon(char t) {
        for (int i = 0; i < 3; i++) {
            if ( // horizontal
                cell[0][i].getToken() == t &&
                cell[1][i].getToken() == t &&
                cell[2][i].getToken() == t
            ) {
                return true;
            } else if ( // vertical
                cell[i][0].getToken() == t &&
                cell[i][1].getToken() == t &&
                cell[i][2].getToken() == t
            ) {
                return true;
            } else if ( // diagonal right
                cell[0][2].getToken() == t &&
                cell[1][1].getToken() == t &&
                cell[2][0].getToken() == t
            ) {
                return true;
            } else if ( // diagonal left
                cell[0][0].getToken() == t &&
                cell[1][1].getToken() == t &&
                cell[2][2].getToken() == t
            ) {
                return true;
            }
        }

        return false;
    }
    
    // --- Cell Class ---
    public class Cell extends Pane {

        private char token = ' ';
        private Text p;

        public Cell() {
            this.setEffect(new Glow(0.9));
            this.setPrefSize(2000, 2000);
            this.setOnMouseClicked(e -> handleCellClick());
        }
        public char getToken() {
            return token;
        }
        public void setToken(char token) {
            this.token = token;

            handleToken(token);
        }
        
        // sets turn (X / O) styles
        public void handleToken(char token) {

            if (this.token == 'X') {
                p = new Text("X");
                p.setScaleX(2);
                p.setScaleY(2);
                p.setTranslateX(this.getWidth() / 2.1);
                p.setTranslateY(this.getHeight() / 1.7);
                p.setFill(Color.AQUA);

                getChildren().add(p);
            } else if (this.token == 'O') {
                p = new Text("O");
                p.setScaleX(2);
                p.setScaleY(2);
                p.setTranslateX(this.getWidth() / 2.1);
                p.setTranslateY(this.getHeight() / 1.7);
                p.setFill(Color.RED);

                getChildren().add(p);
            } else {
                p.setText(" ");
            }
        }
        
        // Calculates turn, sets status msg, and handles game over
        private void handleCellClick() {
            if (token == ' ' && turn != ' ') {
                setToken(turn);
                if (isWon(token)) {
                    status.setText("Player " + token + " won!");
                    turn = ' ';
                } else if (isTied()) {
                    status.setText("The game was tied!");
                    status.setFill(Color.HOTPINK);
                    turn = ' ';
                } else if (turn == 'X') {
                    turn = 'O';
                    status.setText("O's turn to play!");
                    status.setFill(Color.RED);
                } else {
                    turn = 'X';
                    status.setText("X's turn to play!");
                    status.setFill(Color.AQUA);
                }
            }
        }

    }
    // main method
    public static void main(String[] args) {
        launch(args);
    }
}