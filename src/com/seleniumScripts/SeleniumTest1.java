package com.seleniumScripts;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumTest1 {
  WebDriver driver;
  WebElement container;
  List<WebElement>containerVideos;
    public SeleniumTest1(){
        System.setProperty("webdriver.chrome.driver","src/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/usr/bin/brave-browser");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu ");
        options.addArguments("--no-sandbox");
        driver=new ChromeDriver(options);
        driver.get("https://www.youtube.com/playlist?list=PLUmlZuWit0rSZGafmtYOKstSfLw7pz8Ip");
        System.out.println(driver.getTitle());

        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contents")));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        container=driver.findElement(new By.ById("contents"));
        containerVideos=container.findElements(new By.ById("content"));
        for(int i=0;i<containerVideos.size();i++){
            System.out.println("Title: "+containerVideos.get(i).findElement(By.id("video-title"))
                                                      .getAttribute("title"));
            System.out.println("Link: "+containerVideos.get(i).findElement(By.id("video-title"))
                                                      .getAttribute("href"));
            System.out.println("thumbnail: "+containerVideos.get(i).findElement(By.tagName("img")).getAttribute("src"));
            System.out.println("-----------------------------------------------------------------------------------------------");
            js.executeScript("arguments[0].scrollIntoView();", containerVideos.get(i));
        }
        this.closeDriverandBrowser();
    }

    public void closeDriverandBrowser(){
        try {Thread.sleep(100);} catch (InterruptedException e) {System.out.println("error from sleep thread");}
        System.out.println("\nclosing browser");
        driver.close();
        driver.quit();
        System.out.println("browser and webdriver closed");
    }
}
