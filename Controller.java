package sample;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class Controller {


    public static Form makeRect() {
        int shape = (int) (Math.random() * 100);
        String name;
        Rectangle
                a = new Rectangle(Main.SIZE-1, Main.SIZE-1),
                b = new Rectangle(Main.SIZE-1, Main.SIZE-1),
                c = new Rectangle(Main.SIZE-1, Main.SIZE-1),
                d = new Rectangle(Main.SIZE-1, Main.SIZE-1);
        if (shape < 15) {
            a.setX(Main.XMAX / 2 - Main.SIZE);
            b.setX(Main.XMAX / 2 - Main.SIZE);
            b.setY(Main.SIZE);
            c.setX(Main.XMAX / 2);
            c.setY(Main.SIZE);
            d.setX(Main.XMAX / 2 + Main.SIZE);
            d.setY(Main.SIZE);
            name = "j";
        } else if (shape < 30) {
            a.setX(Main.XMAX / 2 + Main.SIZE);
            b.setX(Main.XMAX / 2 - Main.SIZE);
            b.setY(Main.SIZE);
            c.setX(Main.XMAX / 2);
            c.setY(Main.SIZE);
            d.setX(Main.XMAX / 2 + Main.SIZE);
            d.setY(Main.SIZE);
            name = "l";
        } else if (shape < 45) {
            a.setX(Main.XMAX / 2 - Main.SIZE);
            b.setX(Main.XMAX / 2);
            c.setX(Main.XMAX / 2 - Main.SIZE);
            c.setY(Main.SIZE);
            d.setX(Main.XMAX / 2);
            d.setY(Main.SIZE);
            name = "o";
        } else if (shape < 60) {
            a.setX(Main.XMAX / 2 + Main.SIZE);
            b.setX(Main.XMAX / 2);
            c.setX(Main.XMAX / 2);
            c.setY(Main.SIZE);
            d.setX(Main.XMAX / 2 - Main.SIZE);
            d.setY(Main.SIZE);
            name = "s";
        } else if (shape < 75) {
            a.setX(Main.XMAX / 2 - Main.SIZE);
            b.setX(Main.XMAX / 2);
            c.setX(Main.XMAX / 2);
            c.setY(Main.SIZE);
            d.setX(Main.XMAX / 2 + Main.SIZE);
            name = "t";
        } else if (shape < 90) {
            a.setX(Main.XMAX / 2 + Main.SIZE);
            b.setX(Main.XMAX / 2);
            c.setX(Main.XMAX / 2 + Main.SIZE);
            c.setY(Main.SIZE);
            d.setX(Main.XMAX / 2 + Main.SIZE + Main.SIZE);
            d.setY(Main.SIZE);
            name = "z";
        } else {
            a.setX(Main.XMAX / 2 - Main.SIZE - Main.SIZE);
            b.setX(Main.XMAX / 2 - Main.SIZE);
            c.setX(Main.XMAX / 2);
            d.setX(Main.XMAX / 2 + Main.SIZE);
            name = "i";
        }
        return new Form(a, b, c, d, name);
    }

    public static void RemoveRows(Pane pane) {
        ArrayList<Node> rects = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newrects = new ArrayList<Node>();
        int full = 0;
        for (int i = 0; i < Main.MESH[0].length; i++) {
            for (int j = 0; j < Main.MESH.length; j++) {
                if (Main.MESH[j][i] == 1)
                    full++;
            }
            if (full == Main.MESH.length)
                lines.add(i);
            full = 0;
        }
        if (lines.size() > 0)
            do {
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                Main.score += 100;

                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() == lines.get(0) * Main.SIZE) {
                        Main.MESH[(int) a.getX() / Main.SIZE][(int) a.getY() / Main.SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newrects.add(node);
                }

                for (Node node : newrects) {
                    Rectangle a = (Rectangle) node;
                    if (a.getY() < lines.get(0) * Main.SIZE) {
                        Main.MESH[(int) a.getX() / Main.SIZE][(int) a.getY() / Main.SIZE] = 0;
                        a.setY(a.getY() + Main.SIZE);
                    }
                }
                lines.remove(0);
                rects.clear();
                newrects.clear();
                for (Node node : pane.getChildren()) {
                    if (node instanceof Rectangle)
                        rects.add(node);
                }
                for (Node node : rects) {
                    Rectangle a = (Rectangle) node;
                    try {
                        Main.MESH[(int) a.getX() / Main.SIZE][(int) a.getY() / Main.SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
                rects.clear();
            } while (lines.size() > 0);
    }
}