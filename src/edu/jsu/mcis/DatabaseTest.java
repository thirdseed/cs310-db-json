package edu.jsu.mcis;

import java.sql.*;
import org.json.simple.*;

public class DatabaseTest {
    
    /**
     * A method designed to test SQL database interfacing via Java; demonstrates
     * the results of an SQL query being parsed and converted into JSON
     * @return A JSON String representing to contents of a specific database
     * table
     */
    public static JSONArray getJSONData() {
                
        JSONArray convertedQueryResults = new JSONArray();
            
        Connection conn = null;
        PreparedStatement pstSelect = null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;
        
        String query;
        
        boolean hasResults;
        int columnCount;
        
        try {
            
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "CS488";
            
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection(server, username, password);
            
            if (conn.isValid(0)) {
                
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query); 
                
                hasResults = pstSelect.execute();

                if (hasResults) {
                        
                    resultSet = pstSelect.getResultSet();
                    metaData = resultSet.getMetaData();
                    columnCount = metaData.getColumnCount();
                        
                    while (resultSet.next()) {
                        
                        JSONObject JO = new JSONObject();
                        
                        // start from index = 2 to skip key field `id` specified in schema
                        for (int i = 2; i <= columnCount; i++) {
                            
                            String key = metaData.getColumnLabel(i);
                            
                            JO.put(key,resultSet.getString(key));
                            
                        }
                        
                        convertedQueryResults.add(JO);
                        
                    }
                        
                }
                
            }
                
            conn.close();
            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultSet != null) { try { resultSet.close(); resultSet = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
        }
        
        return convertedQueryResults;
    }

    public static void main(String[] args) {
        
        System.out.println(getJSONData().toJSONString());
        
    }
    
}