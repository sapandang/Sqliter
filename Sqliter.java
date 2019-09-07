package skd.common;

//import org.apache.commons.io.FileUtils; //removed dependency
import org.sqlite.jdbc4.JDBC4ResultSet;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * only support for the string as of now
 * @author sapan.dang
 */
public class Sqliter {

    String dbName = "";
    Connection con = null;


    public Sqliter(String dbName) {
        this.dbName = "gendata/" + dbName+".db";

        openDb();
    }

    public void deleteDb() {
        try {


            //FileUtils.forceDelete(new File(dbName)); //apache commons io
            new File(dbName).delete(); //delete file
            System.out.println("DB File deleted success");
        } catch (Exception e) {
            System.out.println("ERROR DELETING FILE " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void openDb() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            System.out.println("Opened database successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDb() {

        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createTable(String tableName, HashMap<String, String> tabledata) {

        try {

            //generate the table query
            //String format : " NAME           TEXT, "
            String query = "";
            for (int i = 0; i < tabledata.size(); i++) {

                String columnName = tabledata.keySet().toArray()[i].toString();
                String columnValue = tabledata.get(columnName);

                query += " " + columnName + " " + columnValue;
                if (i < (tabledata.size() - 1)) {
                    query += ",";
                }

            }

            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE " + tableName +
                    "(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + query +
                    ")";

            System.out.println("table created " + sql);
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error While creating table " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void insertData(String tableName, HashMap<String, String> tabledata) {

        try {

            String columnNamesString = "";
            String columnValuesString = "";

            for (int i = 0; i < tabledata.size(); i++) {

                String columnName = tabledata.keySet().toArray()[i].toString();
                String columnValue = tabledata.get(columnName);

                columnNamesString += "" + columnName;
                columnValuesString += "'" + columnValue+"'";

                if (i < (tabledata.size() - 1)) {
                    columnNamesString += ",";
                    columnValuesString += ",";
                }

            }


            Statement stmt = con.createStatement();
            String sql = "INSERT INTO " + tableName +
                    "(" +
                    columnNamesString
                    + ") VALUES (" +
                    columnValuesString
                    + ");";

            System.out.println("Data inserted " + sql);
            stmt.executeUpdate(sql);
            stmt.close();


        } catch (Exception e) {
            System.out.println("Error inserting data " + e.getMessage());
            e.printStackTrace();
        }


    }

    public int updateData(String tableName, HashMap<String, String> tabledata)
    {
            try{
                String coulmnwithValues="";
                String conditions = "";

                for (int i = 0; i < tabledata.size(); i++) {

                    String columnName = tabledata.keySet().toArray()[i].toString();
                    String columnValue = tabledata.get(columnName);

                    if(columnName.toLowerCase().equals("id"))
                    {
                        conditions+="ID="+columnValue+" ";


                    }else {

                        coulmnwithValues += columnName + "='" + columnValue + "' ";
                        if (i < (tabledata.size() - 2)) {

                            coulmnwithValues += ",";
                        }
                    }




                }


                //sql query
                String query = "UPDATE "+tableName+" SET "+coulmnwithValues+" WHERE "+conditions+";";
                System.out.println(""+query);
                Statement stmt = con.createStatement();
                return  stmt.executeUpdate(query);

            }catch (Exception e)
            {

                System.out.println("Error while updating data "+e.getMessage());
                e.printStackTrace();
            }

            return 0;

    }


    public int deleteData(String tableName, HashMap<String, String> tabledata)
    {
        try{

            //sql query
            String query = "DELETE from "+tableName+" where ID="+tabledata.get("ID")+";";
            System.out.println(""+query);
            Statement stmt = con.createStatement();
            return  stmt.executeUpdate(query);

        }catch (Exception e)
        {

            System.out.println("Error while deleting data "+e.getMessage());
            e.printStackTrace();
        }

        return 0;

    }





    public int runUpdateOrDelete(String query)
    {

        try {

            Statement stmt = con.createStatement();
            return  stmt.executeUpdate(query);

        }catch (Exception e)
        {
            System.out.println("ERROR UPDATE "+e.getMessage() );
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<HashMap<String,String>> runQuery(String query)
    {
        ArrayList<HashMap<String,String>> resultList = new ArrayList<>();
        try {

            //check if it a update statement or not
            String checkquery = query.trim().toLowerCase();

            if(checkquery.startsWith("update"))
            {
                System.out.println("This is update query");
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                return null;
            }



            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery( query );
            String[] columnNames = ((JDBC4ResultSet) rs).cols;

            while ( rs.next() ) {
                HashMap<String,String> resultData  = new HashMap<>();

                for(int i=0;i<columnNames.length;i++) {

                    resultData.put(columnNames[i],rs.getString(columnNames[i]));

                }
                resultList.add(resultData);
            }
            rs.close();
            stmt.close();


        }catch (Exception e)
        {
            System.out.println("ERROR WHILE RUNNING Query "+e.getMessage());
            e.printStackTrace();
        }
        return resultList;

    }



}
