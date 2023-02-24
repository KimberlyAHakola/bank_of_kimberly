/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */

import java.awt.*;
import javax.swing.*;
import java.util.List;


public class Frame extends JFrame {
    //Instance variables
    private JTextField fnameTextField;
    private JTextField lnameTextField;
    private JButton searchUpdateButton;
    private JButton addAcctButton;
    
    private Database database = new Database();
    private List<Customers> customers;
    private Customers customer = new Customers();
    
    public Frame(){
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
    
    private void initComponents(){
        //Establish Frame
        setTitle("Bank of Kimberly");
        setLocationByPlatform(true); //Sets location of pane by system default
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Closes application
        
        //Add controls for main frame
        
        //Create textFields: First Name, Last Name
        fnameTextField = setTextField(fnameTextField, 150, 20);
        lnameTextField = setTextField(lnameTextField, 150, 20);
        
        //Establish text panel
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createTitledBorder("Enter Customer"
                + " Name"));
        textPanel.setLayout(new GridBagLayout());
        textPanel.add(new JLabel("First Name"), getConstraints(0,0));
        textPanel.add(fnameTextField, getConstraints(1,0));
        textPanel.add(new JLabel("Last Name"), getConstraints(0,1));
        textPanel.add(lnameTextField, getConstraints(1,1));
        
        //Create buttons: Search/Update, Deposit/Withdrawl, Add Customer
        searchUpdateButton = new JButton("Search / Update");
        searchUpdateButton.addActionListener(event -> 
                searchUpdateButtonClicked());
        addAcctButton = new JButton("Add Customer");
        addAcctButton.addActionListener(event -> addAcctButtonClicked());
        
        //Establish buttonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(searchUpdateButton);
        buttonPanel.add(addAcctButton);
        
        //Add panels to JFrame
        add(textPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        
        setVisible(true);
    }
    
    //Gets GridBagConstraints object
    private GridBagConstraints getConstraints(int x, int y){
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(5, 5, 0, 5);
        c.gridx = x;
        c.gridy = y;
        return c;
    }
    
    private JTextField setTextField(JTextField name, int dim1, int dim2){
        
        name = new JTextField();
        Dimension dim = new Dimension (dim1, dim2);
        name.setPreferredSize(dim);
        name.setMinimumSize(dim);
        name.setMaximumSize(dim);
        
        return name;
    }
    
    //BEGIN searchUpdateButtonClicked()
    //Signals event of search button clicked
    private void searchUpdateButtonClicked(){
       
        //Get name from textfield
        String fname = fnameTextField.getText().toUpperCase();
        String lname = lnameTextField.getText().toUpperCase();
        String typeFname = "typeFname";
        String typeLname = "typeLname";
        String typeFnameLname = "typeFnameLname";
        
        /*Validate input and search for customer, display results in Customer
        info frame */
        if(fname.isEmpty() && lname.isEmpty()){
                errorNameNotEntered();
        }else if(fname.isEmpty()){
                searchCustomer(fname,lname,typeLname);
        }else if(lname.isEmpty()){
                searchCustomer(fname,lname,typeFname);
        }else{
            searchCustomer(fname,lname,typeFnameLname);
        }
    }//END searchUpdateButtonClicked()
    
     //BEGIN addAcctButtonClicked()
    //Calls AddAcctFrame
    private void addAcctButtonClicked(){
        //Add functionality
        java.awt.EventQueue.invokeLater(() -> {
            AddAcctFrame aaFrame = new AddAcctFrame();
            aaFrame.setVisible(true);
        });
    }//END addAcctButtonClicked()
    
    //BEGIN errorNameNotEntered()
    //Displays error message if no name is entered
    private void errorNameNotEntered(){
        String message = "Please enter a name."
                        + "\nBank App is not psychic.";
        String title = "Invalid Entry";
        JOptionPane.showMessageDialog(this, message, title,
                    JOptionPane.ERROR_MESSAGE);
        fnameTextField.requestFocusInWindow();
    }//END errorNameNotEntered()
    
    //BEGIN searchCustomer()
    //Searches for customer based on type of name: fname, lname, or both
    //Then calls customerinfoframe and populates fname, lname, acct num
    private void searchCustomer(String fname, String lname, String nameType){
        int customerIndex;
        customers = database.getAllCustomers(Database.SORT_BY_LNAME);
        
        
        if(null == nameType){
            customer = customers.get(0);
        }else //Populate customer object according to name type entered
        switch (nameType) {
            case "typeFnameLname":
                customer = database.getCustomer(fname, lname);
                break;
            case "typeFname":
                customer = database.getCustomer(fname, Database.TYPE_FNAME);
                break;
            case "typeLname":
                customer = database.getCustomer(lname, Database.TYPE_LNAME);
                break;
            default:
                customer = customers.get(0);
                break;
        }
        
        //Call CustomerInfoFrame and populate with fname,lname,acctnum
        if(customer == null){
            
            String msg = "Could not find a match.\n"
                    + "Please step through database.";
            String title = "ERROR";
            JOptionPane.showMessageDialog(this, msg, title,
                    JOptionPane.ERROR_MESSAGE);
            
            customer = customers.get(0);
        }
        
        String lastName = customer.getLname();
        String firstName = customer.getFname();
        int custId = customer.getCustId();
        
        if(nameType == "typeFnameLname"){
            customerIndex = indexCustomer(lastName, firstName);
        }else{
            customerIndex = indexCustomer(lastName);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            CustomerInfoFrame infoFrame = new CustomerInfoFrame();
            infoFrame.setVisible(true);


            infoFrame.setCustId(custId);
            infoFrame.setCustomerIndex(customerIndex);
            infoFrame.setCustomerList(customers);
            
            if(customerIndex < 1){
                infoFrame.setPreviousCustomerButton(false);
            }else if(customerIndex > (customers.size()-2)){
                infoFrame.setNextCustomerButton(false);
            }
        });  
    }//END searchCustomer()
    
    private int indexCustomer(String lname){
        int index = -1;
        
        
        for(Customers customerCheck : customers){
            String lastList = customerCheck.getLname();
            if(lastList.equals(lname)){
                return customers.indexOf(customerCheck);
            }
        }
        
        return index;
    }
    
    private int indexCustomer(String lname, String fname){
        int index = -1;
        
        for(Customers customerCheck : customers){
            String lastList = customerCheck.getLname();
            String firstList = customerCheck.getFname();
            if(lastList.equals(lname) && firstList.equals(fname)){
                return customers.indexOf(customerCheck);
            }
        }
        return index;
    }
}
