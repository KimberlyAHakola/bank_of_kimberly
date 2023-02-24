/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */


public class CheckingAcct {
    
    private int acctNum;
    private int custId;
    private double balance;
    private double intRate;
    
    //Getters and setters
    public int getAcctNum(){
        return acctNum;
    }
    
    public void setAcctNum(int acctNum){
        this.acctNum = acctNum;
    }
    
    public int getCustId(){
        return custId;
    }
    
    public void setCustId(int custId){
        this.custId = custId;
    }
    
    public double getBalance(){
        return balance;
    }
    
    public void setBalance(double balance){
        this.balance = balance;
    }
    
    public double getIntRate(){
        return intRate;
    }
    
    public void setIntRate(double intRate){
        this.intRate = intRate;
    }
}
