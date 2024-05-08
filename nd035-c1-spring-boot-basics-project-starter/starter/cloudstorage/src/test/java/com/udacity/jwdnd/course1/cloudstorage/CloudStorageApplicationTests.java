package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new FirefoxDriver();


	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}
	private String _username ="theuser";
	private String _password ="password";




//Write a Selenium test that verifies that the home page is not accessible without logging in.
    @Test
	void TestHomePageRedirectWithoutSigningIn() throws MalformedURLException {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");
		URL url = new URL(driver.getCurrentUrl());
		Assertions.assertEquals(url.getFile(),"/login");
	}
	//Write a Selenium test that signs up a new user, logs that user in, verifies
	// that they can access the home page, then logs out and verifies that the home
	// page is no longer accessible.
	@Test
	void TestSignupLogin()throws MalformedURLException
	{
		signupLogin(_username,_password);
		//verify home page
		URL url = new URL(driver.getCurrentUrl());
		Assertions.assertEquals(url.getFile(),"/home");
		//log out
		driver.findElement(By.id("logoutbutton")).click();

		//verify you no longer have access
		TestHomePageRedirectWithoutSigningIn();
	}

	//Write a Selenium test that logs in an existing user, creates
	// a note and verifies that the note details are visible in the note list.
	@Test
	public void createVerifyNote()
	{
		String notetitle ="This is the title";
		String notedesc ="This is the new note";
		signupLogin(_username,_password);
		driver.findElement(By.id("nav-notes-tab")).click();
		driver.switchTo().activeElement();
		driver.findElement(By.id("newNote")).click();
		saveTestNote(notetitle, notedesc);
		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("saveok"))).click();

		verifySingleNote(wait,notetitle,notedesc);
		driver.findElement(By.id("logoutbutton")).click();

	}

	//Write a Selenium test that logs in an existing user with existing notes, clicks the edit note button on an existing note,
	// changes the note data, saves the changes, and verifies that the changes appear in the note list.
	@Test
	public void editNote()
	{
		createVerifyNote();
		String notetitle="My new title";
		String notedesc="My new desc";
		WebDriverWait wait = new WebDriverWait(driver, 10);

		LoginPage loginPage = new LoginPage(driver);
		loginPage.Login( _username, _password);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-notes-tab"))).click();
		driver.switchTo().activeElement();
		driver.findElement(By.id("noteeditbutton")).click();

		saveTestNote(notetitle, notedesc);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("saveok"))).click();

		verifySingleNote(wait,notetitle,notedesc);
		driver.findElement(By.id("logoutbutton")).click();
	}

	//Write a Selenium test that logs in an existing user with existing notes,
	// clicks the delete note button on an existing note, and verifies that the note no longer appears in the note list.
	@Test
	public void deleteNote()
	{
		createVerifyNote();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.Login( _username, _password);

		wait.until(presenceOfElementLocated(By.id("nav-notes-tab"))).click();
		driver.switchTo().activeElement();
		driver.findElement(By.id("notedeletebutton")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("saveok"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-notes-tab")));
		assertThrows(org.openqa.selenium.NoSuchElementException.class,
				()->driver.findElement(By.id("notedeletebutton")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logoutbutton"))).click();
	}
	//Write a Selenium test that logs in an existing user,
	// creates a credential and verifies that the credential details are visible in the credential list.
	@Test
	public void createVerifyCredentials()
	{
		String credurl="test.com";
		String credusername="user";
		String credpassword="secret";
		signupLogin(_username,_password);

		driver.findElement(By.id("nav-credentials-tab")).click();
		driver.switchTo().activeElement();
		driver.findElement(By.id("newCred")).click();

		saveTestCred(credurl, credusername, credpassword);
		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("saveok"))).click();

		verifySingleCredential(wait,credurl, credusername, credpassword);
		driver.findElement(By.id("logoutbutton")).click();
	}
	//Write a Selenium test that logs in an existing user with existing credentials,
	// clicks the edit credential button on an existing credential, changes the credential data,
	// saves the changes, and verifies that the changes appear in the credential list.
	@Test
	public void editCredentials()
	{
		createVerifyCredentials();
		String credurl="ibm.com";
		String credusername="newuser";
		String credpassword="newsecret";
		WebDriverWait wait = new WebDriverWait(driver, 10);

		LoginPage loginPage = new LoginPage(driver);
		loginPage.Login( _username, _password);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-credentials-tab"))).click();
		driver.switchTo().activeElement();
		driver.findElement(By.id("crededitbutton")).click();
		saveTestCred(credurl,credusername,credpassword);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("saveok"))).click();

		verifySingleCredential(wait,credurl, credusername, credpassword);
		driver.findElement(By.id("logoutbutton")).click();
	}
	//Write a Selenium test that logs in an existing user with existing credentials, clicks the delete
	// credential button on an existing credential, and verifies that the credential no longer appears
	// in the credential list.
	@Test
	public void deleteCredentials()
	{
		createVerifyCredentials();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.Login( _username, _password);

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-credentials-tab"))).click();
		driver.switchTo().activeElement();
		wait.until(presenceOfElementLocated(By.id("creddeletebutton"))).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("saveok"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-credentials-tab")));
		assertThrows(org.openqa.selenium.NoSuchElementException.class,
				()->driver.findElement(By.id("creddeletebutton")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logoutbutton"))).click();
	}
	private void signupLogin(String username,String password)
	{
		SignupPage signupPage = new SignupPage(driver);
		LoginPage loginPage = new LoginPage(driver);

		//sign-up
		driver.get("http://localhost:" + port + "/signup");
		signupPage.Signup("stu","wain", username, password);
		//log in
		driver.get("http://localhost:" + port + "/login");
		loginPage.Login( username, password);

	}
	private void saveTestNote(String notetitle, String notedesc)
	{
		driver.findElement(By.id("note-title")).clear();
		driver.findElement(By.id("note-title")).sendKeys(notetitle);
		driver.findElement(By.id("note-description")).clear();
		driver.findElement(By.id("note-description")).sendKeys(notedesc);

		WebElement notesubmit=driver.findElement(By.id("noteSubmit"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", notesubmit);
	}

	private void verifySingleNote(WebDriverWait wait,String notetitle,String notedesc)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notetitle"))).isDisplayed();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notedesc"))).isDisplayed();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notesTable"))).isDisplayed();

		WebElement notesResult = driver.findElement(By.id("notesTable"));
		List<WebElement> titleRow = notesResult.findElements(By.id("notetitle"));
		List<WebElement> descRow = notesResult.findElements(By.id("notedesc"));

		Assertions.assertEquals(1,titleRow.size());
		Assertions.assertEquals(1,descRow.size());
		Assertions.assertEquals(notetitle,titleRow.get(0).getAttribute("innerHTML"));
		Assertions.assertEquals(notedesc,descRow.get(0).getAttribute("innerHTML"));

	}
	private void saveTestCred(String credurl, String credusername, String credpassword)
	{
		driver.findElement(By.id("credential-url")).clear();
		driver.findElement(By.id("credential-url")).sendKeys(credurl);
		driver.findElement(By.id("credential-username")).clear();
		driver.findElement(By.id("credential-username")).sendKeys(credusername);
		driver.findElement(By.id("credential-password")).clear();
		driver.findElement(By.id("credential-password")).sendKeys(credpassword);

		WebElement credsubmit=driver.findElement(By.id("credentialSubmit"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", credsubmit);

	}
	private void verifySingleCredential(WebDriverWait wait, String credurl, String credusername, String credpassword)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("credurl"))).isDisplayed();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("credusername"))).isDisplayed();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("credpassword"))).isDisplayed();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("credentialTable"))).isDisplayed();

		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credurlRow = credentialTable.findElements(By.id("credurl"));
		List<WebElement> credusernameRow = credentialTable.findElements(By.id("credusername"));
		List<WebElement> credpasswordRow = credentialTable.findElements(By.id("credpassword"));

		Assertions.assertEquals(1,credurlRow.size());
		Assertions.assertEquals(1,credusernameRow.size());
		Assertions.assertEquals(1,credpasswordRow.size());
		Assertions.assertEquals(credurl,credurlRow.get(0).getAttribute("innerHTML"));
		Assertions.assertEquals(credusername,credusernameRow.get(0).getAttribute("innerHTML"));
		Assertions.assertNotEquals(0,credpasswordRow.get(0).getAttribute("innerHTML").length());
	}
	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}
