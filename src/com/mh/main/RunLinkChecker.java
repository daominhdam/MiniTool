package com.mh.main;

import java.util.List;
import org.testng.TestNG;
import org.testng.collections.Lists;
import com.mh.notification.NotificationFrame;
import com.mh.utils.Constant;
import com.mh.utils.Utils;

public class RunLinkChecker {
    
    public static void LinkChecker()   {        
        Utils.getInstance().isExistTestSuiteLinkChecker();   
        TestNG testng = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add(".\\TestRun\\TestRunLinkChecker.xml");        
        testng.setTestSuites(suites);
        testng.run();
        Constant.RUN_OPTIONS_VALUE=-1;
        NotificationFrame.showMsg(Constant.SUCCESSFULLY_MSG_LINK_HTTPS);        
    }
    
}
