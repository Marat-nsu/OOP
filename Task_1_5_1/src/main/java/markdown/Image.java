package markdown;

public class Image extends Link {

    public Image(String altText, String url) {
        super(altText, url);
    }

    public Image(Element altText, String url) {
        super(altText, url);
    }

    public Image(String altText, String url, String title) {
        super(altText, url, title);
    }

    public Image(Element altText, String url, String title) {
        super(altText, url, title);
    }

    @Override
    public String toString() {
        return "!" + super.toString();
    }
}
