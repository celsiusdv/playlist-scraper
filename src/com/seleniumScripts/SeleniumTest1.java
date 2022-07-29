package com.seleniumScripts;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

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

        container=driver.findElement(new By.ById("contents"));
        containerVideos=container.findElements(new By.ById("content"));
        for(int i=0;i<containerVideos.size();i++){
            System.out.print("Title: "+containerVideos.get(i).findElement(new By.ById("video-title"))
                                                     .getAttribute("title"));
            System.out.print(" link: "+containerVideos.get(i).findElement(new By.ById("video-title"))
                    .getAttribute("href")+"\n");

        }
        this.closeDriverandBrowser();
    }

    public void closeDriverandBrowser(){
        try {Thread.sleep(100);} catch (InterruptedException e) {System.out.println("error from sleep thread");}
        System.out.println("closing browser");
        driver.close();
        driver.quit();
        System.out.println("browser and webdriver closed");
    }
}
