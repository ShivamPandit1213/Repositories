package JobApply;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import LaunchBrowser_PublicMethods.LaunchBrowser;
import LaunchBrowser_PublicMethods.PublicMethod;

public class NokriDotComMain {
	// static String text = null;
	static WebDriver driver;
	static String nextPagi = "//span[text()='Next']/parent::a";

	@Test
	public static void main(String[] args) throws Throwable {
//		LaunchBrowser browser = new LaunchBrowser();
//		WebDriver driver = browser.chromelaunch();
//		PublicMethod call = new PublicMethod(driver);
		driver = LaunchBrowser.chromelaunch();
		driver.get("https://www.naukri.com/");
		System.out.println("Current URL launched: " + driver.getCurrentUrl());

		String locator = "//a[text()='Login']";
		String text = PublicMethod.getText(driver, locator);
		WebElement btnText = driver.findElement(By.xpath(locator));
		PublicMethod.moveToElement(driver, btnText);
		PublicMethod.click(driver, locator);
		PublicMethod.getTitle(driver);
		PublicMethod.getScreenshot(driver);

		// Nokri.com login with credential
		String emailLocatior = "//label[text()='Email ID / Username']/following-sibling::input";
		String lText = "//label[text()='Email ID / Username']";
		text = PublicMethod.getText(driver, lText);
		PublicMethod.waitTillElementClickable(driver, emailLocatior);
		WebElement email = driver.findElement(By.xpath(emailLocatior));
		email.sendKeys("shivamparashar1213@gmail.com");
		String passLocatior = "//label[text()='Password']/following-sibling::input";
		WebElement pass = driver.findElement(By.xpath(passLocatior));
		pass.sendKeys("shivam1213");
		driver.findElement(By.xpath("//button[text()='Login']")).click();
		Thread.sleep(3000);
		PublicMethod.getScreenshot(driver);
		System.out.println("Login successful");

		String searchBox = "//span[text()='Search jobs here']/following-sibling::button";
		PublicMethod.waitTillElementClickable(driver, searchBox);
		PublicMethod.getScreenshot(driver);
		driver.findElement(By.xpath(searchBox)).click();
		Thread.sleep(3000);
		PublicMethod.getScreenshot(driver);

		WebElement we = driver.findElement(By.xpath("//input[@placeholder='Enter keyword / designation / companies']"));
		we.sendKeys("Automation Testing, Automation Tester, Qa Automation, Qa Testing, Selenium Automation Jobs");
		driver.findElement(By.xpath("//span[text()='Search']//ancestor::button")).click();
		Thread.sleep(5000);
		PublicMethod.getScreenshot(driver);
		// String parentWindow = PublicMethod.getWindowHandle(driver);
		// System.out.println("Parent session Id: " + parentWindow);

		String slider = "//div[@class='slider-Container']//div[contains(@class, 'handle')]";
		PublicMethod.waitForElementVisible(driver, slider);
		PublicMethod.handleSlider(driver, slider, -182);

		// Jobs latest - Only before 7 days
		String jobTime = "//span[text()='Freshness']/parent::div/following-sibling::div//button";
		WebElement latest = driver.findElement(By.xpath(jobTime));
		PublicMethod.waitForElementVisible(driver, jobTime);
		latest.click();
		driver.findElement(By.xpath("//span[text()='Last 7 days']/parent::a")).click();
		// Select select = new Select();
		Thread.sleep(5000);

		String page = "//div[@align='center']//a";
		PublicMethod.waitForElementVisible(driver, page);
		List<WebElement> pagination = driver.findElements(By.xpath(page));
		int pageCount = pagination.size();
		System.out.println("\nTotal page count in pagination: " + pageCount);// Getting total page count

		String count = "//div[@class='srp-jobtuple-wrapper']//div[contains(@class,'row1')]//a";
		PublicMethod.waitForElementVisible(driver, count);
		List<WebElement> countJob = driver.findElements(By.xpath(count));

		for (int i = 0; i < pageCount; i++) {
			WebElement currPage = pagination.get(i);// Getting current page dynamic element
			String currPageName = currPage.getText();// Getting current page Text number and converting into String
			System.out.println("\nTotal job count: " + countJob.size() + ", on current page: " + currPageName);
			// Getting total job count on current page.

			String parentWindow = driver.getWindowHandle();

			for (int j = 0; j < countJob.size(); j++) {
				WebElement title = countJob.get(j);
				// WebElement checkJobType = driver.findElement(By.xpath(buttonApply));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", title);
				String jobTitle = title.getText();
				System.out.println("\nTitle " + (j + 1) + " : " + jobTitle);

				try {
					// 1️⃣ Open job in a new tab/window
					title.click();
					Thread.sleep(3000); // replace with explicit wait if pop-up delays
					PublicMethod.getScreenshot(driver);

					// 2️⃣ Switch to the new window
					for (String handle : driver.getWindowHandles()) {
						if (!handle.equals(parentWindow)) {
							driver.switchTo().window(handle);
							break;
						}
					}

					// 3️⃣ Check if "Prefers Women" is present
					List<WebElement> prefer = driver.findElements(By.xpath("//span[contains(text(),'Prefers women')]"));
					if (!prefer.isEmpty()) {
						System.err.println("Current Job is for Women, skipping this job.");
						driver.close();
						driver.switchTo().window(parentWindow);
						continue;
					}

					// 4️⃣ Wait for Apply button and interact
					String buttonApply = "//button[contains(text(),'Apply')]";
					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(buttonApply)));
					WebElement checkJobType = driver.findElement(By.xpath(buttonApply));

					String jobButtonName = checkJobType.getText();
					if (jobButtonName.equalsIgnoreCase("Apply on company site")) {
						System.err.println("Current job button name: " + jobButtonName);
						driver.close();
						driver.switchTo().window(parentWindow);
					} else if (jobButtonName.equalsIgnoreCase("Apply")) {
						System.out.println("Current job button name: " + jobButtonName);
						// WebElement chatbot =
						// driver.findElement(By.xpath("//div[@class='chatbot_MessageContainer']"));

						if (checkJobType.isEnabled() || !checkJobType.isEnabled()) {
							wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonApply)));
							checkJobType.click();
							Thread.sleep(5000);
							List<WebElement> chatbotClose = driver.findElements(
									By.xpath("//div[contains(@class,'chatbot')]//button[contains(@class,'close')]"));
							if (!chatbotClose.isEmpty()) {
								chatbotClose.get(0).click();
								System.out.println("Closed chatbot to clear overlay.");
							} else {
								System.out.println("Checking chatbot screen.");
								Thread.sleep(50000);
							}
						}

						// confirm successful application
						List<WebElement> appliedMsg = driver
								.findElements(By.xpath("//span[contains(text(),'You have successfully applied')]"));
						if (!appliedMsg.isEmpty()) {
							System.out.println("You have successfully applied to Job");
						} else {
							System.err.println("You have not successfully applied to Job");
						}

						driver.close();
						driver.switchTo().window(parentWindow);
					}
				} catch (Exception e) {
					System.err.println("Error while processing job: " + e.getMessage());
					// Ensure the child window is closed if an error occurs
					for (String handle : driver.getWindowHandles()) {
						if (!handle.equals(parentWindow)) {
							driver.close();
							driver.switchTo().window(parentWindow);
							break;
						}
					}
				}
			}
		}
	}
}
