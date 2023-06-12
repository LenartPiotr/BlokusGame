package lenart.piotr.blokus.console;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {
    public static int select(String[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ": " + options[i]);
        }
        System.out.print("> ");
        Scanner s = new Scanner(System.in);
        int x;
        try {
            x = s.nextInt() - 1;
        }catch (InputMismatchException e) {
            x = 0;
        }
        if (x < 0 || x >= options.length) x = 0;
        return x;
    }

    public static int getInt(String text) {
        Scanner s = new Scanner(System.in);
        System.out.println(text);
        System.out.print("> ");
        int x;
        try {
            x = s.nextInt() - 1;
        }catch (InputMismatchException e) {
            x = 0;
        }
        return x;
    }

    public static String getText(String text) {
        Scanner s = new Scanner(System.in);
        System.out.println(text);
        System.out.print("> ");
        return s.nextLine();
    }
}
