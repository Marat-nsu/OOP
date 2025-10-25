class ExpressionParser {
    private String input;
    private int pos;

    public ExpressionParser(String input) {
        this.input = input.replaceAll("\\s+", "");
        this.pos = 0;
    }

    // Добавить модификатор доступа
    Expression parse() {
        return parseExpression();

    }

    private Expression parseExpression() {
        if (input.charAt(pos) == '(') {
            pos++;
            Expression left = parseExpression();
            char op = input.charAt(pos);
            pos++;
            Expression right = parseExpression();
            // Скипаем закрывающую скобку
            pos++;
            switch (op) {
                case '+':
                    return new Add(left, right);
                case '-':
                    return new Sub(left, right);
                case '*':
                    return new Mul(left, right);
                case '/':
                    return new Div(left, right);
                default:
                    throw new IllegalArgumentException("Unknown operator: " + op);
            }
        } else if (Character.isDigit(input.charAt(pos))) {
            return parseNumber();
        } else {
            return parseVariable();
        }
    }

    private Expression parseNumber() {
        int start = pos;
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            pos++;
        }
        return new Number(Integer.parseInt(input.substring(start, pos)));
    }

    private Expression parseVariable() {
        int start = pos;
        while (pos < input.length()
                && (Character.isLetter(input.charAt(pos)) || input.charAt(pos) == '_')) {
            pos++;
        }
        return new Variable(input.substring(start, pos));
    }
}
