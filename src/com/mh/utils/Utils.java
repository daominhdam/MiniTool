/**
 * 
 */
package com.mh.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

/**
 * @author Minh-Quan
 *         
 */
public class Utils {
    private static XSSFWorkbook excelWorkbook;
    public static WebDriver     driver;
    public static File          file = null;
                                     
    private Utils() {
    }
    
    private static Utils instance = new Utils();
    
    public static Utils getInstance() {
        return instance;
    }
    
    /**
     * This method get all data in excel file
     * 
     * @param pathValue
     * @param sheetName
     * @return
     */
    public List<HashMap<String, String>> getDataExcel(String pathValue, String sheetName) {
        ArrayList<HashMap<String, String>> dataValue = new ArrayList<HashMap<String, String>>();
        try {
            FileInputStream fs = new FileInputStream(pathValue);
            excelWorkbook = new XSSFWorkbook(fs);
            XSSFSheet excelSheet = excelWorkbook.getSheet(sheetName);
            Row headerRow = excelSheet.getRow(0);
            for (int i = 1; i < excelSheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = excelSheet.getRow(i);
                HashMap<String, String> currentHash = new HashMap<String, String>();
                for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
                    
                    try {
                        Cell currentCell = currentRow.getCell(j);
                        switch (currentCell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                currentHash.put(headerRow.getCell(j).getStringCellValue(),
                                        currentCell.getStringCellValue());
                                break;
                        }
                    }
                    catch (NullPointerException e) {
                        if (!(headerRow.getCell(j) == null) && headerRow.getCell(j).getCellType() == 1)
                            currentHash.put(headerRow.getCell(j).getStringCellValue(), "null");
                    }
                }
                dataValue.add(currentHash);
            }
            fs.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return dataValue;
    }
    
