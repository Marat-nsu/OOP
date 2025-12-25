package markdown;

import java.util.Objects;
import markdown.text.Text;


public class Heading extends Element {
    private final int level;
    private final Element content;

    public Heading(int level, String text) {
        this(level, new Text(text));
    }

    public Heading(int level, Element content) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be between 1 and 6");
        }
        this.level = level;
        this.content = content;
    }

    @Override
    public String toString() {
        return "#".repeat(level) + " " + content.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Heading heading = (Heading) o;
        return level == heading.level && Objects.equals(content, heading.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, content);
    }
}
