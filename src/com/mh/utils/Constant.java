/**
 * 
 */
package com.mh.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Minh-Quan
 *         
 */
public class Constant {
    public static final String                       SUCCESSFULLY_MSG_DESKTOP          = "<html><p style=\" color:#DC143C\">Get java script errors successfully on Desktop.</p></html>";
    public static final String                       SUCCESSFULLY_MSG_TABLET           = "<html><p style=\" color:#DC143C\">Get java script errors successfully on Tablet.</p></html>";
    public static final String                       SUCCESSFULLY_MSG_GET_CONTENT      = "<html><p style=\" color:#DC143C\">Get cotent from source page successfully on.</p></html>";
    public static final String                       RUNNING_AUTO                      = "<html><p style=\" color:#DC143C\">Running get java script errors.</p></html>";
    public static final String                       ERROR_VALID_DATA                  = "<html><p style=\" color:#DC143C\">TestData.xlsx not containt url data</p><br>Please check:</br><br>   - Select Header Name with contain data set of URL  </br><br>   - Has data value of URL in excel file </br></html>";
    public static final String                       ERROR_GET_DATA                    = "<html>  -Select correct file excel<br>  -Input correct SheetName</br><br>  -Input correct Header Column Name for get data</br><br>  -Test Data cannot be null or empty</br></html>";
    public static final String                       ERROR_REGULAR_EXPRESSION_AND_DATA = "<html>  -Select correct file excel<br>  -Input correct SheetName</br><br>  -Input correct Header Column Name for get data</br><br>  -Regular Expression is not correct</br></html>";
    public static final String                       ERROR_REGULAR_EXPRESSION          = "<html>  -Regular Expression is not correct.</html>";
    public static final String                       ERROR_VALID_URL                   = "<html><p style=\" color:#DC143C\">TestData.xlsx has invalid url data</p><br>Please check:</br><br>   - Url data value has format 'https://[url value]' </br><br>   - Url data value has format 'http://[url value]' </br><br>   - Column has not cotain data </br>"
            + "<br>" + "Check on list of row below(Use delete function in excel for Null or Blank rows):</br>" + "<br>"
            + "%s" + "</br></html>";
    public static final String                       ERROR_VALID_DRIVER                = "<html>  Can not find: "
            + System.getProperty("user.dir") + "\\TestRun or"
            + "<br>  Can not find [chromedriver.exe] or [phantomjs.exe] in:[" + System.getProperty("user.dir")
            + "\\TestRun" + "]</br>" + "<br>  Please: </br>"
            + "<br> - Create TestRun folder and put [phantomjs.exe] [chromedriver.exe] into it</br>"
            + "<br> - Put [phantomjs.exe] [chromedriver.exe] [TestData.xlsx] into it</br></html>";
    public static final String						 ERROR_EXCEPTION				   = "<html>  - Has exception when start to run auto<br>%s</br></html>";
    public static final String                       SUCCESSFULLY_MSG_LINK_HTTP        = "<html><p style=\" color:#DC143C\">Link checker for HTTP sucessfully.</p></html>";
    public static final String                       SUCCESSFULLY_MSG_LINK_HTTPS       = "<html><p style=\" color:#DC143C\">Link checker for HTTPS sucessfully.</p></html>";
    public static String                             DATA_PATH;
    public static String                             SHEET_NAME;
    public static String                             HEADER_DATA_NAME                  = "";
    public static String                             TYPE_OF_LINK_CHECKER              = "";
    public static String                             GET_ROW_INVALID_URL;
    public static String                             REGULAR_EXPRESSION;
    public static ArrayList<HashMap<String, String>> TEST_DATA;
    public static int                                RUN_THREAD_1                      = 0;
    public static int                                RUN_THREAD_2                      = 0;
    public static int                                RUN_THREAD_3                      = 0;
    public static int                                RUN_THREAD_4                      = 0;
    public static int                                RUN_THREAD_5                      = 0;
    public static int                                TOTAL_ROW_RUN                     = 0;
    public static int                                RUN_OPTIONS_VALUE                 = -1;
    public static int                                MAX_THREAD                        = 40;
    public static int                                NUMBER_THREAD                     = 1;
    public static int                                TIME_OUT                          = 30;
    public static Boolean                            IS_HAS_DRIVER;
}
