import config.PizzeriaConfig;
import config.PizzeriaConfigLoader;
import config.PizzeriaConfigSource;
import model.ConsoleOrderStatusSink;
import model.PizzeriaFactory;
import model.PizzeriaManager;
import model.api.OrderStatusSink;

public class Main {
    public static void main(String[] args) {
        PizzeriaManager pizzeria = null;
        try {
            PizzeriaConfigSource configSource = new PizzeriaConfigLoader("src/main/resources/pizzeria_config.json");
            PizzeriaConfig config = configSource.load();

            OrderStatusSink statusSink = new ConsoleOrderStatusSink();
            PizzeriaFactory factory = new PizzeriaFactory();
            pizzeria = factory.create(config, statusSink);
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