/**
 * 
 */
package com.mh.notification;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import com.mh.utils.Constant;
import com.mh.utils.Utils;
/**
 * @author minhhoang
 *         
 */
public class MainGui {
    
    private JFrame                  frmAutomationTools         = new JFrame();
                                                               
    JButton                         btnLoadData                = new JButton("Open File");
                                                               
    JButton                         btnStart                   = new JButton("Run Auto");
                                                               
    JFileChooser                    fc                         = new JFileChooser();
                                                               
    JEditorPane                     txtFilePath                = new JEditorPane();
                                                               
    File                            file;
                                    
    private final JLabel            lblSheetName               = new JLabel("Sheet Name");
                                                               
    private final JLabel            lblHeaderName              = new JLabel("Header Name");
                                                               
    private final JPanel            optionsGroup               = new JPanel();
                                                               
    private final JLabel            lblAutoOption              = new JLabel("Select Options Run Auto");
                                                               
    private final JRadioButton      rdbtnRunDeskTop            = new JRadioButton("Get Console Log On Desk");
                                                               
    private final JRadioButton      rdbtnRunIpad               = new JRadioButton("Get Console Log On Ipad");
                                                               
    private final JRadioButton      rdbtnScanTextSource        = new JRadioButton("Get text from source code");
                                                               
    private final JRadioButton      rdbtnRunLinkChecker        = new JRadioButton("Run link checker");
                                                               
    private final JLabel            lblRegularExpressionFormat = new JLabel("Regular Expression Format");
                                                               
    private final JTextField        txtRegularExpression       = new JTextField();
                                                               
    private final JLabel            lblNumberOfThread          = new JLabel("Number Thread");
                                                               
    private final JComboBox<String> selectSheet                = new JComboBox<String>();
                                                               
    private final JComboBox<String> selecteHeader              = new JComboBox<String>();
                                                               
    private final JSpinner          txtThreadPool              = new JSpinner();
                                                               
    private final JSpinner          txtTimeOut                 = new JSpinner();
                                                               
    private final JLabel            lblTimedOut                = new JLabel("Page Load Time");
                                                               
    public static Boolean           isValidData, isValidRegular, isValidURL;
                                    
    private List<String>            sheetValue                 = new ArrayList<String>();
                                                               
