/**
 * 
 */
package com.mh.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author minhhoang
 *         
 */
public class DriverFactorySimulateIpad {
    private DriverFactorySimulateIpad() {
    
    }
    
    private static DriverFactorySimulateIpad instance = new DriverFactorySimulateIpad();
    
    public static DriverFactorySimulateIpad getInstance() {
        return instance;
    }
    
    ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() {
        @Override
        protected WebDriver initialValue() {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            System.setProperty("webdriver.chrome.driver", ".\\TestRun\\chromedriver.exe");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments(
                    "--user-agent=Mozilla/5.0 (iPad; CPU OS 9_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/9.0 Mobile/10A5355d Safari/8536.25");                    
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);            
            return new ChromeDriver(capabilities);
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
