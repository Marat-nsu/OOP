package markdown;

import java.util.Objects;
import markdown.text.Text;


public class Task extends Element {
    private final Element content;
    private final boolean checked;

    public Task(String text, boolean checked) {
        this(new Text(text), checked);
    }

    public Task(Element content, boolean checked) {
        this.content = content;
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "- [" + (checked ? "x" : " ") + "] " + content.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return checked == task.checked && Objects.equals(content, task.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, checked);
    }
}
