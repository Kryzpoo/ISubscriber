package subscriber;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;

class InstagramManager {

    InstagramManager(Properties _properties, Map _locators) {
        this.properties = _properties;
        this.locators = new HashMap<>(_locators);
    }

    private String username;
    private String password;
    private String groupName;
    private int followCount;
    private int startInterval;
    private int endInterval;

    private Properties properties;
    private final Map<String, String> locators;
    private String elementXpath;

    WebDriverWait longWait;
    WebDriverWait mediumWait;
    WebDriverWait shortWait;

    String getUsername() {
        return this.username;
    }

    String getPassword() {
        return this.password;
    }

    String getGroupName() {
        return this.groupName;
    }

    int getFollowCount() {
        return this.followCount;
    }

    int getStartInterval() {
        return this.startInterval;
    }

    int getEndInterval() {
        return this.endInterval;
    }

    String getSubscriberButton() {
        return this.locators.get("subscribeButton");
    }

    String getUnsubscriberButton() {
        return this.locators.get("unsubscribeButton");
    }

    JavascriptExecutor getJsExecutor(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

    //Метод загрузки параметров
    void getProperties() {
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        groupName = properties.getProperty("groupName");
        followCount = Integer.parseInt(properties.getProperty("followCount"));
        startInterval = Integer.parseInt(properties.getProperty("startInterval"));
        endInterval = Integer.parseInt(properties.getProperty("endInterval"));
    }


    WebDriver initializeDriver() {
        String chromeDriverFilename = "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", PropertiesLoader.getLocalDirectory() + chromeDriverFilename);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("-incognito");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = null;

        try {
            driver = new ChromeDriver(capabilities);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        } catch (IllegalStateException ex) {
            System.out.println("Не найден файл " + chromeDriverFilename);
        }
        initializeWaits(driver);
        return driver;
    }


    private void initializeWaits( WebDriver driver ) {
        this.longWait = new WebDriverWait(driver, 15);
        this.mediumWait = new WebDriverWait(driver, 5);
        this.shortWait = new WebDriverWait(driver, 3);
    }


    void getSite(WebDriver driver) {
        driver.get("https://www.instagram.com/");
        WebElement enterLink = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("enterLink"))));
        enterLink.click();
    }


    void loginOnSite(String username, String password) {
        WebElement loginField = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("loginField"))));
        loginField.clear();
        loginField.sendKeys(username);

        WebElement passwordField = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("passwordField"))));
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement enterButton = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("enterButton"))));
        enterButton.click();
    }


    void findGroup() {
        WebElement searchField = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("searchField"))));
        searchField.clear();
        searchField.sendKeys(this.getGroupName());

        WebElement groupLink = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("groupLink"))));
        groupLink.click();
    }


    void openFollowersPage() {
        WebElement followersPageLink = longWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("followersPageLink"))));
        followersPageLink.click();
    }


    String getSubscriberLiElement() {
        return locators.get("subscriberLiElement");
    }

    //Скроллинг костыльный через js
    void scrollDown(JavascriptExecutor jsExecutor, String elementString, int elementNum) {
        int counter = 0;
        this.elementXpath = String.format(elementString, elementNum);

        while ( counter < 5 ) {
            try {
                executeScroll(jsExecutor);
            } catch (Exception e) {
                executeScroll(jsExecutor);
            }
            counter++;
        }

    }


    private void executeScroll(JavascriptExecutor jsExecutor) {
        jsExecutor.executeScript("arguments[0].scrollIntoView();",
                mediumWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(this.elementXpath))));
    }


    boolean isVisibleSubscriber() {
        boolean elementPersistence;

        try {
            mediumWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("visibleSubscriber"))));
            elementPersistence = true;
        } catch (Exception e){
            elementPersistence = false;
        }
        return elementPersistence;
    }


    void randomWait(Random random, int startInterval, int endInterval) {
        startInterval = startInterval * 1000;
        endInterval = endInterval * 1000;

        int randomInterval = random.nextInt(( endInterval - startInterval ) + 1000) + startInterval;

        try {
            Thread.sleep(randomInterval);
        } catch (InterruptedException e) {
            this.randomWait(random, startInterval, endInterval);
        }
    }


    void handleBanner() {
        try {
            WebElement closeBannerButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("closeBannerButton"))));
            closeBannerButton.click();
        } catch (Exception e) {
            System.out.println("Баннер не был найден, продолжение работы.");
        }
    }


    void getSearchPage() {
        WebElement searchPageButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("searchButton"))));
        searchPageButton.click();
    }


    void getPersonalPage() {
        WebElement personalPageButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("personalPageButton"))));
        personalPageButton.click();
    }

    void openFollowingsPage() {
        WebElement followingsPage = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("followingsPage"))));
        followingsPage.click();
    }

    String getUnsubscriberLiElement() {
        return locators.get("usnubscriberLiElement");
    }

    boolean isVisibleFollower() {
        boolean elementPersistence;

        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("visibleFollower"))));
            elementPersistence = true;
        } catch (Exception e){
            elementPersistence = false;
        }
        return elementPersistence;
    }


    private void openInNewTab(WebElement element) {
        String selectLinkOpenInNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN);
        element.sendKeys(selectLinkOpenInNewTab);
    }


    void like(WebDriver driver) {
        WebElement subscriberLink = mediumWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("subscriberLink"))));
        String subscriberName = subscriberLink.getText();
        this.openInNewTab(subscriberLink);

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        try {
            WebElement firstPhoto = mediumWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("firstPhoto"))));
            firstPhoto.click();
            WebElement likeButton = mediumWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locators.get("likeButton"))));
            likeButton.click();

        } catch (Exception ex) {
            System.out.println("Невозможно лайкнуть фото " + subscriberName);
        }
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }
}