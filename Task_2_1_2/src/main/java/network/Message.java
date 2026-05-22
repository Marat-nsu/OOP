package network;

import lombok.Value;

@Value
public class Message {
    MessageType type;
    String payload;

    public static Message block(String serializedBlock) {
        return new Message(MessageType.BLOCK, serializedBlock);
    }

    public String serialize() {
        return type + " " + payload;
    }

    public static Message parse(String line) {
        int separator = line.indexOf(' ');
        if (separator < 0) {
            return new Message(MessageType.valueOf(line), "");
        }
        return new Message(
            MessageType.valueOf(line.substring(0, separator)),
            line.substring(separator + 1)
        );
    }
}
