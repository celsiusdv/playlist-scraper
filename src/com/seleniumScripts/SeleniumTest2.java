package com.seleniumScripts;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumTest2 {
    WebDriver driver;
    JavascriptExecutor js;
    WebElement playlist,player;
    List<WebElement> videos,playerControls;
    public SeleniumTest2() {
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
        System.out.println(driver.getTitle());
        try{
            this.enteringPlaylist();
            this.extractTitleAndSource();
        }catch (InterruptedException | WebDriverException e){
            e.getCause();
        }finally {
            this.closeDriverandBrowser();
            System.out.println("drivers and browser closed successfully");
        }
    }
    public void enteringPlaylist(){//1º entering the playlist and clicking on the first video
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contents") ) );
        playlist=driver.findElement(By.id("contents"));
        videos=playlist.findElements(By.id("content") );
        System.out.println("clicking on first element");
        videos.get(0).findElement(By.id("video-title")).click();
    }
    public void extractThumbnails(int index){//2º extracting thumbnails from the playlist
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(player=driver.findElement(By.tagName("video") ) ) );
        System.out.println("video player loaded");

        playlist=driver.findElement(By.id("items"));
        videos=playlist.findElements(By.xpath("//*[@id=\"playlist-items\"]"));

        System.out.println("videos items list size: "+videos.size());
        System.out.println("thumbnail link: "+videos.get(index).findElement(By.tagName("img")).getAttribute("src"));
    }

    public void extractTitleAndSource() throws InterruptedException, WebDriverException {//3º getting sources from video
        int index=0;
            Thread sourceVideo=new Thread(() -> {
                try {
                    Thread.sleep(400);
                    this.extractThumbnails(index);
                    System.out.println("blob link= "+player.getAttribute("src"));

                } catch (InterruptedException e) {
                    System.out.println("error in sourceVideo thread: no elements found");
                    throw new RuntimeException(e);
                }
            });
            sourceVideo.start();
            sourceVideo.join();
            Thread clickNext=new Thread(() -> {//get title and click in the second element (next button) from playerControls list
                System.out.println(driver.getTitle());
                player=driver.findElement(By.className("ytp-left-controls"));
                playerControls=player.findElements(By.tagName("a"));//all controls (previous,play, next buttons)are loaded in playerControl list
                System.out.println("clicking next element");
                playerControls.get(1).click();
            });
            clickNext.start();
            clickNext.join();
    }

    public void closeDriverandBrowser(){
        try {Thread.sleep(100);} catch (InterruptedException e) {System.out.println("error from sleep thread");}
        System.out.println("\nclosing browser");
        driver.close();
        driver.quit();
        System.out.println("browser and webdriver closed");
    }
}
