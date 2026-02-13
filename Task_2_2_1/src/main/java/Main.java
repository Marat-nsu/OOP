import model.PizzeriaManager;

public class Main {
    public static void main(String[] args) {
        PizzeriaManager pizzeria = null;
        try {
            pizzeria = new PizzeriaManager("src/main/resources/pizzeria_config.json");
            pizzeria.startPizzeria();

            pizzeria.placeOrder("Margherita");
            Thread.sleep(500);
            pizzeria.placeOrder("Pepperoni");
            Thread.sleep(1000);
            pizzeria.placeOrder("Hawaiian");

            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pizzeria != null) {
                pizzeria.stopPizzeria();
            }
        }
    }
}