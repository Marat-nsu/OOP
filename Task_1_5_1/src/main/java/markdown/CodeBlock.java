package markdown;

import java.util.Objects;

public class CodeBlock extends Element {
    private final String code;
    private final String language;

    public CodeBlock(String code) {
        this(code, "");
    }

    public CodeBlock(String code, String language) {
        this.code = code;
        this.language = language == null ? "" : language;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("```").append(language).append("\n");
        sb.append(code);
        if (!code.endsWith("\n")) {
            sb.append("\n");
        }
        sb.append("```");
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
        CodeBlock codeBlock = (CodeBlock) o;
        return Objects.equals(code, codeBlock.code) 
            && Objects.equals(language, codeBlock.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, language);
    }
}
