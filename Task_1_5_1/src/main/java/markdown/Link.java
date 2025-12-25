package markdown;

import java.util.Objects;
import markdown.text.Text;

public class Link extends Element {
    private final Element text;
    private final String url;
    private final String title;

    public Link(String text, String url) {
        this(new Text(text), url, null);
    }

    public Link(Element text, String url) {
        this(text, url, null);
    }

    public Link(String text, String url, String title) {
        this(new Text(text), url, title);
    }

    public Link(Element text, String url, String title) {
        this.text = text;
        this.url = url;
        this.title = title;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(text.toString()).append("](").append(url);
        if (title != null && !title.isEmpty()) {
            sb.append(" \"").append(title).append("\"");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(text, link.text)
            && Objects.equals(url, link.url)
            && Objects.equals(title, link.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, url, title);
    }
}
