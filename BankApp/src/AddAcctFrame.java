/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */

import java.awt.*;
import javax.swing.*;


public class AddAcctFrame extends JFrame{
    //Add instance variables
    private JTextField fnameTextField;
    private JTextField lnameTextField;
    private JTextField addressTextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private JTextField zipCodeTextField;
    private JTextField phoneNumTextField;
    private JTextField intRateTextField;
    private JButton addButton;
    
    public AddAcctFrame() {
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
        setTitle("Add New Account");
        setLocationByPlatform(true); //Sets location of pane by system default
        
        //Add controls for customer info frame
        
        //Set text fields
        fnameTextField = new JTextField();
        Dimension dim = new Dimension(150,20);
        fnameTextField.setPreferredSize(dim);
        fnameTextField.setMinimumSize(dim);
        fnameTextField.setMaximumSize(dim);
        lnameTextField = new JTextField();
        lnameTextField.setPreferredSize(dim);
        lnameTextField.setMinimumSize(dim);
        lnameTextField.setMaximumSize(dim);
        addressTextField = new JTextField();
        addressTextField.setPreferredSize(dim);
        addressTextField.setMinimumSize(dim);
        addressTextField.setMaximumSize(dim);
        cityTextField = new JTextField();
        Dimension dimSmall = new Dimension(50,20);
        cityTextField.setPreferredSize(dimSmall);
        cityTextField.setMinimumSize(dimSmall);
        cityTextField.setMaximumSize(dimSmall);
        stateTextField = new JTextField();
        stateTextField.setPreferredSize(dimSmall);
        stateTextField.setMinimumSize(dimSmall);
        stateTextField.setMaximumSize(dimSmall);
        zipCodeTextField = new JTextField();
        zipCodeTextField.setPreferredSize(dimSmall);
        zipCodeTextField.setMinimumSize(dimSmall);
        zipCodeTextField.setMaximumSize(dimSmall);
        phoneNumTextField = new JTextField();
        phoneNumTextField.setPreferredSize(dim);
        phoneNumTextField.setMinimumSize(dim);
        phoneNumTextField.setMaximumSize(dim);
        intRateTextField = new JTextField();
        intRateTextField.setPreferredSize(dim);
        intRateTextField.setMinimumSize(dim);
        intRateTextField.setMaximumSize(dim);
        
        //Set buttons
        addButton = new JButton("Add Account");
        addButton.addActionListener(event -> 
            addButtonClicked());
        
        //Establish state panel
        JPanel statePanel = new JPanel();
        statePanel.setLayout(new GridBagLayout());
        //REMOVE PADDING FROM PANEL
        statePanel.add(cityTextField, getConstraints(0,0));
        statePanel.add(new JLabel("State"), getConstraints(1,0));
        statePanel.add(stateTextField, getConstraints(2,0));
        
        //Establish panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(new JLabel("First Name"), getConstraints(0,0));
        panel.add(fnameTextField, getConstraints(1,0));
        panel.add(new JLabel("Last Name"), getConstraints(0,1));
        panel.add(lnameTextField, getConstraints(1,1));
        panel.add(new JLabel("Address"), getConstraints(0,2));
        panel.add(addressTextField, getConstraints(1,2));
        panel.add(new JLabel("City"), getConstraints(0,3));
        panel.add(statePanel, getConstraints(1,3));
        panel.add(new JLabel("Zip Code"), getConstraints(0,4));
        panel.add(zipCodeTextField, getConstraints(1,4));
        panel.add(new JLabel("Phone Number"), getConstraints(0,5));
        panel.add(phoneNumTextField, getConstraints(1,5));
        panel.add(new JLabel("Interest Rate"), getConstraints(0,6));
        panel.add(intRateTextField, getConstraints(1,6));
        panel.add(addButton, getConstraints(1,8));
        
        //Add panel to frame
        add(panel, BorderLayout.CENTER);
        
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
    
    private void addButtonClicked(){
        
        //Validate input
        Validation v = new Validation();
        String errorMsg = "";
        errorMsg += v.isPresent(fnameTextField.getText(), "First Name");
        errorMsg += v.isPresent(lnameTextField.getText(), "Last Name");
        errorMsg += v.isPresent(addressTextField.getText(), "Address");
        errorMsg += v.isPresent(cityTextField.getText(), "City");
        errorMsg += v.isPresent(stateTextField.getText(), "State");
        errorMsg += v.isInteger(zipCodeTextField.getText(), "Zip Code");
        errorMsg += v.isPresent(phoneNumTextField.getText(), "Phone Number");
        errorMsg += v.isDouble(intRateTextField.getText(), "Interest Rate");
        
        //Get customer info from text fields
        if(errorMsg.isEmpty()){
            String fname = fnameTextField.getText().toUpperCase();
            String lname = lnameTextField.getText().toUpperCase();
            String address = addressTextField.getText().toUpperCase();
            String city = cityTextField.getText().toUpperCase();
            String state = stateTextField.getText().toUpperCase();
            int zip = Integer.parseInt(zipCodeTextField.getText());
            String phoneNum = phoneNumTextField.getText();
            Double intRate = Double.parseDouble(intRateTextField.getText());
            Double balance = 0.0;
            int acctNum; //auto increment from databse
            //create new customers object and set customer info
            Customers c = new Customers();
            c.setFname(fname);
            c.setLname(lname);
            c.setAddress(address);
            c.setCity(city);
            c.setState(state);
            c.setZip(zip);
            c.setPhone(phoneNum);

            //Create new database object and call addSavingsAcct()
            Database d = new Database();
            acctNum = d.addSavingsAcct(c, intRate);

            if(acctNum == -99){
                String message = "Could not add customer.\n"
                        + "Customer already exists.";
                String title = "Error";
                JOptionPane.showMessageDialog(this, message, title,
                        JOptionPane.INFORMATION_MESSAGE);
            }else if(acctNum == -1){
                String message = "Could not add customer.\n"
                        + "Please try again.";
                String title = "Error";
                JOptionPane.showConfirmDialog(this, message, title,
                        JOptionPane.ERROR_MESSAGE);
            }else{
                String message = "Customer account added.\n"
                        + "Account number: " + acctNum;
                String title = "Customer added.";
                JOptionPane.showMessageDialog(this, message, title, 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            String title = "Invalid Entry";
            JOptionPane.showMessageDialog(this, errorMsg, title,
                    JOptionPane.ERROR_MESSAGE);
            fnameTextField.requestFocusInWindow();
        }
    }
    
}
