/**
 * 
 */
package com.mh.runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.mh.driver.DriverFactorySimulateIpad;
import com.mh.utils.Constant;
import com.mh.utils.Utils;

/**
 * @author Minh-Quan
 *         
 */
public class TestRunnerOnMobile {
    public ArrayList<HashMap<String, String>> urlData = null;
    
    /**
     * @param args
     * @throws Exception
     * @throws IOException
     */
    @BeforeSuite
    public void setUp() throws Exception {
        urlData = Constant.TEST_DATA;                
        Utils.getInstance().getDistributed(urlData);
        Utils.getInstance().createCssColorReport();
    }
    
    @AfterSuite
    public void clearEnviroment() {
        Utils.getInstance().killChromeProcess();
    }
    
    @Test(dataProvider = "Page Run Thread 1")
    public void Thread1(String urlvalue) {
        String urlTemp;
        int pageHeight = 0, currentMosue = 0;
        urlTemp = urlvalue;
        WebDriver driver = DriverFactorySimulateIpad.getInstance().getDriver();
        driver.manage().window().maximize();
        driver.manage().window().setPosition(new Point(-2000, 0));
        driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
        try {
            driver.get(urlTemp);
            driver.manage().deleteAllCookies();
            if (Utils.getInstance().waitForPageLoad(driver)) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JavascriptExecutor js = (JavascriptExecutor) driver;
                pageHeight = (int) (long) js.executeScript(
                        "return Math.max(document.documentElement.clientHeight, document.body.scrollHeight, document.documentElement.scrollHeight, document.body.offsetHeight, document.documentElement.offsetHeight );");
                while (currentMosue < pageHeight) {
                    currentMosue = currentMosue + 250;
                    js.executeScript("javascript:window.scrollBy(0," + currentMosue + ")");
                    Utils.getInstance().delayMs();
                }
                Utils.getInstance().ExtractJSLogs(driver, driver.getTitle(), urlTemp);
                driver.manage().deleteAllCookies();
            }
            else {
                Reporter.log("Error on load page :" + urlTemp);
            }
        }
        catch (TimeoutException e) {
            e.printStackTrace();
            Reporter.log("Error on open page with this URL :" + urlTemp);
        }
        DriverFactorySimulateIpad.getInstance().removeDriver();
    }
    
    @Test(dataProvider = "Page Run Thread 2")
    public void Thread2(String urlvalue) {
        String urlTemp;
        int pageHeight = 0, currentMosue = 0;
        urlTemp = urlvalue;
        WebDriver driver = DriverFactorySimulateIpad.getInstance().getDriver();
        driver.manage().window().maximize();
        driver.manage().window().setPosition(new Point(-2000, 0));
        driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
        try {
            driver.get(urlTemp);
            driver.manage().deleteAllCookies();
            if (Utils.getInstance().waitForPageLoad(driver)) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JavascriptExecutor js = (JavascriptExecutor) driver;
                pageHeight = (int) (long) js.executeScript(
                        "return Math.max(document.documentElement.clientHeight, document.body.scrollHeight, document.documentElement.scrollHeight, document.body.offsetHeight, document.documentElement.offsetHeight );");
                while (currentMosue < pageHeight) {
                    currentMosue = currentMosue + 250;
                    js.executeScript("javascript:window.scrollBy(0," + currentMosue + ")");
                    Utils.getInstance().delayMs();
                }
                Utils.getInstance().ExtractJSLogs(driver, driver.getTitle(), urlTemp);
                driver.manage().deleteAllCookies();
            }
            else {
                Reporter.log("Error on load page :" + urlTemp);
            }
        }
        catch (TimeoutException e) {
            e.printStackTrace();
            Reporter.log("Error on open page with this URL :" + urlTemp);
        }
        DriverFactorySimulateIpad.getInstance().removeDriver();
    }
    
    @Test(dataProvider = "Page Run Thread 3")
    public void Thread3(String urlvalue) {
        String urlTemp;
        int pageHeight = 0, currentMosue = 0;
        urlTemp = urlvalue;
        WebDriver driver = DriverFactorySimulateIpad.getInstance().getDriver();
        driver.manage().window().setSize(new Dimension(1024,768));
        driver.manage().window().setPosition(new Point(-2000, 0));
        driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
        try {
            driver.get(urlTemp);
            driver.manage().deleteAllCookies();
            if (Utils.getInstance().waitForPageLoad(driver)) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JavascriptExecutor js = (JavascriptExecutor) driver;
                pageHeight = (int) (long) js.executeScript(
                        "return Math.max(document.documentElement.clientHeight, document.body.scrollHeight, document.documentElement.scrollHeight, document.body.offsetHeight, document.documentElement.offsetHeight );");
                while (currentMosue < pageHeight) {
                    currentMosue = currentMosue + 250;
                    js.executeScript("javascript:window.scrollBy(0," + currentMosue + ")");
                    Utils.getInstance().delayMs();
                }
                Utils.getInstance().ExtractJSLogs(driver, driver.getTitle(), urlTemp);
                driver.manage().deleteAllCookies();
            }
            else {
                Reporter.log("Error on load page :" + urlTemp);
            }
        }
        catch (TimeoutException e) {
            e.printStackTrace();
            Reporter.log("Error on open page with this URL :" + urlTemp);
        }
        DriverFactorySimulateIpad.getInstance().removeDriver();
    }
    
    @DataProvider(name = "Page Run Thread 1")
    public Object[][] dataProviderMethod1() {
        int row = Constant.RUN_THREAD_1;
        Object[][] data = new Object[row][1];
        for (int n = 0; n < Constant.RUN_THREAD_1; n++) {
            data[n][0] = urlData.get(n).get(Constant.HEADER_DATA_NAME);
        }
        return data;
    }
    
    @DataProvider(name = "Page Run Thread 2")
    public Object[][] dataProviderMethod2() {
        int row = Constant.RUN_THREAD_2 - Constant.RUN_THREAD_1;
        Object[][] data = new Object[row][1];
        for (int n = 0; n < row; n++) {
            data[n][0] = urlData.get(n + Constant.RUN_THREAD_1).get(Constant.HEADER_DATA_NAME);
        }
        return data;
    }
    
    @DataProvider(name = "Page Run Thread 3")
    public Object[][] dataProviderMethod3() {
        int row = Constant.TOTAL_ROW_RUN - Constant.RUN_THREAD_2;
        Object[][] data = new Object[row][1];
        for (int n = 0; n < row; n++) {
            data[n][0] = urlData.get(n + Constant.RUN_THREAD_2).get(Constant.HEADER_DATA_NAME);
        }
        return data;
    }
}
