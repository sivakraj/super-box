package in.bluebytes.superbox;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SuperBoxApplicationTests {

	@LocalServerPort
	public int port;

	public static WebDriver webDriver;

	public String baseURL;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		webDriver = new ChromeDriver();
		webDriver.manage().window().maximize();
		webDriver.manage().deleteAllCookies();
		webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	@AfterAll
	public static void afterAll() {
		webDriver.quit();
		webDriver = null;
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
	}

	/**
	 * Tests sign up, login, and note related functionalities in one method as the program uses in-memory database
	 * and the details get flushed out after each test. So tests cannot be segregated into each user stories.
	 */
	@Test
	public void testSuperBox() throws InterruptedException {

		String firstName = "testFirstName";
		String lastName = "testLastName";
		String userName = "testUserName";
		String password = "testPassword";

		//One cannot avoid waits when testing from browser
		WebDriverWait wait = new WebDriverWait(webDriver, 10);

		//Sign up
		webDriver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(webDriver);
		signupPage.signUp(firstName, lastName, userName, password);

		WebElement successText = webDriver.findElement(By.cssSelector(".alert-dark"));
		assertTrue(successText.getText().contains("successfully signed up"));

		//Login
		webDriver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(webDriver);
		loginPage.login(userName, password);

		assertEquals("Home", webDriver.getTitle());

		//navigate to Notes tab
		handleClicks(wait, "#nav-notes-tab");

		//pop the add note modal to add notes
		handleClicks(wait, "#nav-notes .btn-info");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		NotePage notePage = new NotePage(webDriver);
		notePage.addNote("Test Note Title", "Test note description.");

		//dismiss the success/failure modal
		handleClicks(wait, ".action-message-modal button");

		//now navigate to the notes tab to see if the added note is displayed
		/** page gets refreshed after above step so first wait for the element to become stale
		and then recapture it - to avoid element not attached to page document error **/
		wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.cssSelector("#nav-notes-tab"))));
		handleClicks(wait, "#nav-notes-tab");

		//check if the note is added successfully
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes tbody th")));
		WebElement noteTitle = wait.until(webDriver -> webDriver.findElement(By.cssSelector("#nav-notes tbody th")));
		assertEquals("Test Note Title", noteTitle.getText());

	}

	private void handleClicks(WebDriverWait wait, String elementToBeClicked) {
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(elementToBeClicked)));
		WebElement clickableElement = wait.until(webDriver -> webDriver.findElement(By.cssSelector(elementToBeClicked)));
		clickableElement.click();
	}

}
