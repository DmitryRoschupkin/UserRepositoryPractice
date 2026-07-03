import annotations.ComponentScan;
import contexts.ApplicationContext;
import menu.*;

@ComponentScan("repository")
public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ApplicationContext(Main.class);
        Menu m = context.getComponent(MenuManager.class);
        m.run();
    }
}
