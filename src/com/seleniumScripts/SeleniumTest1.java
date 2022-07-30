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
  JavascriptExecutor js;
  WebElement container,player;

  List<WebElement>containerVideos;

    public SeleniumTest1(){
        System.setProperty("webdriver.chrome.driver","src/chromedriver");
        ChromeOptions options = new ChromeOptions();

        options.setBinary("/usr/bin/brave-browser");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--autoplay-policy=no-user-gesture-required");
        options.addArguments("--mute-audio");
        options.addArguments("--disable-gpu ");
        options.addArguments("--no-sandbox");
        driver=new ChromeDriver(options);
        driver.get("https://www.youtube.com/playlist?list=PLUmlZuWit0rSZGafmtYOKstSfLw7pz8Ip");
        js = (JavascriptExecutor) driver;

        System.out.println(driver.getTitle());

        try{
            this.getLinksandThumbnails();
            this.getVideoSource();
        }catch (Exception e){
            this.closeDriverandBrowser();
            System.out.println("error after extracting links");
        }finally{
            this.closeDriverandBrowser();
            System.out.println("all extractions completed successfully");
        }
    }

    public void getLinksandThumbnails(){//1º------------------------------- getting thumbnails titles and links
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contents")));

        container=driver.findElement(new By.ById("contents"));
        containerVideos=container.findElements(new By.ById("content"));

        for(int i=0;i<containerVideos.size();i++){// ---------------------------------------traversing web elements
            System.out.println("Title: "+containerVideos.get(i).findElement(By.id("video-title"))
                    .getAttribute("title"));
            System.out.println("Link: "+containerVideos.get(i).findElement(By.id("video-title"))
                    .getAttribute("href"));
            System.out.println("thumbnail: "+containerVideos.get(i).findElement(By.tagName("img"))
                    .getAttribute("src"));
            System.out.println("----------------------------------");
            js.executeScript("arguments[0].scrollIntoView();", containerVideos.get(i));

            if(i==containerVideos.size()-1){// -------------------------------------------------scrolling up to the first element
                js.executeScript("window.scrollTo(0, -document.body.scrollHeight)");
                containerVideos.get(0).findElement(By.id("video-title")).click();
            }
        }
    }
    public void getVideoSource(){
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(player=driver.findElement(By.tagName("video"))));
        System.out.println(player.getAttribute("src"));

    }
    public void closeDriverandBrowser(){
        try {Thread.sleep(100);} catch (InterruptedException e) {System.out.println("error from sleep thread");}
        System.out.println("\nclosing browser");
        driver.close();
        driver.quit();
        System.out.println("browser and webdriver closed");
    }
}
