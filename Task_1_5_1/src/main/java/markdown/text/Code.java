package markdown.text;

import java.util.Objects;
import markdown.Element;

public class Code extends Element {
    private final String content;

    public Code(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "`" + content + "`";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Code code = (Code) o;
        return Objects.equals(content, code.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
