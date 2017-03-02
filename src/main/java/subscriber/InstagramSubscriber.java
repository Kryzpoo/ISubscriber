package subscriber;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

class InstagramSubscriber {

    private InstagramManager instagramManager;

    static void start(InstagramManager instagramManager) {

        InstagramSubscriber instagramSubscriber = new InstagramSubscriber(instagramManager);
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
        instagramManager.getSearchPage();

        // TODO: 31.12.2016 Костыль для Ивана
        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        instagramManager.findGroup();
        instagramManager.openFollowersPage();
        String subscriberLiElement = instagramManager.getSubscriberLiElement();

        int elementForScrollStart = 5;

        while (followCount >= 0) {
            if (instagramManager.isVisibleSubscriber()) {
                instagramManager.like(driver);
                instagramSubscriber.subscribe(shortWait);
                followCount -= 1;
                System.out.println("Осталось до конца подписки: " + followCount);
                instagramManager.randomWait(random, startTime, endTime);
            } else {
                instagramManager.scrollDown(jsExecutor, subscriberLiElement, elementForScrollStart);
                elementForScrollStart += 5;
            }
        }
        driver.quit();
    }


    // Конструктор
    private InstagramSubscriber(InstagramManager _instagramManager) {
        this.instagramManager = _instagramManager;

        instagramManager.getProperties();

        System.out.println();
        System.out.println("Используемые настройки:");
        System.out.println("username = " + instagramManager.getUsername());
        System.out.println("password = " + instagramManager.getPassword());
        System.out.println("groupName = " + instagramManager.getGroupName());
        System.out.println("followCount = " + instagramManager.getFollowCount());
        System.out.println("startInterval = " + instagramManager.getStartInterval());
        System.out.println("endInterval = " + instagramManager.getEndInterval());
    }

    private void subscribe(WebDriverWait shortWait) {
        WebElement subscribeButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(instagramManager.getSubscriberButton())));
        subscribeButton.click();
    }
}