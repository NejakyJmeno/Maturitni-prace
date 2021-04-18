package sample;

import java.io.*;
import java.util.ArrayList;

public class ScoreRecording {

    public static void recordScore() {

        //dont sort only 1 score
        if (Main.users != 0) {
            try {
                Sort();
            }
            catch (Exception e) {
                System.out.println(e + "\n Skipping the problem");
            }
        }

        try {
            FileWriter myWriter = new FileWriter("scoredatabase.txt");
            for (int i = 0; i<Main.savedScore_numbers.length; i++) {
                if (Main.savedScore_names[i] != null) {
                    myWriter.write(String.valueOf(Main.savedScore_numbers[i]));
                    myWriter.write("\n");
                }
            }
            myWriter.close();
            System.out.println("Recording of your score was successful.");
        } catch (IOException e) {
            System.out.println("Couldn't record your score.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("namedatabase.txt");
            for (int i = 0; i<Main.savedScore_names.length; i++) {
                if (Main.savedScore_names[i] != null) {
                    myWriter.write(Main.savedScore_names[i]);
                    myWriter.write("\n");
                }
            }
            myWriter.close();
            System.out.println("Recording of your name was successful.");
        } catch (IOException e) {
            System.out.println("Couldn't record your name.");
            e.printStackTrace();
        }
    }

    private static void Sort() {
        System.out.println("Sorting started");
        int helper = 0;
        int bottom = 0;
        for (int i=0; i<5; i++) { //only top 5 needed
            for (int j=Main.users; j > 0+bottom; j--) {
                System.out.println("one mini cycle started");
                if (Main.savedScore_numbers[j] > Main.savedScore_numbers[j - 1]) {
                    helper = Main.savedScore_numbers[j];
                    Main.savedScore_numbers[j] = Main.savedScore_numbers[j - 1];
                    Main.savedScore_numbers[j - 1] = helper;
                    nameSwap(j);
                }
            }
            bottom++;
            if (i+1 > Main.users) {
                break;
            }
        }
    }

    private static void nameSwap(int index){
        String temp;

        temp = Main.savedScore_names[index];
        Main.savedScore_names[index] = Main.savedScore_names[index-1];
        Main.savedScore_names[index-1] = temp;
    }

    public static void remember(){
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("namedatabase.txt"));
            String line = reader.readLine();
            int index = 0;
            while (line != null) {
                Main.savedScore_names[index] = line;
                index++;
                if (index == 256) {
                    break;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader = new BufferedReader(new FileReader("scoredatabase.txt"));
            String line = reader.readLine();
            int index = 0;
            while (line != null) {
                Main.savedScore_numbers[index] = Integer.parseInt(line);
                index++;
                if (index == 256) {
                    break;
                }
                line = reader.readLine();
            }
            reader.close();
            Main.users = index;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(){
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("namedatabase.txt"));

            String line = reader.readLine();
            if (line != null) {
                Main.name1 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.name2 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.name3 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.name4 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.name5 = line;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            reader = new BufferedReader(new FileReader("scoredatabase.txt"));

            String line = reader.readLine();
            if (line != null) {
                Main.score1 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.score2 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.score3 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.score4 = line;
            }

            line = reader.readLine();
            if (line != null) {
                Main.score5 = line;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
