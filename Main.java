package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    public static final int SIZE = 25; //velikost kazdeho ctverecku objektu
    public static int XMAX = SIZE * 12; //sirka hraciho pole
    public static int YMAX = SIZE * 30; //vyska hraciho pole
    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE]; //hraci pole na pocty ctverecku
    public static Pane group = new Pane();
    public static Form object; //padajici objekt
    private static Scene scene = new Scene(group, XMAX + 150, YMAX); //cely obray (hraci plocha i score)
    public static int score = 0;
    private static int top = 0;
    private static boolean playing = false; //hra bezi
    public static Form nextObj = Controller.makeRect();
    private static boolean threadIsRunning = false;
    public static String name;
    public static int users = 0;
    private static String musicFile = "Classic";
    private static Media backgroundMusic = new Media(Paths.get(musicFile+".mp3").toUri().toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(backgroundMusic);
    private static Color textColor = Color.WHITE;

    //hotkeys
    private static String UPcase = "W"; //UP
    private static String RIGHTcase = "D"; //RIGHT
    private static String LEFTcase = "A"; //LEFT
    private static String DOWNcase = "S"; //DOWN
    private static String pressedButton;

    //score stuff
    public static int[] savedScore_numbers = new int[256];
    public static String[] savedScore_names = new String[256];
    public static String name1 = "";
    public static String name2 = "";
    public static String name3 = "";
    public static String name4 = "";
    public static String name5 = "";
    public static String score1 = "";
    public static String score2 = "";
    public static String score3 = "";
    public static String score4 = "";
    public static String score5 = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ScoreRecording.remember();
        setBgImage("bgDark.png");
        menu(stage);
    }

    private void setBgImage(String bgImage) {
        Image image = new Image(getClass().getResource(bgImage).toExternalForm());
        BackgroundImage background = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        group.setBackground(new Background(background));
    }

    private void menu(Stage stage) {
        Button exitButton = new Button("X");
        Button button1 = new Button();
        Button button2 = new Button();
        Button button3 = new Button();
        TextField textField = new TextField ();

        //score stuff {
        Rectangle scoreboard = new Rectangle(50,110,350,500);
        Text topText = new Text("YOUR TOP 5 RECORDS:\n\n\t Name \t    Score");
        Text top1a = new Text("1." + "\t" + name1);
        Text top1b = new Text(score1);
        Text top2a = new Text("2." + "\t" + name2);
        Text top2b = new Text(score2);
        Text top3a = new Text("3." + "\t" + name3);
        Text top3b = new Text(score3);
        Text top4a = new Text("4." + "\t" + name4);
        Text top4b = new Text(score4);
        Text top5a = new Text("5." + "\t" + name5);
        Text top5b = new Text(score5);
        Button backToMenu = new Button("↩");
        //}


        exitButton.setTranslateX(XMAX + 75);
        exitButton.setTranslateY(0);
        exitButton.setStyle("-fx-font-size:40");
        exitButton.setMinSize(25,25);

        textField.setPromptText("Your name");
        textField.setTranslateX(XMAX/2);
        textField.setTranslateY(YMAX/2 - 160);
        textField.setMinSize(150,50);

        button1.setText("PLAY");
        button1.setTranslateX(XMAX/2);
        button1.setTranslateY(YMAX/2 - 105);
        button1.setMinSize(150,100);
        button1.setStyle("-fx-border-color:cyan; -fx-border-width: 3 3 3 3;");

        button2.setText("Change settings");
        button2.setTranslateX(XMAX/2);
        button2.setTranslateY(YMAX/2);
        button2.setMinSize(150,50);

        button3.setText("View scoreboard");
        button3.setTranslateX(XMAX/2);
        button3.setTranslateY(YMAX/2 + 55);
        button3.setMinSize(150,50);

        scoreboard.setFill(Color.rgb(250,250,250));
        scoreboard.setStroke(Color.RED);
        scoreboard.setStrokeWidth(5);

        topText.setStyle("-fx-font-size:30");
        topText.setTranslateX(XMAX/2 - 80);
        topText.setTranslateY(200);

        top1a.setStyle("-fx-font-size:30");
        top1a.setTranslateX(XMAX/2 - 80);
        top1a.setTranslateY(350);
        top1b.setStyle("-fx-font-size:30;");
        top1b.setTranslateX(XMAX/2 + 170);
        top1b.setTranslateY(350);


        top2a.setStyle("-fx-font-size:30");
        top2a.setTranslateX(XMAX/2 - 80);
        top2a.setTranslateY(400);
        top2b.setStyle("-fx-font-size:30");
        top2b.setTranslateX(XMAX/2 + 170);
        top2b.setTranslateY(400);

        top3a.setStyle("-fx-font-size:30");
        top3a.setTranslateX(XMAX/2 - 80);
        top3a.setTranslateY(450);
        top3b.setStyle("-fx-font-size:30");
        top3b.setTranslateX(XMAX/2 + 170);
        top3b.setTranslateY(450);

        top4a.setStyle("-fx-font-size:30");
        top4a.setTranslateX(XMAX/2 - 80);
        top4a.setTranslateY(500);
        top4b.setStyle("-fx-font-size:30");
        top4b.setTranslateX(XMAX/2 + 170);
        top4b.setTranslateY(500);

        top5a.setStyle("-fx-font-size:30");
        top5a.setTranslateX(XMAX/2 - 80);
        top5a.setTranslateY(550);
        top5b.setStyle("-fx-font-size:30");
        top5b.setTranslateX(XMAX/2 + 170);
        top5b.setTranslateY(550);

        backToMenu.setTranslateX(355);
        backToMenu.setTranslateY(115);
        backToMenu.setStyle("-fx-font-size:20");
        backToMenu.setMaxSize(10,10);

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                name = textField.getText();
                if (name.equals("")) {
                    name = "Guest";
                }
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

        button3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                ScoreRecording.read();
                top1a.setText("1." + "\t" + name1);
                top1b.setText(score1);
                top2a.setText("2." + "\t" + name2);
                top2b.setText(score2);
                top3a.setText("3." + "\t" + name3);
                top3b.setText(score3);
                top4a.setText("4." + "\t" + name4);
                top4b.setText(score4);
                top5a.setText("5." + "\t" + name5);
                top5b.setText(score5);
                group.getChildren().addAll(scoreboard, topText, top1a, top1b, top2a, top2b, top3a, top3b, top4a, top4b, top5a, top5b, backToMenu);
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.exit(0);
            }
        });

        backToMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                group.getChildren().clear();
                menu(stage);
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
                if (!RIGHTcase.equals(hotkey.toUpperCase()) && !LEFTcase.equals(hotkey.toUpperCase()) && !DOWNcase.equals(hotkey.toUpperCase())) {
                    UPcase = hotkey.toUpperCase();
                }
                break;
            case "right":
                if (!UPcase.equals(hotkey.toUpperCase()) && !LEFTcase.equals(hotkey.toUpperCase()) && !DOWNcase.equals(hotkey.toUpperCase())) {
                    RIGHTcase = hotkey.toUpperCase();
                }
                break;
            case "left":
                if (!RIGHTcase.equals(hotkey.toUpperCase()) && !UPcase.equals(hotkey.toUpperCase()) && !DOWNcase.equals(hotkey.toUpperCase())) {
                    LEFTcase = hotkey.toUpperCase();
                }
                break;
            case "down":
                if (!RIGHTcase.equals(hotkey.toUpperCase()) && !LEFTcase.equals(hotkey.toUpperCase()) && !UPcase.equals(hotkey.toUpperCase())) {
                    DOWNcase = hotkey.toUpperCase();
                }
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

        Text selectMusic = new Text("Select music: ");
        ObservableList<String> music_options = FXCollections.observableArrayList("Classic", "Techno", "Halloween");
        final ComboBox music_changer = new ComboBox(music_options);

        RadioButton rb1 = new RadioButton("Light mode");
        RadioButton rb2 = new RadioButton("Dark mode");


        selectMusic.setStyle("-fx-font: 20 arial;");
        selectMusic.setX(XMAX/2 - 80);
        selectMusic.setY(YMAX/2 - 130);
        selectMusic.setFill(textColor);

        music_changer.setTranslateX(XMAX/2 + 50);
        music_changer.setTranslateY(YMAX/2 - 150);
        music_changer.setValue(musicFile);

        rb1.setTranslateX(XMAX/2 + 100);
        rb1.setTranslateY(50);
        rb1.setStyle("-fx-font: 10 arial;");
        rb1.setTextFill(textColor);
        if(textColor == Color.BLACK) rb1.setSelected(true);

        rb2.setTranslateX(XMAX/2 + 100);
        rb2.setTranslateY(75);
        rb2.setStyle("-fx-font: 10 arial;");
        rb2.setTextFill(textColor);
        if(textColor == Color.WHITE) rb2.setSelected(true);

        controlChange.setTranslateX(XMAX/2);
        controlChange.setTranslateY(YMAX/2 + 100);
        controlChange.setMinHeight(55);
        controlChange.setMaxWidth(60);
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
        buttonUP.setTranslateY(YMAX/2 - 32);
        buttonUP.setMinSize(57,50);

        buttonRIGHT.setText(rightText);
        buttonRIGHT.setStyle("-fx-font-size:25");
        buttonRIGHT.setTranslateX(XMAX/2 + 65);
        buttonRIGHT.setTranslateY(YMAX/2 + 30);
        buttonRIGHT.setMinSize(57,50);

        buttonLEFT.setText(leftText);
        buttonLEFT.setStyle("-fx-font-size:25");
        buttonLEFT.setTranslateX(XMAX/2 - 65);
        buttonLEFT.setTranslateY(YMAX/2 + 30);
        buttonLEFT.setMinSize(57,50);

        buttonDOWN.setText(downText);
        buttonDOWN.setStyle("-fx-font-size:25");
        buttonDOWN.setTranslateX(XMAX/2);
        buttonDOWN.setTranslateY(YMAX/2 + 30);
        buttonDOWN.setMinSize(57,50);

        rb1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                rb2.setSelected(false);
                textColor = Color.BLACK;
                rb1.setTextFill(textColor);
                rb2.setTextFill(textColor);
                setBgImage("bgLight.png");
                selectMusic.setFill(textColor);
            }
        });

        rb2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                rb1.setSelected(false);
                textColor = Color.WHITE;
                rb1.setTextFill(textColor);
                rb2.setTextFill(textColor);
                setBgImage("bgDark.png");
                selectMusic.setFill(textColor);
            }
        });

        buttonBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                group.getChildren().clear();
                menu(stage);
                musicFile = (String) music_changer.getValue();
            }
        });

        buttonUP.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "up";
                controlChange.setTranslateX(XMAX/2);
                controlChange.setTranslateY(YMAX/2 - 32);
                group.getChildren().add(controlChange);
            }
        });

        buttonRIGHT.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "right";
                controlChange.setTranslateX(XMAX/2 + 65);
                controlChange.setTranslateY(YMAX/2 + 30);
                group.getChildren().add(controlChange);
            }
        });

        buttonLEFT.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "left";
                controlChange.setTranslateX(XMAX/2 - 65);
                controlChange.setTranslateY(YMAX/2 + 30);
                group.getChildren().add(controlChange);
            }
        });

        buttonDOWN.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                pressedButton = "down";
                controlChange.setTranslateX(XMAX/2);
                controlChange.setTranslateY(YMAX/2 + 30);
                group.getChildren().add(controlChange);
            }
        });

        stage.setScene(scene);
        group.getChildren().addAll(buttonUP, buttonRIGHT, buttonLEFT, buttonDOWN, buttonBack, selectMusic, music_changer, rb1, rb2);
        stage.show();
    }

    private void game(Stage stage) {

        score = 0;
        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }

        Line line = new Line(XMAX, 0, XMAX, YMAX);
        Text scoretext = new Text("Score: ");
        Rectangle stoppedInterface = new Rectangle(50,110,350,500);
        Button stopButton = new Button("| |");
        Button resumeButton = new Button("▶");
        Button menuButton = new Button("Save score \n and \n return to main menu");

        if (textColor == Color.WHITE) {
            line.setStyle("-fx-stroke: white;");
        }
        else if (textColor == Color.BLACK) {
            line.setStyle("-fx-stroke: black;");
        }
        else {
            line.setStyle("-fx-stroke: red;");
        }

        scoretext.setStyle("-fx-font: 20 arial;");
        scoretext.setY(50);
        scoretext.setX(XMAX + 5);
        scoretext.setFill(textColor);

        stoppedInterface.setFill(Color.rgb(250,250,250, 0.5));
        stoppedInterface.setStroke(Color.CYAN);
        stoppedInterface.setStrokeWidth(5);

        stopButton.setStyle("-fx-font: 40 arial;");
        stopButton.setTranslateX(XMAX + 30);
        stopButton.setTranslateY(500);
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
                //zapis do txt dokumentu zacina zde
                savedScore_numbers[users] = score;
                savedScore_names[users] = name;
                ScoreRecording.recordScore();
                if (users != 255) {
                    users++;
                }
                top = 0;
                group.getChildren().clear();
                stopMusic();
                menu(stage);
            }
        });

        group.getChildren().addAll(scoretext, line, stopButton);
        startMusic();

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
                            scoretext.setFill(textColor);
                            scoretext.setText("Score: " + Integer.toString(score));
                        }
                    }
                });
            }
        };
        //repeating task in a set period
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

    private static void startMusic() {
        backgroundMusic = new Media(Paths.get(musicFile+".mp3").toUri().toString());
        mediaPlayer = new MediaPlayer(backgroundMusic);
        mediaPlayer.setVolume(0.03);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    private static void stopMusic() {
        mediaPlayer.stop();
    }

}