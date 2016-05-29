package com.mh.notification;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import com.mh.main.RunGetContentSourcePage;
import com.mh.main.RunLinkChecker;
import com.mh.main.RunOnDesktop;
import com.mh.main.RunOnTablet;
import com.mh.utils.Constant;
import java.awt.Dialog.ModalExclusionType;

public class NotificationFrame extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static String     msgValue         = "";
    /**
     * 
     */
    private static JPanel     contentPanel     = new JPanel();
    static NotificationFrame  frame;
                              
    /**
     * Launch the application.
     */
    public static void showMsg(String msg) {
        EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                setMsg(msg);
                try {
                    if (frame == null) {
                        frame = new NotificationFrame();
                    }
                    frame.setVisible(true);
                    if (Constant.GET_ROW_INVALID_URL != null) {
                        if (Constant.GET_ROW_INVALID_URL.length() > 50) {
                            frame.setResizable(true);
                        }
                        else {
                            frame.setResizable(false);
                            frame.setBounds(100, 100, 350, 180);
                        }
                    }
                    else {
                        frame.setResizable(false);
                        frame.setBounds(100, 100, 350, 180);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the frame.
     */
    private NotificationFrame() {
        setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {                
                if (frame.isVisible()) {
                    if (Constant.RUN_OPTIONS_VALUE == 1 && MainGui.isValidData && Constant.IS_HAS_DRIVER
                            && MainGui.isValidURL) {
                        frame.dispose();
                        RunOnDesktop.RunDesktop();
                    }
                    else if (Constant.RUN_OPTIONS_VALUE == 2 && MainGui.isValidData && MainGui.isValidURL
                            && Constant.IS_HAS_DRIVER) {
                            
                        frame.dispose();
                        RunOnTablet.RunTablet();
                        
                    }
                    else if (Constant.RUN_OPTIONS_VALUE == 3 && MainGui.isValidData && MainGui.isValidURL
                            && Constant.IS_HAS_DRIVER) {
                            
                        frame.dispose();
                        RunLinkChecker.LinkChecker();
                        
                    }
                    else if (Constant.RUN_OPTIONS_VALUE == 4 && MainGui.isValidData && MainGui.isValidRegular
                            && MainGui.isValidURL && Constant.IS_HAS_DRIVER) {
                            
                        frame.dispose();
                        RunGetContentSourcePage.GetContentSourcePage();
                    }                   
                    else
                        frame.dispose();
                }
            }
        });
        setForeground(Color.PINK);
        setTitle("Nofitication Message");
        setAlwaysOnTop(true);
        setFont(new Font("Dialog", Font.BOLD, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 350, 180);
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.PINK);
        contentPanel.setForeground(Color.WHITE);
        contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        setContentPane(contentPanel);
        contentPanel.setLayout(new GridLayout(1, 1, 0, 0));
        contentPanel.setToolTipText("<html>This tool is made by MH</html>");
        JLabel lblNewLabel = new JLabel(msgValue);
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        contentPanel.add(lblNewLabel);
        contentPanel.requestFocus();
        lblNewLabel.setText(msgValue);
        setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { contentPanel, lblNewLabel }));
    }
    
    private static void setMsg(String messageValue) {
        msgValue = messageValue;
        if (frame != null) {
            instancePanel();
        }
        
    }
    
    private static void instancePanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.PINK);
        contentPanel.setForeground(Color.WHITE);
        contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        frame.setContentPane(contentPanel);
        contentPanel.setLayout(new GridLayout(1, 1, 0, 0));
        contentPanel.setToolTipText("<html>This tool to get java script error on Browser.</html>");
        JLabel lblNewLabel = new JLabel(msgValue);
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        contentPanel.add(lblNewLabel);
        contentPanel.requestFocus();
        lblNewLabel.setText(msgValue);
        frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { contentPanel, lblNewLabel }));
    }
    
}
