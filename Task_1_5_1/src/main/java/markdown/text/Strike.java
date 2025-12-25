package markdown.text;

import java.util.Objects;
import markdown.Element;

public class Strike extends Element {
    private final Element content;

    public Strike(String text) {
        this.content = new Text(text);
    }

    public Strike(Element content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "~~" + content.toString() + "~~";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Strike strike = (Strike) o;
        return Objects.equals(content, strike.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
