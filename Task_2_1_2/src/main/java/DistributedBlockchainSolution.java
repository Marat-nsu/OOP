import node.BlockchainNode;

public class DistributedBlockchainSolution implements Solution {
    @Override
    public boolean containsNonPrime(int[] numbers) {
        BlockchainNode node = new BlockchainNode("local-solver", -1, numbers);
        node.mineUntilFinished();
        return node.getBlockchain().containsNonPrime();
    }
}
