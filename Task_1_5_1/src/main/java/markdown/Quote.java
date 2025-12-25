package markdown;

import java.util.Objects;
import markdown.text.Text;


public class Quote extends Element {
    private final Element content;

    public Quote(String text) {
        this(new Text(text));
    }

    public Quote(Element content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "> " + content.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quote quote = (Quote) o;
        return Objects.equals(content, quote.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
