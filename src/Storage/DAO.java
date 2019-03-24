package Storage;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by Ramzah Rehman on 11/5/2016.
 */
public class DAO implements IDAO{

    Connection con;
  public DAO(){
      try {
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           con = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-ND3EINK\\SQLEXPRESS;databaseName=OnlineStore;integratedSecurity=true");
      }
      catch (Exception e) {
          e.printStackTrace();
      }


  }

    @Override
    public HashMap<String,String> getCategories() {
        HashMap<String,String> hashMap=new HashMap<String,String>();

        try {
            Statement statement=con.createStatement();
            ResultSet rs=statement.executeQuery("select * from OnlineStore.Category ");

            int id;
            String title;
            int parencat;

            while(rs.next()){

                id=rs.getInt(1);
                title=rs.getString(2);
                parencat=rs.getInt(3);//if null it gets a zero

                hashMap.put(id + "~" + title , String.valueOf(parencat) );
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    @Override
    public HashMap<String,String> getItems() {
        HashMap<String,String> hashMap=new HashMap<String,String>();

        try {
            Statement statement=con.createStatement();

            ResultSet rs=statement.executeQuery("select * from OnlineStore.Item ");
            while(rs.next()){

                hashMap.put(rs.getString(1) , rs.getString(2)+"~"+rs.getString(3)+"~"+rs.getInt(4)+"~"+rs.getInt(5)+"~"+rs.getInt(6));
            }
            statement.close();
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


        return hashMap;
    }
}
