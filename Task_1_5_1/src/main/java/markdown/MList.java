package markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import markdown.text.Text;


public class MList extends Element {
    private final List<Element> items;
    private final boolean ordered;

    private MList(List<Element> items, boolean ordered) {
        this.items = items;
        this.ordered = ordered;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (ordered) {
                sb.append(i + 1).append(". ");
            } else {
                sb.append("- ");
            }
            sb.append(items.get(i).toString());
            if (i < items.size() - 1) {
                sb.append("\n");
            }
        }
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
        MList mList = (MList) o;
        return ordered == mList.ordered && Objects.equals(items, mList.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, ordered);
    }

    public static class Builder {
        private final List<Element> items = new ArrayList<>();
        private boolean ordered = false;

        public Builder ordered(boolean ordered) {
            this.ordered = ordered;
            return this;
        }

        public Builder addItem(Element item) {
            this.items.add(item);
            return this;
        }

        public Builder addItem(String text) {
            this.items.add(new Text(text));
            return this;
        }

        public MList build() {
            return new MList(new ArrayList<>(items), ordered);
        }
    }
}
