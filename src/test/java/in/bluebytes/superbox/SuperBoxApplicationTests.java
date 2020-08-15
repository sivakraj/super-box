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

	private static final String FIRST_NAME = "testFirstName";
	private static final String LAST_NAME = "testLastName";
	private static final String USER_NAME = "testUserName";
	private static final String PASSWORD = "testPassword";

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
	 * Test sign up, login and logout functionalities of super box
	 * @throws InterruptedException
	 */
	@Test
	public void testSuperBoxLoginLogout() throws InterruptedException {

		//One cannot avoid waits when testing from browser
		WebDriverWait wait = new WebDriverWait(webDriver, 10);

		//Sign up
		webDriver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(webDriver);
		signupPage.signUp(FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD);

		WebElement successText = webDriver.findElement(By.cssSelector(".signup-success-alert"));
		assertTrue(successText.getText().contains("successfully signed up"));

		//Login
		LoginPage loginPage = new LoginPage(webDriver);
		loginPage.login(USER_NAME, PASSWORD);

		//Verify home page is accessible post login
		assertEquals("Home", webDriver.getTitle());

		//perform logout
		handleClicks(wait, "#logoutDiv button");

		//Verify home page is not accessible post logout
		assertEquals("Login", webDriver.getTitle());

	}

	/**
	 * Tests sign up, login, and note related functionalities in one method as the program uses in-memory database
	 * and the details get flushed out after each test. So tests cannot be segregated into each user stories.
	 */
	@Test
	public void testSuperBoxNotes() throws InterruptedException {

		//One cannot avoid waits when testing from browser
		WebDriverWait wait = new WebDriverWait(webDriver, 10);

		//Sign up
		webDriver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(webDriver);
		signupPage.signUp(FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD);

		WebElement successText = webDriver.findElement(By.cssSelector(".signup-success-alert"));
		assertTrue(successText.getText().contains("successfully signed up"));

		//Login
		LoginPage loginPage = new LoginPage(webDriver);
		loginPage.login(USER_NAME, PASSWORD);

		assertEquals("Home", webDriver.getTitle());

		//navigate to Notes tab
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-notes-tab")));
		navigateToTab(wait, "#nav-notes-tab", false);

		//pop the add note modal to add notes
		handleClicks(wait, "#nav-notes .btn-info");

		//add a note
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		NotePage notePage = new NotePage(webDriver);
		notePage.addNote("Test Note Title", "Test note description.");

		//dismiss the success/failure modal
		handleClicks(wait, ".action-message-modal button");

		//now navigate to the notes tab to see if the added note is displayed
		navigateToTab(wait, "#nav-notes-tab", true);

		//check if the note is added successfully
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes tbody th")));
		WebElement noteTitle = wait.until(webDriver -> webDriver.findElement(By.cssSelector("#nav-notes tbody th")));
		String noteTitleText = noteTitle.getText();
		assertEquals("Test Note Title", noteTitleText);

		//click edit button and modify tite of a note
		handleClicks(wait, "#nav-notes tbody .btn-success");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		notePage.addNote( "s", "");

		//dismiss the success/failure modal
		handleClicks(wait, ".action-message-modal button");

		//now navigate to the notes tab to see if the modified note is displayed
		navigateToTab(wait, "#nav-notes-tab", true);

		//check if the note is modified successfully
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes tbody th")));
		WebElement noteTitleModified = wait.until(webDriver -> webDriver.findElement(By.cssSelector("#nav-notes tbody th")));
		assertEquals("Test Note Titles", noteTitleModified.getText());

		//click delete button
		handleClicks(wait, "#nav-notes tbody a.btn-danger");

		//dismiss the success/failure modal
		handleClicks(wait, ".action-message-modal button");

		//now navigate to the notes tab to see if a note is deleted
		navigateToTab(wait, "#nav-notes-tab", true);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-notes tbody")));
		WebElement noteTableBody = wait.until(webDriver -> webDriver.findElement(By.cssSelector("#nav-notes tbody")));
		assertEquals("", noteTableBody.getText().trim());

		//perform logout
		handleClicks(wait, "#logoutDiv button");

	}

	/**
	 * Tests sign up, login, and credential related functionalities in one method as the program uses in-memory database
	 * and the details get flushed out after each test. So tests cannot be segregated into each user stories.
	 */
	@Test
	public void testSuperBoxCredentials() throws InterruptedException {

		//One cannot avoid waits when testing from browser
		WebDriverWait wait = new WebDriverWait(webDriver, 10);

		//Sign up
		webDriver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(webDriver);
		signupPage.signUp(FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD);

		WebElement successText = webDriver.findElement(By.cssSelector(".signup-success-alert"));
		assertTrue(successText.getText().contains("successfully signed up"));

		//Login
		LoginPage loginPage = new LoginPage(webDriver);
		loginPage.login(USER_NAME, PASSWORD);

		assertEquals("Home", webDriver.getTitle());

		//navigate to Credentials tab
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-credentials-tab")));
		navigateToTab(wait, "#nav-credentials-tab", false);

		//pop the add credential modal to add credentials
		handleClicks(wait, "#nav-credentials .btn-info");

		//add a credential
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		CredentialPage credentialPage = new CredentialPage(webDriver);
		credentialPage.addCredential("Test URL", "Test User Name", "Test Password");

		//dismiss the success/failure modal
		handleClicks(wait, ".action-message-modal button");

		//now navigate to the credentials tab to see if the added credential is displayed
		navigateToTab(wait, "#nav-credentials-tab", true);

		//check if the credential is added successfully
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials tbody th")));
		WebElement credURL = wait.until(webDriver -> webDriver.findElement(By.cssSelector("#nav-credentials tbody th")));
		String credURLText = credURL.getText();
		assertEquals("Test URL", credURLText);

		//click edit button and modify URL of a credential
		handleClicks(wait, "#nav-credentials tbody .btn-success");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		credentialPage.addCredential( "s", "", "");

		//dismiss the success/failure modal
		handleClicks(wait, ".action-message-modal button");

		//now navigate to the credentials tab to see if the modified credential is displayed
		navigateToTab(wait, "#nav-credentials-tab", true);

		//check if the credential is modified successfully
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials tbody th")));
		WebElement credURLModified = wait.until(webDriver -> webDriver.findElement(By.cssSelector("#nav-credentials tbody th")));
		assertEquals("Test URLs", credURLModified.getText());

		//click delete button
		handleClicks(wait, "#nav-credentials tbody a.btn-danger");

		//dismiss the success/failure modal
		handleClicks(wait, ".action-message-modal button");

		//now navigate to the credentials tab to see if a credential is deleted
		navigateToTab(wait, "#nav-credentials-tab", true);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-credentials tbody")));
		WebElement credTableBody = wait.until(webDriver -> webDriver.findElement(By.cssSelector("#nav-credentials tbody")));
		assertEquals("", credTableBody.getText().trim());

		//perform logout
		handleClicks(wait, "#logoutDiv button");

	}

	private void handleClicks(WebDriverWait wait, String elementToBeClicked) {
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(elementToBeClicked)));
		WebElement clickableElement = wait.until(webDriver -> webDriver.findElement(By.cssSelector(elementToBeClicked)));
		clickableElement.click();
	}

	private void navigateToTab(WebDriverWait wait, String elementToBeClicked, boolean isStaleCheck) {
		if(isStaleCheck) {
			/** page gets refreshed after any actions(add/modify/delete), so first wait for the tab elements to become stale
			 and then recapture it - to avoid element not attached to page document error **/
			wait.until(ExpectedConditions.stalenessOf(webDriver.findElement(By.cssSelector("#nav-notes-tab"))));
		}
		handleClicks(wait, elementToBeClicked);
	}

}