    /**
     * This method call wait for page load in webdriver
     * 
     * @param driver
     * @return
     */
    public Boolean waitForPageLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(pageLoadCondition);
        Boolean checkPageLoad = ((JavascriptExecutor) driver).executeScript("return document.readyState")
                .equals("complete");
        return checkPageLoad;
    }
    
    /**
     * This method write all console log WebDriver get on browser into .log
     * 
     * @param driver
     * @param pageTitle
     * @param urlData
     */
    public void ExtractJSLogs(WebDriver driver, String pageTitle, String urlData) {
        String checkDuplicate = "";
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        Reporter.log("Check script error on: " + pageTitle.toString().toUpperCase());
        Reporter.log("URL: " + urlData.toString());
        for (LogEntry entry : logEntries) {
            String temp = (new Date(entry.getTimestamp()).toString() + ": " + entry.getLevel().toString() + " -- "
                    + entry.getMessage().toString());
            if (!temp.equals(checkDuplicate)) {
                Reporter.log(temp);
            }
            checkDuplicate = temp;
        }
    }
    
    /**
     * This method call cmd with do end all chromedriver.exe process
     */
    public void killChromeProcess() {
        try {
            Process pIED1 = Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            pIED1.waitFor();
        }
        catch (Exception e) {
            System.out.println("Can not kill chrome driver on task manager");
        }
    }
    
    /**
     * This method call cmd with do end all IEDriverServer.exe process
     */
    public void killIEDriverProcess() {
        try {
            Process pIED1 = Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
            pIED1.waitFor();
        }
        catch (Exception e) {
            System.out.println("Can not kill IE driver on task manager");
        }
    }
    
    /**
     * This method call cmd with do end all phantomjs.exe process
     */
    public void killPhantomjsProcess() {
        try {
            Process pIED1 = Runtime.getRuntime().exec("taskkill /F /IM phantomjs.exe");
            pIED1.waitFor();
        }
        catch (Exception e) {
            System.out.println("Can not kill phantomJS driver on task manager");
        }
    }
    
    /**
     * This method check user has TestSuite for run in get desktop console log,
     * if not it will create one
     */
    public void isExistTestSuiteDesktop() {
        File fs = new File(".\\TestRun\\TestRunOnDesktop.xml");
        try {
            if (!fs.exists()) {
                
                fs.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Get Console error log Desktop\" parallel=\"methods\" thread-count=\"3\">" + "\r\n");
                writer.write("  <test name=\"Get Console error log Desktop\">" + "\r\n");
                writer.write("<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerOnDesktop\" />" + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
                
            }
            else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Get Console error log Desktop\" parallel=\"methods\" thread-count=\"3\">" + "\r\n");
                writer.write("  <test name=\"Get Console error log Desktop\">" + "\r\n");
                writer.write("<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerOnDesktop\" />" + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
                
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method check user has TestSuite for run in get mobile console log,
     * if not it will create one
     */
    public void isExistTestSuiteMobile() {
        File fs = new File(".\\TestRun\\TestRunOnMobile.xml");
        try {
            if (!fs.exists()) {
                fs.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Get Console error log iPad\" parallel=\"methods\" thread-count=\"3\">" + "\r\n");
                writer.write("<test name=\"Get Console error log iPad\" >" + "\r\n");
                writer.write("<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerOnMobile\" />" + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
            }
            else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Get Console error log iPad\" parallel=\"methods\" thread-count=\"3\">" + "\r\n");
                writer.write("<test name=\"Get Console error log iPad\">" + "\r\n");
                writer.write("<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerOnMobile\" />" + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * This method check user has TestSuite for run in get text from source
     * code, if not it will create one
     */
    public void isExistTestSuiteGetConTentSourcePage() {
        File fs = new File(".\\TestRun\\TestRunGetContentSourcePage.xml");
        try {
            if (!fs.exists()) {
                fs.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Get Content Source Page\" parallel=\"methods\" thread-count=\"3\">"
                        + "\r\n");
                writer.write("<test name=\"Get Content Source Page\" >" + "\r\n");
                writer.write("<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerGetContentSourcePage\" />"
                        + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
            }
            else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Get Content Source Page\" parallel=\"methods\" thread-count=\"3\">"
                        + "\r\n");
                writer.write("<test name=\"Get Content Source Page\">" + "\r\n");
                writer.write("<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerGetContentSourcePage\" />"
                        + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method check user has TestSuite for run in linkchecker, if not it
     * will create one
     */
    public void isExistTestSuiteLinkChecker() {
        File fs = new File(".\\TestRun\\TestRunLinkChecker.xml");
        try {
            if (!fs.exists()) {
                fs.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Link Checker Report\" parallel=\"methods\" thread-count=\"4\">" + "\r\n");
                writer.write("<test name=\"Link Checker Report\" >" + "\r\n");
                writer.write(
                        "<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerResponseCode\" />" + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
            }
            else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fs));
                writer.write("<suite name=\"Link Checker Report\" parallel=\"methods\" thread-count=\"4\">" + "\r\n");
                writer.write("<test name=\"Link Checker Report\" >" + "\r\n");
                writer.write(
                        "<classes>" + "\r\n" + "  <class name=\"com.mh.runner.TestRunnerResponseCode\" />" + "\r\n");
                writer.write(" </classes>" + "\r\n" + "</test>" + "\r\n" + "</suite>");
                writer.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method will check all data get in excel is correct with URL format
     * 
     * @param checkData
     * @return
     */
    public Boolean isValidUrl(ArrayList<HashMap<String, String>> checkData) {
        ArrayList<HashMap<String, String>> checkValidData = checkData;
        Boolean isValidUrl = true;
        String checkValidUrl = "";
        Constant.GET_ROW_INVALID_URL = new String();
        int count = 2;
        if (checkData.size() > 0) {
            for (HashMap<String, String> map : checkValidData) {
                try {
                    checkValidUrl = map.get(Constant.HEADER_DATA_NAME).toLowerCase().substring(0, 10);
                    if ((!(checkValidUrl.contains("http://") || checkValidUrl.contains("https://")))) {
                        isValidUrl = false;
                        Constant.GET_ROW_INVALID_URL = Constant.GET_ROW_INVALID_URL + "[" + count + "] ";
                    }
                    count = count + 1;
                }
                catch (Exception e) {
                    Constant.GET_ROW_INVALID_URL = Constant.GET_ROW_INVALID_URL + "[" + count + "] ";
                    count = count + 1;
                    isValidUrl = false;
                }
            }
        }
        else
            isValidUrl = false;
        return isValidUrl;
    }
    
    /**
     * This method check if user already has folder contain driver for run
     */
    public boolean isSetUpEnviroment() {
        File fs = new File(".\\TestRun");
        Boolean isHasDriver;
        if (!fs.exists()) {
            isHasDriver = false;
        }
        else
            isHasDriver = true;
        return isHasDriver;
    }
    
    /**
     * This method check if user already has chomredriver and phantomjs driver
     * in folder testrun
     */
    public boolean isHasDriver() {
        File fs1 = new File(".\\TestRun\\chromedriver.exe");
        File fs2 = new File(".\\TestRun\\phantomjs.exe");
        Boolean isHasDriver;
        if (!(fs1.exists() && fs2.exists())) {
            isHasDriver = false;
        }
        else
            isHasDriver = true;
        return isHasDriver;
    }
    
    /**
     * This method will distributed test data into multiple for run
     */
    public void getDistributed(ArrayList<HashMap<String, String>> checkData) {
        int rowRunPerThread = 0;
        Constant.TOTAL_ROW_RUN = checkData.size();
        rowRunPerThread = Constant.TOTAL_ROW_RUN / 3;
        Constant.RUN_THREAD_1 = rowRunPerThread;
        Constant.RUN_THREAD_2 = rowRunPerThread * 2;
        Constant.RUN_THREAD_3 = rowRunPerThread * 3;
    }
    
    public void createCssColorReport() {
        File fileCss = new File(".\\test-output\\testng.css");
        File folderReprot = new File(".\\test-output");
        if (!folderReprot.exists()) {
            folderReprot.mkdirs();
            changeReportColor();
        }
        else {
            if (!fileCss.exists()) {
                changeReportColor();
            }
            else {
                changeReportColor();
            }
        }
    }
    
    private void changeReportColor() {
        File fileCss = new File(".\\test-output\\testng.css");
        try {
            fileCss.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileCss));
            writer.write(".invocation-failed,  .test-failed  { background-color: #ffc2b3; }" + "\r\n");
            writer.write(".invocation-percent, .test-percent { background-color: #006600; }" + "\r\n");
            writer.write(".invocation-passed,  .test-passed  { background-color: #98FB98; }" + "\r\n");
            writer.write(".invocation-skipped, .test-skipped { background-color: #CCCC00; }" + "\r\n");
            writer.write(".main-page {  font-size: x-large;}" + "\r\n");
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param pageTitle
     * @param urlRun
     * @param pageSource
     * @param regurlarExpression
     *            Ex: String urlPattern =
     *            "\\\"((http):((//))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)\\\"";
     *            for get all http or https on view page source
     */
    public void getContent(String url, String pageSource, String regurlarExpression) {
        List<String> resultURL = new ArrayList<String>();
        if (pageSource != null) {
            String urlPattern = regurlarExpression;
            Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(pageSource);
            Reporter.log("=========================================================");
            Reporter.log("Page: " + url);
            while (m.find()) {
                resultURL.add(pageSource.substring(m.start(0), m.end(0)));
            }
            for (String urlGet : resultURL) {
                Reporter.log("URL :" + urlGet);
            }
            Reporter.log("Total contents found: " + resultURL.size());
        }
        else
            Reporter.log("Please input source page to get http URL");
        Reporter.log("=========================================================");
    }
    
    public List<String> getLinkChecker(WebDriver driver) {
        List<String> resultURL = new ArrayList<String>();
        List<WebElement> elementList = new ArrayList<WebElement>();
        elementList = driver.findElements(By.tagName("img"));
        String notLink = "javascript";
        for (WebElement elementTemp : elementList) {
            String temp = elementTemp.getAttribute("src");
            if ((temp != null && !temp.contains(notLink))) {
                resultURL.add(temp);
            }
        }
        elementList.addAll(driver.findElements(By.tagName("a")));
        for (WebElement elementTemp : elementList) {
            String temp = elementTemp.getAttribute("href");
            if ((temp != null && !temp.contains(notLink))) {
                resultURL.add(temp);
            }
        }
        return resultURL;
    }
    
    public void trustConnectionHttps() {
        
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e) {
            Reporter.log(e.getMessage());
        }
    }
    
    public void delayMs() {
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public Boolean isCorrectRegular(String regularString) {
        Boolean check;
        try {
            Pattern.compile(regularString);
            check = true;
        }
        catch (PatternSyntaxException e) {
            check = false;
        }
        return check;
    }
    
    public List<String> getAllSheetName() {
        List<String> sheetNameReturn = null;
        try {
            File myFile;
            myFile = new File(Constant.DATA_PATH);
            XSSFWorkbook wb = new XSSFWorkbook(myFile);
            sheetNameReturn = new ArrayList<String>();
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                sheetNameReturn.add(wb.getSheetName(i));
            }
            wb.close();
        }
        catch (Exception e) {
        }
        return sheetNameReturn;
    }
    
    public List<String> getAllHeaderName() {
        List<String> headerNameReturn = null;
        try {
            File myFile;
            myFile = new File(Constant.DATA_PATH);
            XSSFWorkbook wb = new XSSFWorkbook(myFile);
            XSSFSheet excelSheet = wb.getSheet(Constant.SHEET_NAME);
            headerNameReturn = new ArrayList<String>();
            Iterator<Cell> headerCell = excelSheet.getRow(0).cellIterator();
            while (headerCell.hasNext()) {
                try {
                    Cell currentCell = headerCell.next();
                    switch (currentCell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            headerNameReturn.add(currentCell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            headerNameReturn.add(Double.toString(currentCell.getNumericCellValue()));
                            break;
                    }
                }
                catch (NullPointerException e) {
                    if ((headerCell.next() == null))
                        headerNameReturn.add("null");
                }
            }
            wb.close();
        }
        catch (Exception e) {
        }
        return headerNameReturn;
    }
}
