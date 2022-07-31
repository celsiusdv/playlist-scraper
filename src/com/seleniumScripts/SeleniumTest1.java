package com.seleniumScripts;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumTest1 {
  WebDriver driver;
  JavascriptExecutor js;
  WebElement container,player;
  List<WebElement>containerVideos,buttons;

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
        driver.get("https://www.youtube.com/playlist?list=PLUmlZuWit0rTAnohQA5C1kwl2qD21obOj");
        js = (JavascriptExecutor) driver;

        System.out.println(driver.getTitle());

        try{
            this.getLinksAndThumbnails();
            this.getVideoSource();
        }catch (Exception e){
            this.closeDriverandBrowser();
            System.out.println("error after extracting links");
        }finally{
            this.closeDriverandBrowser();
            System.out.println("all extractions completed successfully");
        }
    }
    public int getContentSize(){
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contents") ) );
        container=driver.findElement(By.id("stats"));
        String total=container.findElement(By.tagName("span")).getText();
        int totalVideos=Integer.parseInt(total);
        return totalVideos;
    }
    public void getLinksAndThumbnails() {//1º------------------------------- getting thumbnails titles and links
        this.getContentSize();
        int rows=0;
        do{//expanding de div to the bottom to increase the size of containerVideos
            container=driver.findElement(new By.ById("contents"));
            containerVideos=container.findElements(new By.ById("content") );
            new Actions(driver).sendKeys(Keys.PAGE_DOWN).perform();
            rows+=5;
            System.out.println("updating container size: "+containerVideos.size()+" current index: "+rows);
        }while(rows<this.getContentSize());

        for(int i=0;i<containerVideos.size();i++){// ---------------------------------------traversing web elements
            System.out.println("Title: "+containerVideos.get(i).findElement(By.id("video-title"))
                    .getAttribute("title"));
            System.out.println("Link: "+containerVideos.get(i).findElement(By.id("video-title"))
                    .getAttribute("href"));
            System.out.println("thumbnail: "+containerVideos.get(i).findElement(By.tagName("img"))
                    .getAttribute("src"));
            System.out.println("----------------------------------");
            js.executeScript("arguments[0].scrollIntoView();", containerVideos.get(i));//---scrolling down to the last element

            if(i==containerVideos.size()-1){// -------------------------------------scrolling up to the first element
                js.executeScript("window.scrollTo(0, -document.body.scrollHeight)");
                containerVideos.get(0).findElement(By.id("video-title")).click();//once scrolled up, click on the first video
            }
        }
    }
    public void getVideoSource() throws InterruptedException {//2º -----------------getting blob src and clicking next button
        System.out.println("getting blobs and check container size"+containerVideos.size());
        for(int i=0;i<containerVideos.size();i++){
            Thread sourceVideo=new Thread(new Runnable() {
                public void run() {
                    WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(15));
                    wait.until(ExpectedConditions.visibilityOf(player=driver.findElement(By.tagName("video") ) ) );
                    System.out.println(player.getAttribute("src"));
                }});
            sourceVideo.start();
            sourceVideo.join();
            Thread.sleep(200);
            Thread clickNext=new Thread(new Runnable() {
                public void run() {
                    player=driver.findElement(By.className("ytp-left-controls"));
                    buttons=player.findElements(By.tagName("a"));
                    System.out.println("clicking next element");
                    buttons.get(1).click();
                }});
            clickNext.start();
            clickNext.join();
        }
    }
    public void closeDriverandBrowser(){
        try {Thread.sleep(100);} catch (InterruptedException e) {System.out.println("error from sleep thread");}
        System.out.println("\nclosing browser");
        driver.close();
        driver.quit();
        System.out.println("browser and webdriver closed");
    }
}