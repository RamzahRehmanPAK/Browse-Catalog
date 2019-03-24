package Business;

import Storage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ramzah Rehman on 11/5/2016.
 */
public class Item{

    private String  code;
    private String title;
    private String description;
    private int price;
    private int available;
    // private Category parentCategory;

    public Item( String code,String title, String description,int price,int available){

        this.code=code;
        this.title=title;
        this.description=description;
        this.price=price;
        this.available=available;
    }
    public static void loadItems(ArrayList<Category> rootCategories){

        IDAO dataAccessObject= new DAO();
        HashMap<String,String> hashMap=dataAccessObject.getItems();

        Item item;

        String code;
        String title;
        String description;
        int price;
        int available;
        int parentCategory;

        String string;

        int comma1,comma2,comma3,comma4;

        for(Map.Entry<String,String> entry:hashMap.entrySet()){

            code=entry.getKey();

            string=entry.getValue();

            comma1=string.indexOf('~',0);
            title=string.substring(0,comma1);

            comma2=string.indexOf('~',comma1+1);
            description=string.substring(comma1+1,comma2);

            comma3=string.indexOf('~',comma2+1);
            price=Integer.parseInt(string.substring(comma2+1,comma3));

            comma4=string.indexOf('~',comma3+1);
            available=Integer.parseInt(string.substring(comma3+1,comma4));

            parentCategory=Integer.parseInt(string.substring(comma4+1));

            item=new Item(code,title,description,price,available);

            for(Category category:rootCategories){

                if(category.addItem(item,parentCategory)==true){
                    break;
                }
            }
        }
    }
    public boolean matches(String str){
        return code.equals(str);
    }
    public  String getCode(){
        return code;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
   public  int getPrice(){
        return price;
    }
    public int getAvailable(){
        return available;
    }
}
