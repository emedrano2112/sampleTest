package lineOfCredits;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class creditCreation {
    private WebDriver driver;
    private SoftAssert softAssert;
    private Capabilities cap;

    @BeforeMethod
    public void setup() {
        softAssert = new SoftAssert();
        driver = new ChromeDriver();
        //driver = new FirefoxDriver();
        cap = ((RemoteWebDriver) driver).getCapabilities();
        WebDriverWait wait = new WebDriverWait(driver, 15);
        driver.get("http://credit-test.herokuapp.com/line_of_credits");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body"))); //Waits for the presence of the main class is visible
    }
    @AfterMethod
    public void tearDown() {
        driver.close();
        softAssert.assertAll();
    }

    @Test
    public void firstScenario() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));
        WebElement amount = driver.findElement(By.id("transaction_amount"));
        WebElement saveTransaction = driver.findElement(By.name("Save Transaction"));
        Select type = new Select(driver.findElement(By.id("transaction_type")));
        int interest = interestCalc(35, 1000, 30);
        int payoffMonth = interest + 500;

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("35");
        creditLimit.sendKeys("1000");
        createLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("line_of_credits show")));
        softAssert.assertTrue(driver.findElement(By.id("notice")).getText().equals("Line of credit was successfully created."), "Line of credit not created");
        amount.sendKeys("500");
        saveTransaction.click();
        softAssert.assertTrue(driver.findElement(By.xpath("//*[@id=\"transactions_table\"]/tbody/tr")).isDisplayed(), "Transaction not displayed");
        int totalPayoffMonth = Integer.parseInt(driver.findElement(By.xpath("/html/body/p[6]")).getText());
        softAssert.assertTrue(totalPayoffMonth == payoffMonth, "Wrong payoff value calculation");
    }

    @Test
    public void secondScenario() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));
        WebElement amount = driver.findElement(By.id("transaction_amount"));
        WebElement saveTransaction = driver.findElement(By.name("Save Transaction"));
        Select type = new Select(driver.findElement(By.id("transaction_type")));
        int firstInterest = interestCalc(35, 500, 15);
        int secondInterest = interestCalc(35, 200, 10);
        int thirdInterest = interestCalc(35, 100, 5);
        int interest = firstInterest + secondInterest + thirdInterest;
        int payoffMonth = interest + 400; //500 - 200 + 100 = 400

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("35");
        creditLimit.sendKeys("1000");
        createLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("line_of_credits show")));
        softAssert.assertTrue(driver.findElement(By.id("notice")).getText().equals("Line of credit was successfully created."), "Line of credit not created");
        amount.sendKeys("500");
        saveTransaction.click();
        softAssert.assertTrue(driver.findElement(By.xpath("//*[@id=\"transactions_table\"]/tbody/tr")).isDisplayed(), "Transaction not displayed");
        type.selectByVisibleText("Payment");
        amount.sendKeys("200");
        Select atDay = new Select(driver.findElement(By.id("transaction_applied_at")));
        atDay.selectByVisibleText("15");
        saveTransaction.click();
        softAssert.assertTrue(driver.findElement(By.xpath("//*[@id=\"transactions_table\"]/tbody/tr[2]")).isDisplayed(), "Transaction not displayed");
        type.selectByVisibleText("Draw");
        amount.sendKeys("100");
        atDay.selectByVisibleText("25");
        saveTransaction.click();
        softAssert.assertTrue(driver.findElement(By.xpath("//*[@id=\"transactions_table\"]/tbody/tr[3]")).isDisplayed(), "Transaction not displayed");
        int totalPayoffMonth = Integer.parseInt(driver.findElement(By.xpath("/html/body/p[6]")).getText());
        softAssert.assertTrue(totalPayoffMonth == payoffMonth, "Wrong payoff value calculation");
    }

    @Test
    public void wrongCreditLimitLetters() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("35");
        creditLimit.sendKeys("AAAA");
        createLineOfCredit.click();
        softAssert.assertTrue(driver.findElement(By.id("error_explanation")).isDisplayed(), "Credit Limit number validation is broken");
    }

    @Test
    public void wrongAprLetters() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("YY");
        creditLimit.sendKeys("1000");
        createLineOfCredit.click();
        softAssert.assertTrue(driver.findElement(By.id("error_explanation")).isDisplayed(), "Apr number validation is broken");
    }

    @Test
    public void wrongCreditLimitNeg() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("35");
        creditLimit.sendKeys("-1000");
        createLineOfCredit.click();
        softAssert.assertTrue(driver.findElement(By.id("error_explanation")).isDisplayed(), "Credit Limit number validation is broken");
    }

    @Test
    public void wrongAprNeg() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("-35");
        creditLimit.sendKeys("1000");
        createLineOfCredit.click();
        softAssert.assertTrue(driver.findElement(By.id("error_explanation")).isDisplayed(), "Apr number validation is broken");
    }

    @Test
    public void wrongCreditLimitEmpty() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("35");
        creditLimit.sendKeys("");
        createLineOfCredit.click();
        softAssert.assertTrue(driver.findElement(By.id("error_explanation")).isDisplayed(), "Credit Limit number validation is broken");
    }

    @Test
    public void wrongAprEmpty() {
        //Elements declaration
        WebDriverWait wait = new WebDriverWait(driver, 15); //wait object declaration to be used in this test
        WebElement newLineOfCredit = driver.findElement(By.linkText("New Line of credit"));
        WebElement apr = driver.findElement(By.id("line_of_credit_apr"));
        WebElement creditLimit = driver.findElement(By.id("line_of_credit_credit_limit"));
        WebElement createLineOfCredit = driver.findElement(By.name("commit"));

        //Test steps
        newLineOfCredit.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body")));
        apr.sendKeys("");
        creditLimit.sendKeys("1000");
        createLineOfCredit.click();
        softAssert.assertTrue(driver.findElement(By.id("error_explanation")).isDisplayed(), "Apr number validation is broken");
    }

    public int interestCalc(int apr, int amount, int day) {
        int interest = (((amount * (apr/100))/365) * day);
        return interest;
    }
}
