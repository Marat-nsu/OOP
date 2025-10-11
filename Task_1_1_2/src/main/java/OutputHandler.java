public class OutputHandler {

    public OutputHandler() {
    }

    public void print(String text) {
        System.out.print(text);
    }

    public void println(String text) {
        System.out.println(text);
    }

    public void println(String text, Object... args) {
        String message = String.format(text, args);
        System.out.println(message);
    }
}
