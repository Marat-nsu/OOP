package markdown.text;

import java.util.Objects;
import markdown.Element;

public class Bold extends Element {
    private final Element content;

    public Bold(String text) {
        this.content = new Text(text);
    }

    public Bold(Element content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "**" + content.toString() + "**";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bold bold = (Bold) o;
        return Objects.equals(content, bold.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
