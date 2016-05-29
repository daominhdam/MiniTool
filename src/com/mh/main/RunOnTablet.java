/**
 * 
 */
package com.mh.main;

import java.io.IOException;
import java.util.List;
import org.testng.TestNG;
import org.testng.collections.Lists;
import com.mh.notification.NotificationFrame;
import com.mh.utils.Constant;
import com.mh.utils.Utils;

/**
 * @author minhhoang
 *        
 */
public class RunOnTablet {
    
    /**
     * @param args
     * @throws IOException
     */
    public static void RunTablet()   {    
        Utils.getInstance().isExistTestSuiteMobile();       
        TestNG testng = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add(".\\TestRun\\TestRunOnMobile.xml");
        testng.setTestSuites(suites);
        testng.run();
        Constant.RUN_OPTIONS_VALUE=-1;
        NotificationFrame.showMsg(Constant.SUCCESSFULLY_MSG_TABLET);
    }
    
}
