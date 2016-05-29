/**
 * 
 */
package com.mh.driver;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author minhhoang
 *        
 */
public class DriverFactoryPhantomjs {
    private DriverFactoryPhantomjs() {
    }
    
    private static DriverFactoryPhantomjs instance = new DriverFactoryPhantomjs();
    
    public static DriverFactoryPhantomjs getInstance() {
        return instance;
    }
    
    ThreadLocal<PhantomJSDriver> driver = new ThreadLocal<PhantomJSDriver>() {
        @Override
        protected PhantomJSDriver initialValue() {
            System.setProperty("phantomjs.binary.path", ".\\TestRun\\phantomjs.exe");                          
            DesiredCapabilities caps = DesiredCapabilities.phantomjs();            
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
            ArrayList<String> cliArgsCap = new ArrayList<String>();
            cliArgsCap.add("--webdriver-loglevel=NONE");                
            cliArgsCap.add("--web-security=false");
            cliArgsCap.add("--ssl-protocol=any");
            cliArgsCap.add("--ignore-ssl-errors=true");
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
            Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);            
            return new PhantomJSDriver(caps);
        }
    };
    
    public PhantomJSDriver getDriver() {
        return driver.get();
    }
    
    public void removeDriver() {
        driver.get().quit();
        driver.remove();
    }
    
}