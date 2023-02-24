/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.text.NumberFormat;

public class CustomerInfoFrame extends JFrame {
    //Add instance variables
    private JTextField fnameTextField;
    private JTextField lnameTextField;
    private JTextField acctNumTextField;
    private JTextField balanceTextField;
    private JTextField interestRateTextField;
    private JTextField monthTextField;
    private JTextField addressTextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private JTextField zipCodeTextField;
    private JTextField phoneNumTextField;
    private JButton previousCustomerButton;
    private JButton nextCustomerButton;
    private JButton selectCustomerButton;
    private JButton calcYTDInterestButton;
    private JButton getCustomerInfoButton;
    private JButton updateCustomerInfoButton;
    private JButton depositWithdrawlButton;
    private JButton resetButton;
    private JRadioButton acctTypeSavingsRadioButton;
    private JRadioButton acctTypeCheckingRadioButton;
    
    private int custId;
    private int customerIndex;
    
    private List<Customers> customerList;
    private Database database = new Database();
    private Customers customer = new Customers();
    private SavingsAcct savingsAcct = new SavingsAcct();
    private CheckingAcct checkingAcct = new CheckingAcct();
    
    
    //BEGIN GETTERS AND SETTERS
    //Getters and Setters for fname,lname,custId,acctNum: populated from Frame
    public void setCustomerList(List<Customers> customerList){
        this.customerList = customerList;
        populateInitialTextFields();
    }
    
    public void setCustId(int custId){
        this.custId = custId;
    }
    
    public void setCustomerIndex(int customerIndex){
        this.customerIndex = customerIndex;
    }

    public void setPreviousCustomerButton(boolean x){
        this.previousCustomerButton.setEnabled(x);
    }
    
    public void setNextCustomerButton(boolean x){
        this.nextCustomerButton.setEnabled(x);
    }
    //  END GETTERS AND SETTERS
    
    private void populateInitialTextFields(){
        
        customer = customerList.get(customerIndex);
        custId = customer.getCustId();
        savingsAcct = database.getCustSavingsAcct(custId);
        checkingAcct = database.getCustCheckingAcct(custId);
        
        fnameTextField.setText(customer.getFname());
        System.out.println(customer.getFname());
        lnameTextField.setText(customer.getLname());
        acctNumTextField.setText(Integer.toString
                (savingsAcct.getAcctNum()));
        
        if(!hasCheckingAcct()){
            acctTypeCheckingRadioButton.setEnabled(false);
        }
        
    }
    
    public CustomerInfoFrame() {
        //Set look and feel of window using UIManager Class
        try{
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e){
            System.out.println(e);
        }
        initComponents();
    }
    
