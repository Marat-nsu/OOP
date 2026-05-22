import java.util.Arrays;
import node.BlockchainNode;

public class Main {
    public static void main(String[] args) throws Exception {
        int[] numbers = args.length == 0
            ? new int[] {6, 8, 7, 13, 5, 9, 4}
            : Arrays.stream(args[0].split(",")).mapToInt(Integer::parseInt).toArray();

        BlockchainNode node = new BlockchainNode("demo", -1, numbers);
        node.mineUntilFinished();
        System.out.println(node.getBlockchain().containsNonPrime());
    }
}
