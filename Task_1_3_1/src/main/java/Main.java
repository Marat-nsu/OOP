import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Integer> res = SubstringSearcher.find("input.txt", "бра");
            System.out.println(res);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