    private void initComponents() {
        
         //Establish Frame
        setTitle("Customer Information");
        setLocationByPlatform(true); //Sets location of pane by system default
        
        //Set Customer Search text fields
        fnameTextField = setTextField(fnameTextField, 150, 20, false);
        lnameTextField = setTextField(lnameTextField, 150, 20, false);
        acctNumTextField = setTextField(acctNumTextField, 150, 20, false);
        
        //Set customer search button fields
        previousCustomerButton = new JButton("Previous Customer");
        previousCustomerButton.addActionListener(event -> 
            previousCustomerButtonClicked());
        nextCustomerButton = new JButton("Next Customer");
        nextCustomerButton.addActionListener(event -> 
                nextCustomerButtonClicked());
        selectCustomerButton = new JButton("Select Customer");
        selectCustomerButton.addActionListener(event -> 
                selectCustomerButtonClicked());
        
        //Establish Customer Search text Panel
        JPanel searchTextPanel = new JPanel();
        searchTextPanel.setBorder(BorderFactory.createTitledBorder("Search "
                + "Customer"));
        searchTextPanel.setLayout (new GridBagLayout());
        searchTextPanel.add(new JLabel("First Name"), getConstraints(0,0));
        searchTextPanel.add(fnameTextField, getConstraints(1,0));
        searchTextPanel.add(new JLabel("Last Name"), getConstraints(0,1));
        searchTextPanel.add(lnameTextField, getConstraints(1,1));
        searchTextPanel.add(new JLabel("Account Number"), getConstraints(0,2));
        searchTextPanel.add(acctNumTextField, getConstraints(1,2));
        searchTextPanel.add(previousCustomerButton, getConstraints(2,0));
        searchTextPanel.add(nextCustomerButton, getConstraints(2,1));
        searchTextPanel.add(selectCustomerButton, getConstraints(2,2));
        
        //Set account information text fields
        balanceTextField = setTextField(balanceTextField, 150, 20, false);
        monthTextField = setTextField(monthTextField, 50, 20, false);
        interestRateTextField = setTextField(interestRateTextField, 50, 20, false);
        
        ButtonGroup acctTypeBG = new ButtonGroup();
        acctTypeSavingsRadioButton = new JRadioButton("Savings");
        acctTypeCheckingRadioButton = new JRadioButton("Checking");
        acctTypeBG.add(acctTypeSavingsRadioButton);
        acctTypeBG.add(acctTypeCheckingRadioButton);
        acctTypeSavingsRadioButton.setSelected(true);
        acctTypeCheckingRadioButton.setSelected(false);
        
        
        //Set account information buttons
        depositWithdrawlButton = new JButton("Deposit / Withdrawl");
        depositWithdrawlButton.addActionListener(event -> 
            depositWithdrawlButtonClicked());
        depositWithdrawlButton.setEnabled(false);
        calcYTDInterestButton = new JButton("Calculate YTD Interest");
        calcYTDInterestButton.addActionListener(event -> 
            calcYTDInterestButtonClicked());
        calcYTDInterestButton.setEnabled(false);
        
        
        //Establish interest panel, flow layout
        JPanel interestPanel = new JPanel();
        interestPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        interestPanel.add(interestRateTextField);
        interestPanel.add(new JLabel("Current Month"));
        interestPanel.add(monthTextField);
        
        //Establish account Information Panel
        JPanel customerInfoPanel = new JPanel();
        customerInfoPanel.setBorder(BorderFactory.createTitledBorder("Account "
                + "Information"));
        customerInfoPanel.setLayout (new GridBagLayout());
        customerInfoPanel.add(new JLabel("Balance"), getConstraints(0,0));
        customerInfoPanel.add(balanceTextField, getConstraints(1,0));
        customerInfoPanel.add(new JLabel("Interest Rate"), getConstraints(0,1));
        customerInfoPanel.add(interestPanel, getConstraints(1,1));
        customerInfoPanel.add(depositWithdrawlButton, getConstraints(2,0));
        customerInfoPanel.add(calcYTDInterestButton, getConstraints(2,1));
        customerInfoPanel.add(acctTypeSavingsRadioButton, getConstraints(0,2));
        customerInfoPanel.add(acctTypeCheckingRadioButton, getConstraints(1,2));
        
        //Set update info text fields
        addressTextField = setTextField(addressTextField, 150, 20, false);
        cityTextField = setTextField(cityTextField, 50, 20, false);
        stateTextField = setTextField(stateTextField, 50, 20, false);
        zipCodeTextField = setTextField(zipCodeTextField, 50, 20, false);
        phoneNumTextField = setTextField(phoneNumTextField, 150, 20, false);
        
        //Set update info buttons
        getCustomerInfoButton = new JButton("Get Customer Info");
        getCustomerInfoButton.addActionListener(event -> 
            getCustomerInfoButtonClicked());
        getCustomerInfoButton.setEnabled(false);
        updateCustomerInfoButton = new JButton("Update Customer Info");
        updateCustomerInfoButton.addActionListener(event -> 
            updateCustomerInfoButtonClicked());
        updateCustomerInfoButton.setEnabled(false);
        
        
        //Establish state panel
        JPanel statePanel = new JPanel();
        statePanel.setLayout(new GridBagLayout());
        //REMOVE PADDING FROM PANEL
        statePanel.add(cityTextField, getConstraints(0,0));
        statePanel.add(new JLabel("State"), getConstraints(1,0));
        statePanel.add(stateTextField, getConstraints(2,0));
        
        //Establish Update Customer Panel
        JPanel updateInfoPanel = new JPanel();
        updateInfoPanel.setBorder(BorderFactory.createTitledBorder("Get "
                + "Customer Information"));
        updateInfoPanel.setLayout (new GridBagLayout());
        updateInfoPanel.add(new JLabel("Address"), getConstraints(0,0));
        updateInfoPanel.add(addressTextField, getConstraints(1,0));
        updateInfoPanel.add(new JLabel("City"), getConstraints(0,1));
        updateInfoPanel.add(statePanel, getConstraints(1,1));
        updateInfoPanel.add(new JLabel("Zip Code"), getConstraints(0,2));
        updateInfoPanel.add(zipCodeTextField, getConstraints(1,2));
        updateInfoPanel.add(new JLabel("Phone Number"), getConstraints(0,3));
        updateInfoPanel.add(phoneNumTextField, getConstraints(1,3));
        updateInfoPanel.add(getCustomerInfoButton, getConstraints(2,0));
        updateInfoPanel.add(updateCustomerInfoButton, getConstraints(2,1));
        
        resetButton = new JButton("Reset");
        resetButton.addActionListener(event -> 
            resetButtonClicked());
        
        //Establish south panel
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(resetButton);
        
        //Establich main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout (new GridBagLayout());
        mainPanel.add(searchTextPanel, getConstraints(0,0));
        mainPanel.add(customerInfoPanel, getConstraints(0,1));
        mainPanel.add(updateInfoPanel, getConstraints(0,2));
        
        //Add panels to frame
        add(mainPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        
        pack();
        
        setVisible(false);
    }
    
    //Get GridBagConstraints Object
    private GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 0, 5);
        c.gridx = x;
        c.gridy = y;
        return c;
    }
    
    private JTextField setTextField(JTextField name, int dim1, int dim2,
            boolean x){
        
        name = new JTextField();
        Dimension dim = new Dimension (dim1, dim2);
        name.setPreferredSize(dim);
        name.setMinimumSize(dim);
        name.setMaximumSize(dim);
        name.setEditable(x);
        
        return name;
    }
    
    private void previousCustomerButtonClicked(){
        //Add functionality
        nextCustomerButton.setEnabled(true);
        acctTypeCheckingRadioButton.setEnabled(true);
        
        customerIndex -= 1;

        populateInitialTextFields();
        
        if(customerIndex == -1){
            String message = "ERROR...Cycling to beginning of list.";
            String title = "Previous Customer Error";
            JOptionPane.showMessageDialog(this, message, title, 
                    JOptionPane.ERROR_MESSAGE);
            previousCustomerButton.requestFocusInWindow();
            customerIndex = 0;
        }else if(customerIndex <= 0){
            previousCustomerButton.setEnabled(false);
        }        
    }
    
    private void nextCustomerButtonClicked(){
        //Add functionality
        previousCustomerButton.setEnabled(true);
        acctTypeCheckingRadioButton.setEnabled(true);
        
        customerIndex += 1;

        populateInitialTextFields();
        
        if(customerIndex == -1){
            String message = "ERROR...Cycling to beginning of list.";
            String title = "Previous Customer Error";
            JOptionPane.showMessageDialog(this, message, title, 
                    JOptionPane.ERROR_MESSAGE);
            nextCustomerButton.requestFocusInWindow();
            customerIndex = 0;
        }else if(customerIndex < (customerList.size()-1)){
        } else {
            nextCustomerButton.setEnabled(false);
        }        
    }
    
    //BEGIN selectCustomerButtonClicked()
    //When selected populates savings acct info
    private void selectCustomerButtonClicked(){
        //Add functionality__________need to format int rate
        //Disable text fields fname,lname,acctNum,balance,intRate, radio buttons
        //and next/previous/select customer buttons
        fnameTextField.setEnabled(false);
        lnameTextField.setEnabled(false);
        acctNumTextField.setEnabled(false);
        balanceTextField.setEnabled(false);
        interestRateTextField.setEnabled(false);
        acctTypeSavingsRadioButton.setEnabled(false);
        acctTypeCheckingRadioButton.setEnabled(false);
        previousCustomerButton.setEnabled(false);
        nextCustomerButton.setEnabled(false);
        selectCustomerButton.setEnabled(false);
        
        //Enable text fields address,city,state,zip,phone
        //Enable buttons getCustInfo,Deposit/Witdrawl,calcYTDInterest,UpdateInfo
        depositWithdrawlButton.setEnabled(true);
        calcYTDInterestButton.setEnabled(true);
        monthTextField.setEditable(true);
        addressTextField.setEditable(true);
        cityTextField.setEditable(true);
        stateTextField.setEditable(true);
        zipCodeTextField.setEditable(true);
        phoneNumTextField.setEditable(true);
        getCustomerInfoButton.setEnabled(true);
        updateCustomerInfoButton.setEnabled(true);
        
        //Set account info text fields
        if(acctTypeSavingsRadioButton.isSelected()){
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            NumberFormat percent = NumberFormat.getPercentInstance();
            double actualBalance = savingsAcct.getBalance();
            double actualIntRate = savingsAcct.getIntRate();
            String formattedBalance = currency.format(actualBalance);
            String formattedIntRate = percent.format(actualIntRate);
            balanceTextField.setText(formattedBalance);
            interestRateTextField.setText(formattedIntRate);
        }else{balanceTextField.setText(Double.toString
                (checkingAcct.getBalance()));
            interestRateTextField.setText(Double.toString
                (checkingAcct.getIntRate()));
        }
    }//END selecCustomerButtonClicked()
    
    private boolean hasCheckingAcct(){
        if(database.getCustCheckingAcct(custId) != null){
            return true;
        }else{
            return false;
        }
    }
    
    private void calcYTDInterestButtonClicked(){
        //Add functionality____________need to format return data
        double interest = calcYTDInterest();
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        String formattedInterest = currency.format(interest);
        if(interest == -1){
            String message = "Please enter current month as an integer.";
            String title = "ERROR";
            JOptionPane.showMessageDialog(this,message, title,
                    JOptionPane.ERROR_MESSAGE);
            monthTextField.requestFocusInWindow();
        }else{
            String message = "YTD interest is: " + formattedInterest;
            String title = "YTD Interest";
            JOptionPane.showMessageDialog(this, message, title, 
                    JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    private double calcYTDInterest(){
        //Get and validate month
        String stringMonth = monthTextField.getText();
        double bal = savingsAcct.getBalance();
        double intR = savingsAcct.getIntRate();
        
        if(!stringMonth.isEmpty()){
            try{
                int month = Integer.parseInt(stringMonth);
                double ytdInterest = bal * intR * 
                    (month / 12.0);
            
                return ytdInterest;
                
            }catch(NumberFormatException e){
                return -1;
            }
        }else{
            return -1;
        }
    }
    
    private void getCustomerInfoButtonClicked(){
       
        addressTextField.setText(customer.getAddress());
        cityTextField.setText(customer.getCity());
        stateTextField.setText(customer.getState());
        zipCodeTextField.setText(Integer.toString(customer.getZip()));
        phoneNumTextField.setText(customer.getPhone());
    }
    
    private void updateCustomerInfoButtonClicked(){
     
        String updateAddress = addressTextField.getText().toUpperCase();
        String updateCity = cityTextField.getText().toUpperCase();
        String updateState = stateTextField.getText().toUpperCase();
        int updateZip = Integer.parseInt(zipCodeTextField.getText());
        String updatePhone = phoneNumTextField.getText().toUpperCase();
        
        //Database d = new Database();
        if(database.updateCustInfo(custId, updateAddress, updateCity, updateState,
                updateZip, updatePhone)){
            
            String message = "Update complete.\n"
                    + "Please close window to reset changes.";
            String title = "Update";
            JOptionPane.showMessageDialog(this, message, title, 
                    JOptionPane.INFORMATION_MESSAGE);
        }else{
            String message = "Unable to complete update.\n Try again.";
            String title = "Update";
            JOptionPane.showMessageDialog(this, message, title,
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void depositWithdrawlButtonClicked(){
        //Add functionality
        //May need to send customer name and acct number to other frame
        java.awt.EventQueue.invokeLater(() -> {
            DepositWithdrawlFrame dwFrame = new DepositWithdrawlFrame();
            dwFrame.setVisible(true);
            dwFrame.setCustId(custId);
     
            if(acctTypeCheckingRadioButton.isSelected()){
                dwFrame.setBalance(checkingAcct.getBalance());
                dwFrame.setAcctType("checking");
            }else{
                dwFrame.setBalance(savingsAcct.getBalance());
                dwFrame.setAcctType("savings");
            }
        });
    }
    
    private void resetButtonClicked(){
        //Enable text fields and buttons
        acctTypeSavingsRadioButton.setEnabled(true);
        if(hasCheckingAcct()){
            acctTypeCheckingRadioButton.setEnabled(true);
        }
        previousCustomerButton.setEnabled(true);
        nextCustomerButton.setEnabled(true);
        selectCustomerButton.setEnabled(true);
        
        //Disable
        depositWithdrawlButton.setEnabled(false);
        calcYTDInterestButton.setEnabled(false);
        monthTextField.setEditable(false);
        addressTextField.setEditable(false);
        cityTextField.setEditable(false);
        stateTextField.setEditable(false);
        zipCodeTextField.setEditable(false);
        phoneNumTextField.setEditable(false);
        getCustomerInfoButton.setEnabled(false);
        updateCustomerInfoButton.setEnabled(false);
        
        //Set text fields to null
        balanceTextField.setText(null);
        interestRateTextField.setText(null);
        monthTextField.setText(null);
        addressTextField.setText(null);
        cityTextField.setText(null);
        stateTextField.setText(null);
        zipCodeTextField.setText(null);
        phoneNumTextField.setText(null);
    
    }
}


