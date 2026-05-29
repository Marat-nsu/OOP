package storage;

import blockchain.Block;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockStorage {
    private final Path file;

    public synchronized void append(Block block) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(block.serialize());
            writer.newLine();
        }
    }

    public synchronized List<Block> readAll() throws IOException {
        if (!Files.exists(file)) {
            return List.of();
        }
        List<Block> blocks = new ArrayList<>();
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (!line.isBlank()) {
                blocks.add(Block.parse(line));
            }
        }
        return blocks;
    }
}
