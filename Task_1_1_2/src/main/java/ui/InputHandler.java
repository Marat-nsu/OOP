package ui;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputHandler implements AutoCloseable {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public int readInt(int min, int max) {
        try {
            if (!scanner.hasNextInt()) {
                scanner.next();
                throw new IllegalArgumentException("Invalid number input");
            }
            int value = scanner.nextInt();
            if (value < min || value > max) {
                throw new IllegalArgumentException("Value out of range: " + min + " to " + max);
            }
            return value;
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("No input available", e);
        }
    }

    public int readInt() {
        return readInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public String readString() {
        scanner.nextLine();
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
