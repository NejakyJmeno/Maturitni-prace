package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    public static final int MOVE = 25; //objekt se vzdy musi pohybovat o presne svoji velikost
    public static final int SIZE = 25; //velikost kazdeho ctverecku objektu
    public static int XMAX = SIZE * 12; //sirka hraciho pole
    public static int YMAX = SIZE * 30; //vyska hraciho pole
    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE]; //hraci pole na pocty ctverecku
    public static Pane group = new Pane();
    public static Form object; //padajici objekt
    private static Scene scene = new Scene(group, XMAX + 150, YMAX); //cely obray (hraci plocha i score)
    public static int score = -10; //vyska skore
    private static int top = 0;
    private static boolean playing = false; //hra bezi
    public static Form nextObj = Controller.makeRect();
    public static int linesNo = 0; //pocet linek
    private static boolean threadIsRunning = false;
    public static String name = "User";
    public static int users = 0;
    public static ArrayList savedScore = new ArrayList(2);

    //hotkeys
    public static String UPcase = "UP";
    public static String RIGHTcase = "RIGHT";
    public static String LEFTcase = "LEFT";
    public static String DOWNcase = "DOWN";
    public static String pressedButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Image image = new Image(getClass().getResource("bg0.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        group.setBackground(new Background(background));
        menu(stage);
    }

    private void menu(Stage stage) {
        Button exitButton = new Button("X");
        Button button1 = new Button();
        Button button2 = new Button();
        Button button3 = new Button();
        TextField textField = new TextField ();


        exitButton.setTranslateX(XMAX + 75);
        exitButton.setTranslateY(0);
        exitButton.setStyle("-fx-font-size:40");
        exitButton.setMinSize(25,25);

        textField.setText("Your name");
        textField.setTranslateX(XMAX/2);
        textField.setTranslateY(YMAX/2 - 160);
        textField.setMinSize(150,50);

        button1.setText("PLAY");
        button1.setTranslateX(XMAX/2);
        button1.setTranslateY(YMAX/2 - 105);
        button1.setMinSize(150,100);

        button2.setText("Change settings");
        button2.setTranslateX(XMAX/2);
        button2.setTranslateY(YMAX/2);
        button2.setMinSize(150,50);

        button3.setText("View scoreboard");
        button3.setTranslateX(XMAX/2);
        button3.setTranslateY(YMAX/2 + 55);
        button3.setMinSize(150,50);

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                name = textField.getText();
                System.out.println(name);
                group.getChildren().clear();
                game(stage);
            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                group.getChildren().clear();
                settings(stage);
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.exit(0);
            }
        });

        stage.setScene(scene);
        group.getChildren().addAll(button1, button2, button3, textField, exitButton);
        stage.show();
    }

    private String arrowCheck(String text) {
        switch (text) {
            case "UP":
                text = "↑";
                break;
            case "RIGHT":
                text = "→";
                break;
            case "LEFT":
                text = "←";
                break;
            case "DOWN":
                text = "↓";
                break;
            default:
                break;
        }
        return text;

    }

    private void changingHotkeys(Stage stage, String hotkey) {
        switch (pressedButton) {
            case "up":
                UPcase = hotkey.toUpperCase();
                break;
            case "right":
                RIGHTcase = hotkey.toUpperCase();
                break;
            case "left":
                LEFTcase = hotkey.toUpperCase();
                break;
            case "down":
                DOWNcase = hotkey.toUpperCase();
                break;
        }
        pressedButton = null;
        group.getChildren().clear();
        settings(stage);
    }

    private void settings(Stage stage) {
        Button buttonUP = new Button();
        Button buttonRIGHT = new Button();
        Button buttonLEFT = new Button();
        Button buttonDOWN = new Button();
        Button buttonBack = new Button("↩");
        TextField controlChange = new TextField ();

        controlChange.setTranslateX(XMAX/2);
        controlChange.setTranslateY(YMAX/2 + 100);
        controlChange.setMinHeight(50);
        controlChange.setMaxWidth(50);
        controlChange.setOnKeyTyped(event -> changingHotkeys(stage, controlChange.getText()));

        String upText = arrowCheck(UPcase);
        String rightText = arrowCheck(RIGHTcase);
        String leftText = arrowCheck(LEFTcase);
        String downText = arrowCheck(DOWNcase);

        buttonBack.setStyle("-fx-font-size:40");
        buttonBack.setMinSize(25,25);

        buttonUP.setText(upText);
        buttonUP.setStyle("-fx-font-size:25");
        buttonUP.setTranslateX(XMAX/2);
        buttonUP.setTranslateY(YMAX/2 - 30);
        buttonUP.setMinSize(25,25);

        buttonRIGHT.setText(rightText);
        buttonRIGHT.setStyle("-fx-font-size:25");
        buttonRIGHT.setTranslateX(XMAX/2 + 60);
        buttonRIGHT.setTranslateY(YMAX/2 + 30);
        buttonRIGHT.setMinSize(25,25);

        buttonLEFT.setText(leftText);
        buttonLEFT.setStyle("-fx-font-size:25");
        buttonLEFT.setTranslateX(XMAX/2 - 60);
        buttonLEFT.setTranslateY(YMAX/2 + 30);
        buttonLEFT.setMinSize(25,25);

        buttonDOWN.setText(downText);
        buttonDOWN.setStyle("-fx-font-size:25");
        buttonDOWN.setTranslateX(XMAX/2);
        buttonDOWN.setTranslateY(YMAX/2 + 30);
        buttonDOWN.setMinSize(25,25);

        buttonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                group.getChildren().clear();
                menu(stage);
            }
        });

        buttonUP.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "up";
                group.getChildren().add(controlChange);
            }
        });

        buttonRIGHT.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "right";
                group.getChildren().add(controlChange);
            }
        });

        buttonLEFT.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "left";
                group.getChildren().add(controlChange);
            }
        });

        buttonDOWN.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "down";
                group.getChildren().add(controlChange);
            }
        });

        stage.setScene(scene);
        group.getChildren().addAll(buttonUP, buttonRIGHT, buttonLEFT, buttonDOWN, buttonBack);
        stage.show();
    }

    private void game(Stage stage) {

        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }

        Line line = new Line(XMAX, 0, XMAX, YMAX);
        Text scoretext = new Text("Score: ");
        Text level = new Text("Lines: ");
        Rectangle stoppedInterface = new Rectangle(50,110,350,500);
        Button stopButton = new Button("| |");
        Button resumeButton = new Button("▶");
        Button menuButton = new Button("Save score \n and \n return to main menu");

        line.setStyle("-fx-stroke: white;");

        scoretext.setStyle("-fx-font: 20 arial;");
        scoretext.setY(50);
        scoretext.setX(XMAX + 5);
        scoretext.setFill(Color.WHITE);

        level.setStyle("-fx-font: 20 arial;");
        level.setY(100);
        level.setX(XMAX + 5);
        level.setFill(Color.WHITE);

        stoppedInterface.setFill(Color.rgb(250,250,250, 0.5));
        stoppedInterface.setStroke(Color.CYAN);
        stoppedInterface.setStrokeWidth(5);

        stopButton.setStyle("-fx-font: 40 arial;");
        stopButton.setMinSize(50,50);
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                playing = false;
                group.getChildren().removeAll(stopButton);
                group.getChildren().addAll(stoppedInterface, menuButton, resumeButton);
            }
        });

        resumeButton.setStyle("-fx-font: 40 arial;");
        resumeButton.setMinSize(50,50);
        resumeButton.setTranslateX(200);
        resumeButton.setTranslateY(150);
        resumeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                group.getChildren().removeAll(stoppedInterface, menuButton, resumeButton);
                group.getChildren().addAll(stopButton);
                playing = true;
            }
        });

        menuButton.setStyle("-fx-font: 20 arial;");
        menuButton.setTextAlignment(TextAlignment.CENTER);
        menuButton.setMinSize(50,50);
        menuButton.setTranslateX(120);
        menuButton.setTranslateY(400);
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                savedScore.add(name);
                savedScore.add(score);
                System.out.println(savedScore);
                ScoreRecording.recordScore(savedScore);
                score = -5;
                group.getChildren().clear();
                menu(stage);
            }
        });

        group.getChildren().addAll(scoretext, line, level, stopButton);

        Form a = nextObj;
        group.getChildren().addAll(a.a, a.b, a.c, a.d);
        moveOnKeyPress(a);
        object = a;
        nextObj = Controller.makeRect();

        stage.setScene(scene);
        stage.setTitle("T E T R I S");
        stage.show();

        playing = true;

        //repeated refreshing of game status
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {

                        if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0 || object.d.getY() == 0)
                            top++;
                        else
                            top = 0;

                        if (top == 2) {
                            // GAME OVER
                            Text over = new Text("GAME OVER");
                            over.setFill(Color.RED);
                            over.setStyle("-fx-font: 70 arial;");
                            over.setY(250);
                            over.setX(10);
                            group.getChildren().add(over);
                            playing = false;
                            cancel();
                        }

                        if (playing) {
                            group.getChildren().removeAll(scoretext);
                            group.getChildren().addAll(scoretext);
                            Moving.MoveDown(object);
                            scoretext.setText("Score: " + Integer.toString(score));
                            level.setText("Lines: " + Integer.toString(linesNo));
                        }
                    }
                });
            }
        };
        //doing the repeated task in a set period
        if (!threadIsRunning) {
            threadIsRunning = true;
            Timer fall = new Timer();
            fall.schedule(task, 0, 300);
        }

    }

    public static void moveOnKeyPress(Form form) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String input = event.getCode().toString();
                //System.out.println(event.getCode());
                //System.out.println(event);
                //System.out.println(input);

                if (input.equals(UPcase)) {
                    Moving.MoveTurn(form);
                }
                else if (input.equals(RIGHTcase)) {
                    Moving.MoveRight(form);
                }
                else if (input.equals(LEFTcase)) {
                    Moving.MoveLeft(form);
                }
                else if (input.equals(DOWNcase)) {
                    Moving.MoveDown(form);
                    score++;
                }
                else {
                    System.out.println("Your input did not get recognised");
                }
            }
        });
    }

}