package markdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Table extends Element {
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER = 2;

    private final List<List<String>> rows;
    private final List<Integer> alignments;

    private Table(List<List<String>> rows, List<Integer> alignments) {
        this.rows = rows;
        this.alignments = alignments;
    }

    @Override
    public String toString() {
        if (rows.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int columns = rows.get(0).size();
        int[] colWidths = new int[columns];

        for (List<String> row : rows) {
            for (int i = 0; i < Math.min(row.size(), columns); i++) {
                colWidths[i] = Math.max(colWidths[i], row.get(i).length());
            }
        }

        for (int i = 0; i < columns; i++) {
             colWidths[i] = Math.max(colWidths[i], 3);
        }


        appendRow(sb, rows.get(0), colWidths);
        sb.append("\n");

        sb.append("|");
        for (int i = 0; i < columns; i++) {
            sb.append(" ");
            int align = (i < alignments.size()) ? alignments.get(i) : ALIGN_LEFT;
            int width = colWidths[i];
            if (align == ALIGN_RIGHT) {
                sb.append("-".repeat(width - 1)).append(":");
            } else if (align == ALIGN_CENTER) {
                sb.append(":").append("-".repeat(width - 2)).append(":");
            } else {
                sb.append("-".repeat(width));
            }
            sb.append(" |");
        }
        sb.append("\n");

        for (int i = 1; i < rows.size(); i++) {
            appendRow(sb, rows.get(i), colWidths);
            if (i < rows.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private void appendRow(StringBuilder sb, List<String> row, int[] colWidths) {
        sb.append("|");
        for (int i = 0; i < colWidths.length; i++) {
            sb.append(" ");
            String cell = (i < row.size()) ? row.get(i) : "";
            sb.append(cell);
            sb.append(" ".repeat(colWidths[i] - cell.length()));
            sb.append(" |");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Table table = (Table) o;
        return Objects.equals(rows, table.rows) &&
                Objects.equals(alignments, table.alignments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, alignments);
    }

    public static class Builder {
        private final List<List<String>> rows = new ArrayList<>();
        private List<Integer> alignments = new ArrayList<>();
        private int rowLimit = Integer.MAX_VALUE;

        public Builder withAlignments(int... alignments) {
            this.alignments = Arrays.stream(alignments).boxed().collect(Collectors.toList());
            return this;
        }

        public Builder withRowLimit(int rowLimit) {
            this.rowLimit = rowLimit;
            return this;
        }

        public Builder addRow(Object... cells) {
            if (rows.size() >= rowLimit) {
                return this;
            }
            List<String> row = new ArrayList<>();
            for (Object cell : cells) {
                if (cell instanceof Element) {
                    row.add(((Element) cell).toString());
                } else {
                    row.add(String.valueOf(cell));
                }
            }
            rows.add(row);
            return this;
        }

        public Table build() {
            return new Table(new ArrayList<>(rows), new ArrayList<>(alignments));
        }
    }
}
