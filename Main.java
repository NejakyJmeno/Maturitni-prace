package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    public static int score = 0; //vyska skore
    private static int top = 0;
    private static boolean game = true; //hra bezi
    public static Form nextObj = Controller.makeRect();
    public static int linesNo = 0; //pocet linek
    private static int timeProgress = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }

        Line line = new Line(XMAX, 0, XMAX, YMAX);
        Text scoretext = new Text("Score: ");
        scoretext.setStyle("-fx-font: 20 arial;");
        scoretext.setY(50);
        scoretext.setX(XMAX + 5);
        Text level = new Text("Lines: ");
        level.setStyle("-fx-font: 20 arial;");
        level.setY(100);
        level.setX(XMAX + 5);
        level.setFill(Color.GREEN);
        group.getChildren().addAll(scoretext, line, level);

        Form a = nextObj;
        group.getChildren().addAll(a.a, a.b, a.c, a.d);
        moveOnKeyPress(a);
        object = a;
        nextObj = Controller.makeRect();
        stage.setScene(scene);
        stage.setTitle("T E T R I S");
        stage.show();

        Timer fall = new Timer();
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
                            game = false;
                        }
                        // Exit
                        if (top == 15) {
                            System.exit(0);
                        }

                        if (game) {
                            Moving.MoveDown(object);
                            scoretext.setText("Score: " + Integer.toString(score));
                            level.setText("Lines: " + Integer.toString(linesNo));
                        }
                    }
                });
            }
        };
        fall.schedule(task, 0, 300-timeProgress);
        timeProgress += 1000;
    }

    public static void moveOnKeyPress(Form form) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event);
                switch (event.getCode()) {
                    case UP:
                        Moving.MoveTurn(form);
                        break;
                    case RIGHT:
                        Moving.MoveRight(form);
                        break;
                    case LEFT:
                        Moving.MoveLeft(form);
                        break;
                    case DOWN:
                        Moving.MoveDown(form);
                        score++;
                        break;
                    /*case SPACE:
                        MoveDrop(form);
                        break; */
                }
            }
        });
    }

}