    private List<String>            headerValue                = new ArrayList<String>();
                                                               
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                
                try {
                    MainGui window = new MainGui();
                    window.frmAutomationTools.setVisible(true);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
    
    /**
     * Create the application.
     */
    public MainGui() {
        initialize();
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        
        btnGroupComponent();
        frmAutomationComponent();
        /**
         * Action in start run auto
         */
        btnStart.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent eve) {
                
                runAuto(eve);
            }
        });
        /**
         * Action in load data for run with user use for select .xlsx file
         */
        btnLoadData.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent eve) {
                
                onChooseFile(eve);
            }
        });
        /**
         * Action for get Sheet Name user has select
         */
        selectSheet.addItemListener(new ItemListener() {
            
            public void itemStateChanged(ItemEvent event) {
                
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    selecteHeader.removeAllItems();
                    Constant.SHEET_NAME = selectSheet.getSelectedItem().toString();
                    headerValue = Utils.getInstance().getAllHeaderName();
                    for (String header : headerValue) {
                        selecteHeader.addItem(header);
                    }
                }
            }
        });
        /*
         * Action for get Time out
         */
        selecteHeader.setModel(new DefaultComboBoxModel<String>(new String[] { "Please Select" }));
        txtTimeOut.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent arg0) {
                
                Constant.TIME_OUT = Integer.parseInt(txtTimeOut.getValue().toString());
            }
        });
        /**
         * Action for get number of thread when run link checker
         */
        txtThreadPool.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent arg0) {
                
                Constant.NUMBER_THREAD = Integer.parseInt(txtThreadPool.getValue().toString());
            }
        });
        frmAutomationTools.setFocusTraversalPolicy(new FocusTraversalOnArray(
                new Component[] { frmAutomationTools.getContentPane(), btnLoadData, txtFilePath, selectSheet,
                        selecteHeader, optionsGroup, rdbtnRunDeskTop, rdbtnRunIpad, rdbtnScanTextSource,
                        rdbtnRunLinkChecker, lblSheetName, lblHeaderName, lblAutoOption, lblRegularExpressionFormat,
                        txtRegularExpression, lblNumberOfThread, txtThreadPool, btnStart }));
        /**
         * Action for get all header in sheet
         */
        selecteHeader.addItemListener(new ItemListener() {
            
            public void itemStateChanged(ItemEvent event) {
                
                if (event.getStateChange() == ItemEvent.SELECTED
                        && ! (selecteHeader.getSelectedItem().equals("Please Select"))) {
                    Constant.HEADER_DATA_NAME = selecteHeader.getSelectedItem().toString();
                }
            }
        });
        txtRegularExpression.addFocusListener(new FocusAdapter() {
            
            @Override
            public void focusLost(FocusEvent e) {
                
                if (txtRegularExpression.getText().length() == 0 && Constant.REGULAR_EXPRESSION == null)
                    txtRegularExpression.setText("Input your regular expression");
                else if (Constant.REGULAR_EXPRESSION == null && txtRegularExpression.getText().length() > 0)
                    Constant.REGULAR_EXPRESSION = txtRegularExpression.getText();
                else if (! Constant.REGULAR_EXPRESSION.equals(txtRegularExpression.getText())
                        && (txtRegularExpression.getText().length() == 0))
                    txtRegularExpression.setText(Constant.REGULAR_EXPRESSION);
                else if (! Constant.REGULAR_EXPRESSION.equals(txtRegularExpression.getText())
                        && (txtRegularExpression.getText().length() > 0))
                    Constant.REGULAR_EXPRESSION = txtRegularExpression.getText();
            }
        });
        txtRegularExpression.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                
                txtRegularExpression.setText("");
            }
        });
    }
    
    /**
     * This method is use for run auto
     * 
     * @param evt
     */
    private void runAuto(ActionEvent evt) {
        
        if (evt.getSource() == btnStart) {
            try {
                validateAllField();
            } catch (Exception e) {
                NotificationFrame.showMsg(String.format(Constant.ERROR_GET_DATA, e.getMessage()));
            }
        }
    }
    
    /**
     * This method is use for select Data File
     * 
     * @param evt
     */
    private void onChooseFile(ActionEvent evt) {
        
        if (evt.getSource() == btnLoadData) {
            int returnVal = fc.showOpenDialog(btnLoadData);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectSheet.removeAllItems();
                file = fc.getSelectedFile();
                Constant.DATA_PATH = file.getAbsolutePath();
                txtFilePath.setText(file.getAbsolutePath());
                sheetValue = Utils.getInstance().getAllSheetName();
                for (String name : sheetValue) {
                    selectSheet.addItem(name);
                }
            }
        }
    }
    
    /**
     * This method is use for check correct Regx
     */
    private void isCorrectRegular() {
        
        if (Utils.getInstance().isCorrectRegular(txtRegularExpression.getText())) {
            Constant.REGULAR_EXPRESSION = txtRegularExpression.getText();
            isValidRegular = true;
        } else {
            isValidRegular = false;
        }
    }
    
    /**
     * This method is use for check select correct TestData
     */
    private void isCorrectData() {
        
        try {
            Constant.TEST_DATA = (ArrayList<HashMap<String, String>>) Utils.getInstance()
                    .getDataExcel(Constant.DATA_PATH, Constant.SHEET_NAME);
            isValidData = Constant.TEST_DATA.size() > 0 ? true : false;
        } catch (NullPointerException e) {
            isValidData = false;
        } catch (IllegalStateException e) {
            isValidData = false;
        }
    }
    
    private void isValidUrl() {
        
        try {
            isValidURL = Utils.getInstance().isValidUrl(Constant.TEST_DATA) == false ? false : true;
        } catch (NullPointerException e) {
            isValidURL = false;
        }
    }
    
    private void btnGroupComponent() {
        
        ButtonGroup groupOptions = new ButtonGroup();
        groupOptions.add(rdbtnRunDeskTop);
        groupOptions.add(rdbtnRunIpad);
        groupOptions.add(rdbtnRunLinkChecker);
        groupOptions.add(rdbtnScanTextSource);
    }
    
    private void validateAllField() {
        
        Constant.IS_HAS_DRIVER = Utils.getInstance().isSetUpEnviroment() && Utils.getInstance().isHasDriver();
        isCorrectData();
        isCorrectRegular();
        getOptionsRun();
        isValidUrl();
        if (isValidData == false && isValidRegular == true && Constant.RUN_OPTIONS_VALUE == 4) {
            NotificationFrame.showMsg(Constant.ERROR_GET_DATA);
        } else if (isValidData == false && isValidRegular == true && ! (Constant.RUN_OPTIONS_VALUE == 4)) {
            NotificationFrame.showMsg(Constant.ERROR_GET_DATA);
        } else if (isValidData == true && isValidRegular == false && Constant.RUN_OPTIONS_VALUE == 4) {
            NotificationFrame.showMsg(Constant.ERROR_REGULAR_EXPRESSION);
        } else if (isValidData == false && isValidRegular == false && Constant.RUN_OPTIONS_VALUE == 4) {
            NotificationFrame.showMsg(Constant.ERROR_REGULAR_EXPRESSION_AND_DATA);
        } else if (isValidData == false && isValidRegular == false && ! (Constant.RUN_OPTIONS_VALUE == 4)) {
            NotificationFrame.showMsg(Constant.ERROR_GET_DATA);
        }
        if (Constant.IS_HAS_DRIVER == false) {
            NotificationFrame.showMsg(Constant.ERROR_VALID_DRIVER);
        }
        if (! isValidURL) {
            NotificationFrame.showMsg(Constant.ERROR_VALID_URL);
        }
    }
    
    private void getOptionsRun() {
        
        if (rdbtnRunDeskTop.isSelected()) {
            NotificationFrame.showMsg("<html>Start to run get console on browser in desktop<html>");
            Constant.RUN_OPTIONS_VALUE = 1;
        } else if (rdbtnRunIpad.isSelected()) {
            NotificationFrame.showMsg("<html>Start to run get console on browser in iPad</html>");
            Constant.RUN_OPTIONS_VALUE = 2;
        } else if (rdbtnRunLinkChecker.isSelected()) {
            NotificationFrame.showMsg("<html>Start to run link checker</html>");
            Constant.RUN_OPTIONS_VALUE = 3;
        } else if (rdbtnScanTextSource.isSelected()) {
            NotificationFrame.showMsg("<html>Start to run get content with regular expression on source page.</html>");
            Constant.RUN_OPTIONS_VALUE = 4;
        }
    }
    
    /**
     * This contain all component set up for GUI
     */
    private void frmAutomationComponent() {
        
        frmAutomationTools.getContentPane().setBackground(new Color(255, 153, 255));
        frmAutomationTools.setResizable(false);
        frmAutomationTools.setTitle("Automation Tools");
        frmAutomationTools.setBackground(Color.WHITE);
        frmAutomationTools.setAlwaysOnTop(true);
        frmAutomationTools.setBounds(100, 100, 243, 532);
        frmAutomationTools.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmAutomationTools.getContentPane().setLayout(null);
        frmAutomationTools.getContentPane().add(btnStart);
        frmAutomationTools.getContentPane().add(txtFilePath);
        frmAutomationTools.getContentPane().add(optionsGroup);
        frmAutomationTools.getContentPane().add(btnLoadData);
        frmAutomationTools.getContentPane().add(lblSheetName);
        frmAutomationTools.getContentPane().add(lblHeaderName);
        frmAutomationTools.getContentPane().add(lblAutoOption);
        frmAutomationTools.getContentPane().add(lblRegularExpressionFormat);
        frmAutomationTools.getContentPane().add(txtRegularExpression);
        btnStart.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnStart.setBounds(69, 455, 100, 40);
        txtRegularExpression.setFont(new Font("Tahoma", Font.BOLD, 13));
        txtRegularExpression.setForeground(Color.BLACK);
        txtRegularExpression.setText("Input your regular expression");
        txtRegularExpression.setBounds(10, 313, 217, 45);
        txtRegularExpression.setColumns(10);
        lblSheetName.setFont(new Font("Arial", Font.BOLD, 13));
        lblSheetName.setBounds(10, 48, 105, 33);
        lblHeaderName.setFont(new Font("Arial", Font.BOLD, 13));
        lblHeaderName.setBounds(125, 48, 105, 33);
        lblAutoOption.setFont(new Font("Arial", Font.BOLD, 13));
        lblAutoOption.setBounds(10, 129, 173, 23);
        lblRegularExpressionFormat.setFont(new Font("Arial", Font.BOLD, 13));
        lblRegularExpressionFormat.setBounds(10, 289, 173, 23);
        txtFilePath.setEditable(false);
        txtFilePath.setBounds(10, 11, 100, 40);
        btnLoadData.setFont(new Font("Tahoma", Font.BOLD, 10));
        btnLoadData.setBounds(125, 11, 100, 40);
        optionsGroup.setBackground(new Color(255, 255, 255));
        optionsGroup.setBounds(10, 163, 217, 118);
        optionsGroup.setLayout(null);
        rdbtnRunDeskTop.setSelected(true);
        rdbtnRunDeskTop.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnRunDeskTop.setBackground(new Color(255, 255, 255));
        rdbtnRunDeskTop.setBounds(6, 7, 208, 23);
        optionsGroup.add(rdbtnRunDeskTop);
        rdbtnRunIpad.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnRunIpad.setBackground(new Color(255, 255, 255));
        rdbtnRunIpad.setBounds(6, 33, 208, 23);
        optionsGroup.add(rdbtnRunIpad);
        rdbtnScanTextSource.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnScanTextSource.setBackground(new Color(255, 255, 255));
        rdbtnScanTextSource.setBounds(6, 58, 208, 23);
        optionsGroup.add(rdbtnScanTextSource);
        rdbtnRunLinkChecker.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnRunLinkChecker.setBackground(new Color(255, 255, 255));
        rdbtnRunLinkChecker.setBounds(6, 84, 208, 23);
        optionsGroup.add(rdbtnRunLinkChecker);
        lblAutoOption.setFont(new Font("Arial", Font.BOLD, 13));
        lblAutoOption.setBounds(10, 129, 173, 23);
        txtThreadPool.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        txtThreadPool.setForeground(Color.BLACK);
        txtThreadPool.setFont(new Font("Tahoma", Font.BOLD, 15));
        txtThreadPool.setBounds(10, 390, 100, 40);
        frmAutomationTools.getContentPane().add(txtThreadPool);
        lblNumberOfThread.setFont(new Font("Arial", Font.BOLD, 13));
        lblNumberOfThread.setBounds(10, 365, 173, 23);
        frmAutomationTools.getContentPane().add(lblNumberOfThread);
        selectSheet.setModel(new DefaultComboBoxModel<String>(new String[] { "Please Select" }));
        selectSheet.setMaximumRowCount(10);
        selectSheet.setBackground(Color.WHITE);
        selectSheet.setForeground(new Color(0, 0, 0));
        selectSheet.setFont(new Font("Tahoma", Font.BOLD, 12));
        selectSheet.setBounds(10, 78, 100, 40);
        frmAutomationTools.getContentPane().add(selectSheet);
        selecteHeader.setMaximumRowCount(10);
        selecteHeader.setForeground(Color.BLACK);
        selecteHeader.setFont(new Font("Tahoma", Font.BOLD, 12));
        selecteHeader.setBackground(Color.WHITE);
        selecteHeader.setBounds(125, 78, 100, 40);
        frmAutomationTools.getContentPane().add(selecteHeader);
        txtTimeOut.setModel(new SpinnerNumberModel(30, 10, 180, 1));
        txtTimeOut.setForeground(Color.BLACK);
        txtTimeOut.setFont(new Font("Tahoma", Font.BOLD, 15));
        txtTimeOut.setBounds(125, 390, 100, 40);
        frmAutomationTools.getContentPane().add(txtTimeOut);
        lblTimedOut.setFont(new Font("Arial", Font.BOLD, 13));
        lblTimedOut.setBounds(123, 365, 173, 23);
        frmAutomationTools.getContentPane().add(lblTimedOut);
    }
}
