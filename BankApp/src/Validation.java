/**
Murach, J. ( 2017). Murachs Java Programming, Training and 
Reference, 5th Edition, Fresno, CA: Mike Murach & Associates. Inc.
Modifications by K. Hakola, 2021 
 */


public class Validation {
    private String lineEnd;
    
    public Validation(){
        this.lineEnd = "\n";
    }
    
    public Validation(String lineEnd){
        this.lineEnd = lineEnd;
    }
    
    public String isPresent(String value, String name){
        String msg = "";
        if(value.isEmpty()){
            msg = name + " is required." + lineEnd;
        }
        return msg;
    }
    
    public String isDouble(String value, String name){
        String msg = "";
        try{
            Double.parseDouble(value);
        }catch(NumberFormatException e){
            msg = name + " msut be a valid number." + lineEnd;
        }
        return msg;
    }
    
    public String isInteger(String value, String name){
        String msg = "";
        try{
            Integer.parseInt(value);
        }catch (NumberFormatException e){
            msg = name + " must be an integer." + lineEnd;
        }
        return msg;
    }
}
