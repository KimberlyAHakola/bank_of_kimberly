/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */

import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;
import java.util.List;

public class DepositWithdrawlFrame extends JFrame{
    //Add instance variables
    private JTextField amountTextField;
    private JTextField balanceTextField;
    private JButton depositButton;
    private JButton withdrawlButton;
    
    private int custId;
    private double balance;
    private String acctType;
    
    Database database = new Database();
    
    //Getters and setters
    public void setCustId(int custId){
        this.custId = custId;
    }
    
    public void setBalance(double balance){
        this.balance = balance;
    }
    
    public void setAcctType(String acctType){
        this.acctType = acctType;
    }
    //END getters and setters
    
    public DepositWithdrawlFrame() {
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
    
    //BEGIN initComponents()
    //Initializes instance variables
    private void initComponents() {
         //Establish Frame
        setTitle("Deposit / Withdrawl");
        setLocationByPlatform(true); //Sets location of pane by system default
        
        //Add controls for customer info frame
        
        //Set text fields
        amountTextField = setTextField(amountTextField, 150, 20, true);
        balanceTextField = setTextField(balanceTextField, 150, 20, false);
        
        //Set buttons
        depositButton = new JButton("Deposit");
        depositButton.addActionListener(event -> depositButtonClicked());
        withdrawlButton = new JButton("Withdrawl");
        withdrawlButton.addActionListener(event -> withdrawlButtonClicked());
        
        //Establish mainPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(new JLabel("Amount"), getConstraints(0,0));
        mainPanel.add(amountTextField, getConstraints(1,0));
        mainPanel.add(new JLabel("Balance"), getConstraints(0,1));
        mainPanel.add(balanceTextField, getConstraints(1,1));
        
        //Establish button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawlButton);
        
        //Add panel to frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
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
    
    private void depositButtonClicked(){
        
        double amount = 0;
        
        //Validate amount deposit from amount text field
        Validation v = new Validation();
        String errorMsg = "";
        errorMsg += v.isDouble(amountTextField.getText(), "Amount");
        
        if(errorMsg.isEmpty()){
            //Get amount, cust id, and balance
            amount = Double.parseDouble(amountTextField.getText());
            
            //Calculate new balance, update database, and display new balance
            //resets amount to null value
            
            balance+=amount;
            
            switch(acctType){
                case "checking":
                    database.updateCheckingBalance(custId, balance);
                case "savings":
                    database.updateSavingsBalance(custId, balance);
            }
            
            
            String msg = "Transaction complete.";
            String title = "Deposit";
            JOptionPane.showMessageDialog(this, msg, title,
                    JOptionPane.INFORMATION_MESSAGE);
            amountTextField.requestFocusInWindow();
            
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            String formattedBalance = currency.format(balance);
            balanceTextField.setText(formattedBalance);
            amountTextField.setText(null);

        }else{
            String title = "Invalid Entry";
            JOptionPane.showMessageDialog(this, errorMsg, title,
                    JOptionPane.ERROR_MESSAGE);
            amountTextField.requestFocusInWindow();
        }
    }
    
    private void withdrawlButtonClicked(){

        double amount = 0;
        
        //Validate amount deposit from amount text field
        Validation v = new Validation();
        String errorMsg = "";
        errorMsg += v.isDouble(amountTextField.getText(), "Amount");
        
        if(errorMsg.isEmpty()){
            //Get amount, cust id, and balance
            amount = Double.parseDouble(amountTextField.getText());
            double newBalance = 0;
            //Calculate new balance, update database, and display new balance
            //resets amount to null value
            newBalance = balance - amount;

            if(newBalance >= 0){
                switch(acctType){
                    case "checking":
                        database.updateCheckingBalance(custId, newBalance);
                    case "savings":
                        database.updateSavingsBalance(custId, newBalance);
                }
            
                String msg = "Transaction complete.";
                String title = "Withdrawl";
                JOptionPane.showMessageDialog(this, msg, title,
                        JOptionPane.INFORMATION_MESSAGE);
                amountTextField.requestFocusInWindow();
                
                NumberFormat currency = NumberFormat.getCurrencyInstance();
                String formattedBalance = currency.format(newBalance);
                balanceTextField.setText(formattedBalance);
                amountTextField.setText(null);
                balance = newBalance;
            }else{
                
                String message = "Insufficient funds.";
                String title = "Withdrawl Error";
                JOptionPane.showMessageDialog(this, message, title,
                        JOptionPane.ERROR_MESSAGE);
                balanceTextField.setText(Double.toString(balance));
                amountTextField.setText(null);
                
            }
        }else{
            String title = "Invalid Entry";
            JOptionPane.showMessageDialog(this, errorMsg, title,
                    JOptionPane.ERROR_MESSAGE);
            amountTextField.requestFocusInWindow();
        }
    }
}
