/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database{
    
    //Connection variables
    private static Connection connection;
    public static final String DB_NAME = "BankOfKimberly.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
    
    //Define table and column variables
    //Customers Table
    public static final String TABLE_CUSTOMERS = "Customers";
    
    public static final String COLUMN_CUSTOMERS_CUST_ID = "cust_id";
    public static final String COLUMN_CUSTOMERS_FNAME = "fname";
    public static final String COLUMN_CUSTOMERS_LNAME = "lname";
    public static final String COLUMN_CUSTOMERS_ADDRESS = "address";
    public static final String COLUMN_CUSTOMERS_CITY = "city";
    public static final String COLUMN_CUSTOMERS_STATE = "state";
    public static final String COLUMN_CUSTOMERS_ZIP = "zip";
    public static final String COLUMN_CUSTOMERS_PHONE = "phone";
   
    public static final int INDEX_CUSTOMERS_CUST_ID = 1;
    public static final int INDEX_CUSTOMERS_FNAME = 2;
    public static final int INDEX_CUSTOMERS_LNAME = 3;
    public static final int INDEX_CUSTOMERS_ADDRESS = 4;
    public static final int INDEX_CUSTOMERS_CITY = 5;
    public static final int INDEX_CUSTOMERS_STATE = 6;
    public static final int INDEX_CUSTOMERS_ZIP = 7;
    public static final int INDEX_CUSTOMERS_PHONE = 8;
    
    public static final int SORT_BY_NONE = 1;
    public static final int SORT_BY_FNAME = 2;
    public static final int SORT_BY_LNAME = 3;
    public static final int SORT_BY_ACCT_NUM = 4;
    
    public static final int TYPE_FNAME = 1;
    public static final int TYPE_LNAME = 2;
    public static final int TYPE_FNAME_LNAME = 3;
    
    //SavingsAcct Table
    public static final String TABLE_SAVINGS_ACCT = "SavingsAcct";
    
    public static final String COLUMN_SAVINGS_ACCT_ACCT_NUM = "acct_num";
    public static final String COLUMN_SAVINGS_ACCT_CUST_ID = "cust_id";
    public static final String COLUMN_SAVINGS_ACCT_BALANCE = "balance";
    public static final String COLUMN_SAVINGS_ACCT_INT_RATE = "int_rate";
    public static final String COLUMN_SAVINGS_ACCT_SECONDARY_CUST_ID = 
            "Secondary_Cust_Id";
    
    public static final int INDEX_SAVINGS_ACCT_ACCT_NUM = 1;
    public static final int INDEX_SAVINGS_ACCT_CUST_ID = 2;
    public static final int INDEX_SAVINGS_ACCT_BALANCE = 3;
    public static final int INDEX_SAVINGS_ACCT_INT_RATE = 4;
    
    private static final String GET_SAVINGS_ACCT = "SELECT * " +
            " FROM " + TABLE_SAVINGS_ACCT +
            " WHERE " + COLUMN_SAVINGS_ACCT_CUST_ID + " = ?" +
            " OR " + COLUMN_SAVINGS_ACCT_SECONDARY_CUST_ID + " = ?";
    
    //Checking Account table
    
    public static final String TABLE_CHECKING_ACCT = "CheckingAcct";
    
    public static final String COLUMN_CHECKING_ACCT_ACCT_NUM = "acct_num";
    public static final String COLUMN_CHECKING_ACCT_CUST_ID = "cust_id";
    public static final String COLUMN_CHECKING_ACCT_BALANCE = "balance";
    public static final String COLUMN_CHECKING_ACCT_INT_RATE = "int_rate";
    public static final String COLUMN_CHECKING_ACCT_SECONDARY_CUST_ID = 
            "Secondary_Cust_Id";
    
    public static final int INDEX_CHECKING_ACCT_ACCT_NUM = 1;
    public static final int INDEX_CHECKING_ACCT_CUST_ID = 2;
    public static final int INDEX_CHECKING_ACCT_BALANCE = 3;
    public static final int INDEX_CHECKING_ACCT_INT_RATE = 4;
    
    private static final String GET_CHECKING_ACCT = "SELECT * " +
            " FROM " + TABLE_CHECKING_ACCT +
            " WHERE " + COLUMN_CHECKING_ACCT_CUST_ID + " = ?" +
            " OR " + COLUMN_CHECKING_ACCT_SECONDARY_CUST_ID + " = ?";
    
    //Insert statements
    private static final String CHECK_FOR_CUSTOMER = "SELECT * FROM " +
            TABLE_CUSTOMERS + " WHERE " + COLUMN_CUSTOMERS_FNAME + " = ? AND " +
            COLUMN_CUSTOMERS_LNAME + " = ? AND " + COLUMN_CUSTOMERS_ADDRESS +
            " = ?";
    private static final String INSERT_RECORD_CUSTOMERS = "INSERT INTO " +
            TABLE_CUSTOMERS + " (" + COLUMN_CUSTOMERS_FNAME + ", " +
            COLUMN_CUSTOMERS_LNAME + ", " + COLUMN_CUSTOMERS_ADDRESS +
            ", " + COLUMN_CUSTOMERS_CITY + ", " + COLUMN_CUSTOMERS_STATE +
            ", " + COLUMN_CUSTOMERS_ZIP + ", " + COLUMN_CUSTOMERS_PHONE +
            ") VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_RECORD_SAVINGS_ACCT = "INSERT INTO "
            + TABLE_SAVINGS_ACCT + " (" + COLUMN_SAVINGS_ACCT_CUST_ID + ", " +
            COLUMN_SAVINGS_ACCT_BALANCE + ", " + COLUMN_SAVINGS_ACCT_INT_RATE +
            ") VALUES (?, 0, ?)";
    private static final String INSERT_RECORD_CHECKING_ACCT = "INSERT INTO " +
            TABLE_CHECKING_ACCT + "(" + COLUMN_CHECKING_ACCT_CUST_ID + 
            ", 0, 0.0, ''";
    
    //Update statements
    private static final String UPDATE_CUST_INFO = "UPDATE " + TABLE_CUSTOMERS
            + " SET " + COLUMN_CUSTOMERS_ADDRESS + " = ?, " +
            COLUMN_CUSTOMERS_CITY + " = ?, " +
            COLUMN_CUSTOMERS_STATE + " = ?, " +
            COLUMN_CUSTOMERS_ZIP + " = ?, " +
            COLUMN_CUSTOMERS_PHONE + " = ?" + 
            " WHERE " + COLUMN_CUSTOMERS_CUST_ID  + " = ?";
    
    private static final String UPDATE_SAVINGS_ACCT_BALANCE = "UPDATE " +
            TABLE_SAVINGS_ACCT +
            " SET " + COLUMN_SAVINGS_ACCT_BALANCE + " = ?" +
            " WHERE " + COLUMN_SAVINGS_ACCT_CUST_ID + " = ?";
    
    private static final String UPDATE_CHECKING_ACCT_BALANCE = "UPDATE " +
            TABLE_CHECKING_ACCT +
            " SET " + COLUMN_CHECKING_ACCT_BALANCE + " = ?" +
            " WHERE " + COLUMN_CHECKING_ACCT_CUST_ID + " = ?";
    
    
    //Open a connection to the database, throws exception
    private Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(CONNECTION_STRING);
        return connection;
    }
    
    //BEGIN GET CUSTOMERS Get list of all customers info
    public List<Customers> getAllCustomers(int sortBy){
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_CUSTOMERS);
        if(sortBy != SORT_BY_NONE){
            sb.append(" ORDER BY ");
            switch(sortBy){
                case SORT_BY_FNAME: 
                    sb.append(COLUMN_CUSTOMERS_FNAME);
                    break;
                default:
                    sb.append(COLUMN_CUSTOMERS_LNAME);
            }
            sb.append(" COLLATE NOCASE ");
            sb.append("ASC");
        }
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sb.toString());
            ResultSet rs = ps.executeQuery()) {
            
            List<Customers> customers = new ArrayList<>();
            while(rs.next()){
                Customers customer = new Customers();
                customer.setCustId(rs.getInt(INDEX_CUSTOMERS_CUST_ID));
                customer.setFname(rs.getString(INDEX_CUSTOMERS_FNAME));
                customer.setLname(rs.getString(INDEX_CUSTOMERS_LNAME));
                customer.setAddress(rs.getString(INDEX_CUSTOMERS_ADDRESS));
                customer.setCity(rs.getString(INDEX_CUSTOMERS_CITY));
                customer.setState(rs.getString(INDEX_CUSTOMERS_STATE));
                customer.setZip(rs.getInt(INDEX_CUSTOMERS_ZIP));
                customer.setPhone(rs.getString(INDEX_CUSTOMERS_PHONE));
                
                customers.add(customer);
            }
            
            return customers;
            
        } catch(SQLException e) {
            System.out.println("Customers Query Failed");
            System.err.println(e);
            return null;
        }           
    }//END GET ALL CUSTOMERS
    
    //BEGIN GET CUSTOMER Get customer info
    public Customers getCustomer(String name, int type){
        
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_CUSTOMERS);
        sb.append(" WHERE ");
        if(type == 1 || type == 2){
            switch(type){
                case TYPE_FNAME: 
                    sb.append(COLUMN_CUSTOMERS_FNAME);
                    break;
                case TYPE_LNAME:
                    sb.append(COLUMN_CUSTOMERS_LNAME);
                    break;
                }  
        }
        sb.append(" = ?");
        sb.append(" ORDER BY ");
        sb.append(COLUMN_CUSTOMERS_LNAME);
        sb.append(" ASC");
        
        try(Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement
                (sb.toString())){
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Customers customer = new Customers();
                customer.setCustId(rs.getInt(INDEX_CUSTOMERS_CUST_ID));
                customer.setFname(rs.getString(INDEX_CUSTOMERS_FNAME));
                customer.setLname(rs.getString(INDEX_CUSTOMERS_LNAME));
                customer.setAddress(rs.getString(INDEX_CUSTOMERS_ADDRESS));
                customer.setCity(rs.getString(INDEX_CUSTOMERS_CITY));
                customer.setState(rs.getString(INDEX_CUSTOMERS_STATE));
                customer.setZip(rs.getInt(INDEX_CUSTOMERS_ZIP));
                customer.setPhone(rs.getString(INDEX_CUSTOMERS_PHONE));
                
                return customer;
            }else {
                rs.close();
                return null;
            }
        }catch(SQLException e) {
            System.out.println("Customers Query Failed");
            System.err.println(e);
            return null;
        }              
    }
    
    //getCustomer overloaded for fname and lname
    public Customers getCustomer(String fname, String lname){
        
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_CUSTOMERS);
        sb.append(" WHERE ");
        sb.append(COLUMN_CUSTOMERS_FNAME);
        sb.append(" = ?");
        sb.append(" AND ");
        sb.append(COLUMN_CUSTOMERS_LNAME);
        sb.append(" = ?");
        
        try(Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement
                (sb.toString())){
            ps.setString(1, fname);
            ps.setString(2, lname);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Customers customer = new Customers();
                customer.setCustId(rs.getInt(INDEX_CUSTOMERS_CUST_ID));
                customer.setFname(rs.getString(INDEX_CUSTOMERS_FNAME));
                customer.setLname(rs.getString(INDEX_CUSTOMERS_LNAME));
                customer.setAddress(rs.getString(INDEX_CUSTOMERS_ADDRESS));
                customer.setCity(rs.getString(INDEX_CUSTOMERS_CITY));
                customer.setState(rs.getString(INDEX_CUSTOMERS_STATE));
                customer.setZip(rs.getInt(INDEX_CUSTOMERS_ZIP));
                customer.setPhone(rs.getString(INDEX_CUSTOMERS_PHONE));
                
                return customer;
            }else {
                rs.close();
                return null;
            }
        }catch(SQLException e) {
            System.out.println("Customers Query Failed");
            System.err.println(e);
            return null;
        }              
    }//END GET CUSTOMER
    
    //BEGIN GET CUST SAVINGS ACCT  Gets customer account info, requires custId
    public SavingsAcct getCustSavingsAcct(int custId){

          String sql = GET_SAVINGS_ACCT;
        try(Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement
                (sql)) {
            String customerId = Integer.toString(custId);
            ps.setString(1, customerId);
            ps.setString(2, customerId);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                SavingsAcct sa = new SavingsAcct();
                sa.setAcctNum(rs.getInt(INDEX_SAVINGS_ACCT_ACCT_NUM));
                sa.setCustId(rs.getInt(INDEX_SAVINGS_ACCT_CUST_ID));
                sa.setBalance(rs.getDouble(INDEX_SAVINGS_ACCT_BALANCE));
                sa.setIntRate(rs.getDouble(INDEX_SAVINGS_ACCT_INT_RATE));
                
                return sa;
            }else{
                rs.close();
                return null;
            }
        }catch(SQLException e) {
            System.out.println("Savings Query Failed");
            System.err.println(e);
            return null;
        }
    }//END GET CUST SAVINGS ACCT
    
     //BEGIN GET CUST CHECKING ACCT  Gets customer account info, requires custId
    public CheckingAcct getCustCheckingAcct(int custId){

          String sql = GET_CHECKING_ACCT;
        try(Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement
                (sql)) {
            String customerId = Integer.toString(custId);
            ps.setString(1, customerId);
            ps.setString(2, customerId);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                CheckingAcct ca = new CheckingAcct();
                ca.setAcctNum(rs.getInt(INDEX_CHECKING_ACCT_ACCT_NUM));
                ca.setCustId(rs.getInt(INDEX_CHECKING_ACCT_CUST_ID));
                ca.setBalance(rs.getDouble(INDEX_CHECKING_ACCT_BALANCE));
                ca.setIntRate(rs.getDouble(INDEX_CHECKING_ACCT_INT_RATE));
                
                return ca;
            }else{
                rs.close();
                return null;
            }
        }catch(SQLException e) {
            System.out.println("Checking Query Failed");
            System.err.println(e);
            return null;
        }
    }//END GET CUST CHECKING ACCT
    
    //BEGIN CHECK_FOR_CUSTOMER
    //Check to see if customer exists
    private boolean checkForCustomer(Customers c) throws SQLException{
        String sql = CHECK_FOR_CUSTOMER;
        
        //open database connection and check for customer
        Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getFname());
            ps.setString(2, c.getLname());
            ps.setString(3, c.getAddress());
            
            ResultSet rs = ps.executeQuery();
            //if customer exists send alert CUSTOMER ALREADY EXISTS
            if(rs.next()){
                return true;
            }
        return false;
    }//END CHECK_FOR_CUSTOMER
    
    //BEGIN addCustomer
    /*Adds new customer to database, input Customers object, called from 
    addAcct().  Throws exeception.
    */
    private int addCustomer(Customers c) throws SQLException{
        //Check if customer exists
        if(!checkForCustomer(c)){
            String sql = INSERT_RECORD_CUSTOMERS;
            // If customer does not exist get connection and add customer
            //Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement
                (sql, PreparedStatement.RETURN_GENERATED_KEYS);                
                ps.setString(1, c.getFname());
                ps.setString(2,c.getLname());
                ps.setString(3, c.getAddress());
                ps.setString(4, c.getCity());
                ps.setString(5,c.getState());
                ps.setInt(6,c.getZip());
                ps.setString(7, c.getPhone());
                //execute query to insert customer and return number of rows affected
                int affectedRows = ps.executeUpdate();
                //If the number of rows affected was not 1 then throw exception
                if(affectedRows != 1){
                    throw new SQLException("Update Failed");
                }
                //If inserted correctly return custID, else throw exception
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }else{
                    throw new SQLException("No custId found");
                }
        }
        return -99;
    }//END ADD_CUSTOMER
    
    //BEGIN ADD_SAVINGS_ACCT
    //Adds new savings acct, requres custId and intRate, calls addCustomer which
    //requires fname, lname, address, city, state, zip, phone
    public int addSavingsAcct(Customers c, double intRate){
        String sql = INSERT_RECORD_SAVINGS_ACCT;
        //Set connection
        try(Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement
                (sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            //turn auto commit off
            conn.setAutoCommit(false);
            /*
            BEGIN TRANSACTION 
            Retrieve custId by adding customer record, if incomplete with throw 
            exception and initiate rollback
            */
            int custId = addCustomer(c);
            if(custId == -99){
                return -99;
            }else{
                //Begin creation of savings acct
                ps.setInt(1, custId);
                ps.setDouble(2, intRate);
                //execute update to savings acct 
                int affectedRows = ps.executeUpdate();
                /*
                END TRANSACTION
                If number of affected rows is 1 transaction is committed and
                acct number is returned
                */
                if(affectedRows == 1){
                    conn.commit();
                    try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                        if(generatedKeys.next()){
                            return generatedKeys.getInt(1);
                        }
                    }
                }else{
                    throw new SQLException("Add customer savings acct failed.");
                }
            }
        }catch(SQLException e){
            System.out.println("Could not add customer savings acct");
            System.err.println(e);
            try{
                System.out.println("Performing rollback...");
                connection.rollback();
            }catch(SQLException e2 ){
                System.out.println("Couldn't perform rollback.");
                System.err.println(e2);
            }
        }     
        return -1;
    }//END ADD_SAVINGS_ACCT
    
    
    //BEGIN updateCustInfo() 
    //Updates customer address and phone number
    public boolean updateCustInfo(int custId, String address, String city,
            String state, int zip, String phone){
        
        String sql = UPDATE_CUST_INFO;
        
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, address);
                ps.setString(2, city);
                ps.setString(3, state);
                ps.setInt(4, zip);
                ps.setString(5, phone);
                ps.setInt(6, custId);
                
                ps.executeUpdate();
                
                return true;
        }catch(SQLException e){
                System.out.println("Update cust info failed");
                System.err.println(e);
                return false;
        }
    }// END updateCustInfo()
    
    //BEGIN updateSavingsBalance()
    //Update savings acct balance
    public boolean updateSavingsBalance(int custId, double balance){
        
        String sql = UPDATE_SAVINGS_ACCT_BALANCE;
        
        try(Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, balance);
            ps.setInt(2, custId);
            
            ps.executeUpdate();
            
            return true;
        }catch(SQLException e){
            System.out.println("Balance update failed.");
            System.err.println(e);
            return false;
        }
        
    }//END updateSavingsBalance()
    
    //BEGIN updateCheckingBalance()
    //Updates balance of checking acct
    public boolean updateCheckingBalance(int custId, double balance){
        
        String sql = UPDATE_CHECKING_ACCT_BALANCE;
        
        try(Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDouble(1, balance);
            ps.setInt(2, custId);
            
            ps.executeUpdate();
            
            return true;
        }catch(SQLException e){
            System.out.println("Checking Balance update failed.");
            System.err.println(e);
            return false;
        }
        
    }//END updateCheckingBalance()
}