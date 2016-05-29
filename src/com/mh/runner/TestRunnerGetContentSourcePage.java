/**
 * 
 */
package com.mh.runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.mh.driver.DriverFactoryPhantomjs;
import com.mh.utils.Constant;
import com.mh.utils.Utils;

/**
 * @author minhhoang
 *         
 */
public class TestRunnerGetContentSourcePage {
    public ArrayList<HashMap<String, String>> urlData = null;
    public String[]                           testData1, testData2, testData3;
                                              
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
        Utils.getInstance().killPhantomjsProcess();
    }
    
    @Test(dataProvider = "Page Run Thread 1")
    public void Thread1(String urlvalue) {
        PhantomJSDriver  driver = DriverFactoryPhantomjs.getInstance().getDriver();
        driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
        driver.get(urlvalue);      
        try {
            
            Utils.getInstance().getContent(urlvalue, driver.getPageSource(), Constant.REGULAR_EXPRESSION);
        }
        catch (Exception e) {                
            Reporter.log("Page error on: " + urlvalue + " -- " + e.getMessage());
            Reporter.log("=========================================================");           
        }        
        DriverFactoryPhantomjs.getInstance().removeDriver();       
    }
    
    @Test(dataProvider = "Page Run Thread 2")
    public void Thread2(String urlvalue) {      
        PhantomJSDriver  driver = DriverFactoryPhantomjs.getInstance().getDriver();
        driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
        driver.get(urlvalue);      
        try {
            
            Utils.getInstance().getContent(urlvalue, driver.getPageSource(), Constant.REGULAR_EXPRESSION);
        }
        catch (Exception e) {                
            Reporter.log("Page error on: " + urlvalue + " -- " + e.getMessage());
            Reporter.log("=========================================================");           
        }        
        DriverFactoryPhantomjs.getInstance().removeDriver();        
    }
    
    @Test(dataProvider = "Page Run Thread 3")
    public void Thread3(String urlvalue) {
        PhantomJSDriver  driver = DriverFactoryPhantomjs.getInstance().getDriver();
        driver.manage().timeouts().pageLoadTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
        driver.get(urlvalue);      
        try {
            
            Utils.getInstance().getContent(urlvalue, driver.getPageSource(), Constant.REGULAR_EXPRESSION);
        }
        catch (Exception e) {                
            Reporter.log("Page error on: " + urlvalue + " -- " + e.getMessage());
            Reporter.log("=========================================================");           
        }        
        DriverFactoryPhantomjs.getInstance().removeDriver();       
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
