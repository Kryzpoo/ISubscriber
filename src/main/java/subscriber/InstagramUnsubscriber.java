package subscriber;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

class InstagramUnsubscriber {

    private InstagramManager instagramManager;

    static void start(InstagramManager instagramManager) {

        InstagramUnsubscriber instagramUnsubscriber = new InstagramUnsubscriber(instagramManager);
        WebDriver driver = instagramManager.initializeDriver();
        JavascriptExecutor jsExecutor = instagramManager.getJsExecutor(driver);
        WebDriverWait shortWait = new WebDriverWait(driver, 2);
        Random random = new Random();
        int startTime = instagramManager.getStartInterval();
        int endTime = instagramManager.getEndInterval();
        int followCount = instagramManager.getFollowCount();

        instagramManager.getSite(driver);
        instagramManager.loginOnSite(instagramManager.getUsername(), instagramManager.getPassword());
        instagramManager.handleBanner();

        // TODO: 31.12.2016 Костыль для Ивана
        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        instagramManager.getPersonalPage();
        instagramManager.openFollowingsPage();
        String unsubscriberLiElement = instagramManager.getUnsubscriberLiElement();

        int elementForScrollStart = 1;

        while (followCount > 0) {
            if (instagramManager.isVisibleFollower()) {
                instagramUnsubscriber.unsubscribe(shortWait);
                followCount -= 1;
                System.out.println("Осталось до конца отписки: " + followCount);
                instagramManager.randomWait(random, startTime, endTime);
            } else {
                instagramManager.scrollDown(jsExecutor, unsubscriberLiElement, elementForScrollStart);
                elementForScrollStart += 1;
            }
        }
        driver.quit();
    }


    // Конструктор
    private InstagramUnsubscriber(InstagramManager _instagramManager) {
        this.instagramManager = _instagramManager;

        instagramManager.getProperties();

        System.out.println();
        System.out.println("Используемые настройки:");
        System.out.println("username = " + instagramManager.getUsername());
        System.out.println("password = " + instagramManager.getPassword());
        System.out.println("followCount = " + instagramManager.getFollowCount());
        System.out.println("startInterval = " + instagramManager.getStartInterval());
        System.out.println("endInterval = " + instagramManager.getEndInterval());
    }

    private void unsubscribe(WebDriverWait shortWait) {
        WebElement unsubscribeButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(instagramManager.getUnsubscriberButton())));
        unsubscribeButton.click();
    }
}