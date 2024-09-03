import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.List;

public class DemowebshopUITests {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().browserVersion("114.0.5735.90").setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testUserRegistration() {
        driver.get("https://demowebshop.tricentis.com/register");
        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys("John");
        driver.findElement(By.id("LastName")).sendKeys("Doe");
        driver.findElement(By.id("Email")).sendKeys("john.doe@example.com");
        driver.findElement(By.id("Password")).sendKeys("password");
        driver.findElement(By.id("ConfirmPassword")).sendKeys("password");
        driver.findElement(By.id("register-button")).click();
        WebElement confirmationMessage = driver.findElement(By.className("result"));
        assertTrue(confirmationMessage.getText().contains("Your registration completed"));
    }

    @Test
    public void testLoginFunctionality() {
        driver.get("https://demowebshop.tricentis.com/login");
        driver.findElement(By.id("Email")).sendKeys("test@example.com");
        driver.findElement(By.id("Password")).sendKeys("password123");
        driver.findElement(By.cssSelector("input.login-button")).click();
        String welcomeText = driver.findElement(By.cssSelector("div.account")).getText();
        assertTrue(welcomeText.contains("test@example.com"));
    }

    @Test
    public void testComputerSubGroups() {
        driver.get("https://demowebshop.tricentis.com/computers");
        List<WebElement> subGroups = driver.findElements(By.cssSelector(".block-category-navigation .sub-category-item a"));
        assertEquals(3, subGroups.size());
        assertTrue(subGroups.stream().anyMatch(element -> element.getText().equals("Desktops")));
        assertTrue(subGroups.stream().anyMatch(element -> element.getText().equals("Notebooks")));
        assertTrue(subGroups.stream().anyMatch(element -> element.getText().equals("Accessories")));
    }

    @Test
    public void testSortingItems() {
        driver.get("https://demowebshop.tricentis.com/desktops");
        Select sortDropdown = new Select(driver.findElement(By.id("products-orderby")));
        sortDropdown.selectByVisibleText("Name: A to Z");
        WebElement firstProduct = driver.findElement(By.cssSelector(".product-item:first-of-type .product-title a"));
    }

    @Test
    public void testChangeNumberOfItemsOnPage() {
        driver.get("https://demowebshop.tricentis.com/desktops");
        Select pageSizeDropdown = new Select(driver.findElement(By.id("products-pagesize")));
        pageSizeDropdown.selectByVisibleText("12");
        List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));
        assertEquals(12, products.size());
    }

    @Test
    public void testAddToWishlist() {
        driver.get("https://demowebshop.tricentis.com");
        driver.findElement(By.cssSelector("input[value='Add to wishlist']")).click();
        WebElement successNotification = driver.findElement(By.xpath("//p[@class='content']"));
        assertTrue(successNotification.getText().contains("The product has been added to your wishlist"));
    }

    @Test
    public void testAddToCartFunctionality() {
        driver.get("https://demowebshop.tricentis.com");
        driver.findElement(By.cssSelector("a[href='/books']")).click();
        driver.findElement(By.linkText("Fiction")).click();
        driver.findElement(By.cssSelector("input[value='Add to cart']")).click();
        WebElement shoppingCartLink = driver.findElement(By.cssSelector("span.cart-label"));
        shoppingCartLink.click();
        WebElement successNotification = driver.findElement(By.className("content"));
        assertTrue(successNotification.getText().contains("The product has been added to your shopping cart"));
    }

    @Test
    public void testRemoveFromCart() {
        testAddToCartFunctionality();
        driver.findElement(By.name("removefromcart")).click();
        driver.findElement(By.name("updatecart")).click();
        WebElement emptyCartMessage = driver.findElement(By.className("no-data"));
        assertEquals("Your Shopping Cart is empty!", emptyCartMessage.getText());
    }

    @Test
    public void testCheckoutItem() {
        testLoginFunctionality();
        testAddToCartFunctionality();
        driver.findElement(By.className("checkout")).click();
    }
}