package markdown.text;

import java.util.Objects;
import markdown.Element;

public class Italic extends Element {
    private final Element content;

    public Italic(String text) {
        this.content = new Text(text);
    }

    public Italic(Element content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "*" + content.toString() + "*";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Italic italic = (Italic) o;
        return Objects.equals(content, italic.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
