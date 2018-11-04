package test.javafirst;
import org.junit.Test;
import org.junit.Before;


public class test_one {

    public static ThreadLocal<Application> tlApp= new ThreadLocal<>();
    public Application app;

    @Before
    public void start() {
        if (tlApp.get() != null) {
            app = tlApp.get();
            return;
        }

        app = new Application();
        tlApp.set(app);

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> { app.quit(); app = null; }));
    }

    @Test
    public void test_one_sap () {

        //open main page
        app.open_page("https://www.marksandspencer.com");
        //open product catalog function
        app.open_catalog();
        //sorting prices from lower price to higher
        app.sort_items();
        // add 3 сheapest items to cart
        app.add_to_basket(3);

    }






}
