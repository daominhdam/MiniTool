/**
 * 
 */
package com.mh.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author minhhoang
 *         
 */
public class DriverFactoryChrome {
    private DriverFactoryChrome() {
    }
    
    private static DriverFactoryChrome instance = new DriverFactoryChrome();
    
    public static DriverFactoryChrome getInstance() {
        return instance;
    }
    
    ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() {
        @Override
        protected WebDriver initialValue() {
            System.setProperty("webdriver.chrome.driver", ".\\TestRun\\chromedriver.exe");
            return new ChromeDriver();
        }
    };
    
    public WebDriver getDriver() {
        return driver.get();
    }
    
    public void removeDriver() {
        driver.get().quit();
        driver.remove();
    }
    
}
