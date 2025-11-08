import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class ExpressionTest {

    // Утилита для захвата вывода из консоли
    private String captureOutput(Runnable task) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        task.run();
        System.out.flush();
        PrintStream old = System.out;
        System.setOut(old);
        return baos.toString().trim();
    }

    @Test
    void testNumber() throws ExpressionException {
        Expression number = new Number(5);
        assertEquals("5", number.toString());
        assertEquals(5, number.eval("x=10"));
        assertEquals("0", number.derivative("x").toString());
        assertEquals("5", captureOutput(number::print));
    }

    @Test
    void testVariable() throws ExpressionException {
        Expression var = new Variable("x");
        assertEquals("x", var.toString());
        assertEquals("x", captureOutput(var::print));
        assertEquals(10, var.eval("x=10;y=20"));
        assertEquals(0, var.eval("y=20"));
        assertEquals("1", var.derivative("x").toString());
        assertEquals("0", var.derivative("y").toString());
    }

    @Test
    void testAdd() throws ExpressionException {
        Expression add = new Add(new Number(3), new Variable("x"));
        assertEquals("(3+x)", add.toString());
        assertEquals("(3+x)", captureOutput(add::print));
        assertEquals(13, add.eval("x=10"));
        assertEquals("(0+1)", add.derivative("x").toString());
        assertEquals("(0+0)", add.derivative("y").toString());
    }

    @Test
    void testSub() throws ExpressionException {
        Expression sub = new Sub(new Number(5), new Variable("x"));
        assertEquals("(5-x)", sub.toString());
        assertEquals("(5-x)", captureOutput(sub::print));
        assertEquals(-5, sub.eval("x=10"));
        assertEquals("(0-1)", sub.derivative("x").toString());
        assertEquals("(0-0)", sub.derivative("y").toString());
    }

    @Test
    void testMul() throws ExpressionException {
        Expression mul = new Mul(new Number(2), new Variable("x"));
        assertEquals("(2*x)", mul.toString());
        assertEquals("(2*x)", captureOutput(mul::print));
        assertEquals(20, mul.eval("x=10"));
        Expression deriv = mul.derivative("x");
        assertEquals("((0*x)+(2*1))", deriv.toString());
        assertEquals(2, deriv.eval("x=10"));
    }

    @Test
    void testDiv() throws ExpressionException {
        Expression div = new Div(new Variable("x"), new Number(2));
        assertEquals("(x/2)", div.toString());
        assertEquals("(x/2)", captureOutput(div::print));
        assertEquals(5, div.eval("x=10"));
        Expression deriv = div.derivative("x");
        assertEquals("(((1*2)-(x*0))/(2*2))", deriv.toString());
        assertEquals(0, div.derivative("y").eval("x=10"));
        assertThrows(ExpressionException.class,
                () -> new Div(new Number(1), new Number(0)).eval(""));
    }

    @Test
    void testExpressionParser() throws ExpressionException {
        ExpressionParser parser = new ExpressionParser("(3+(2*x))");
        Expression expr = parser.parse();
        assertEquals("(3+(2*x))", expr.toString());
        assertEquals("(3+(2*x))", captureOutput(expr::print));
        assertEquals(23, expr.eval("x=10;y=13"));
        Expression deriv = expr.derivative("x");
        assertEquals("(0+((0*x)+(2*1)))", deriv.toString());
        assertEquals(2, deriv.eval("x=10"));

        parser = new ExpressionParser("((5*x)/y)");
        expr = parser.parse();
        assertEquals("((5*x)/y)", expr.toString());
        assertEquals(25, expr.eval("x=10;y=2"));
        assertEquals("(((((0*x)+(5*1))*y)-((5*x)*0))/(y*y))", expr.derivative("x").toString());
    }

    @Test
    void testComplexExpression() throws ExpressionException {
        Expression expr = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        assertEquals("(3+(2*x))", expr.toString());
        assertEquals(23, expr.eval("x=10;y=13"));
        assertEquals("(0+((0*x)+(2*1)))", expr.derivative("x").toString());
        assertEquals("(3+(2*x))", captureOutput(expr::print));
    }

    @Test
    void testParserVariableAndNumber() throws ExpressionException {
        ExpressionParser parser = new ExpressionParser("x");
        Expression expr = parser.parse();
        assertEquals("x", expr.toString());
        assertEquals(10, expr.eval("x=10"));

        parser = new ExpressionParser("42");
        expr = parser.parse();
        assertEquals("42", expr.toString());
        assertEquals(42, expr.eval("x=10"));
    }
}
