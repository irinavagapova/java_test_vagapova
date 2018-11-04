package test.javafirst;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;
import java.util.List;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

public class Application {

    private WebDriver driver;
    private WebDriverWait wait;
    private Actions action;


    public Application() {
        driver = new ChromeDriver();
        wait= new WebDriverWait(driver,10);
        action= new Actions(driver);
    }
    public void quit() {
        driver.quit();
    }

    public void open_page(String page)
    {
        driver.get(page);
    }

    //open product catalog function
    public void open_catalog()
    {

        WebElement link = driver.findElement(By.cssSelector(".nav-primary__list li:first-child a"));
        wait.until(elementToBeClickable(link));
        link.click();
        wait.until(presenceOfElementLocated(By.cssSelector(".left-navigation")));
        WebElement link_catalog = driver.findElement(By.cssSelector(".left-navigation li:nth-child(2) a"));
        wait.until(elementToBeClickable(link_catalog));
        link_catalog.click();
        // wait until catalog list all located on the page
        wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(".product-listing-container .detail a")));

    }


    //sorting prices from lower price to higher
    public void sort_items()
    {
        //current sort value
        Select select = new Select(driver.findElement(By.cssSelector("select#sortBy-top")));
        WebElement option = select.getFirstSelectedOption();
        String defaultItem = option.getAttribute("value");

        //check dropdown value
        if(defaultItem!="product_price_from|0")
        {
            //list item's prices in catalog
            List<WebElement> links_price=driver.findElements(By.cssSelector(".product-listing-container p.price.sale"));
            List<String> arr_old_price = new ArrayList<String>();
            for (WebElement item : links_price)
            {
                String item_price=item.getAttribute("innerText");
                arr_old_price.add(item_price);
            }
            //change sort
            driver.findElement(By.xpath("//select[option[@value='product_price_from|0']]")).sendKeys("product_price_from|0");
            // wait until new sort(from low to high)catalog list all located on the page
            wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(".product-listing-container .detail a")));
            //list items new prices in catalog
            List <WebElement> links_price_new=driver.findElements(By.cssSelector(".product-listing-container p.price.sale"));

            List<String> arr_new_price = new ArrayList<String>();
            for (WebElement item_new : links_price_new)
            {
                String item_new_price=item_new.getAttribute("innerText");
                arr_new_price.add(item_new_price);
            }
            assert String.join(",", arr_old_price) != String.join(",",arr_new_price):"Goods didn't  sorted from low to high price";

        }
        //list of item's prices if sorted from low to high
        else
        {
            wait.until(presenceOfAllElementsLocatedBy(By.cssSelector(".product-listing-container p.price.sale")));
            //list item's prices in catalog
            List <WebElement> links_price=driver.findElements(By.cssSelector(".product-listing-container p.price.sale"));

        }


    }

    // add items to cart
    public void add_to_basket(int count)
    {

        for(int i=1; i<=count; i++)
        {
            WebElement item = driver.findElement(By.cssSelector(".grid-view li:nth-child("+i+")"));
            wait.until(visibilityOf(item));
            action.moveToElement(item).perform();
            WebElement quick_button=driver.findElement(By.cssSelector(".grid-view li:nth-child("+i+") a.lightbox-link.quick-view"));
            wait.until(visibilityOf(quick_button));
            quick_button.click();
            WebElement cart_window=driver.findElement(By.cssSelector("#lightboxQuick"));
            wait.until(visibilityOf(cart_window));
            WebElement basket_button=driver.findElement(By.cssSelector("input.basket"));
            basket_button.click();
            wait.until(not(visibilityOf(cart_window)));

            WebElement window_add_to_bag=driver.findElement(By.cssSelector(".message.added-to-bag"));
            wait.until(not(visibilityOf(window_add_to_bag)));
            wait.until( textToBePresentInElementLocated(By.cssSelector("span#viewBagCountDuck"),""+i+""));

        }
        //check that there are  items=count in the cart
        wait.until( textToBePresentInElementLocated(By.cssSelector("span#viewBagCountDuck"),""+count+""));

    }
}
