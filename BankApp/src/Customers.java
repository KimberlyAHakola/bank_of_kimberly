/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */


public class Customers {
    
    private int custId;
    private String fname;
    private String lname;
    private String address;
    private String city;
    private String state;
    private int zip;
    private String phone;
    
    //Getters and setters 
    public int getCustId(){
        return custId;
    }
    
    public void setCustId(int custId){
        this.custId = custId;
    }
            
    public String getFname(){
        return fname;
    }
    
    public void setFname(String fname){
        this.fname = fname;
    }
    
    public String getLname(){
        return lname;
    }
    
    public void setLname(String lname){
        this.lname = lname;
    }
    
    public String getAddress(){
        return address;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public String getCity(){
        return city;
    }
    
    public void setCity(String city){
        this.city = city;
    }
    
    public String getState(){
        return state;
    }
    
    public void setState(String state){
        this.state = state;
    }
    
    public int getZip(){
        return zip;
    }
    
    public void setZip(int zip){
        this.zip = zip;
    }
    
    public String getPhone(){
        return phone;
    }
    
    public void setPhone(String phone){
        this.phone = phone;
    }
}
