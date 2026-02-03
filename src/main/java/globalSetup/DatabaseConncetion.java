package globalSetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConncetion {
	private static final String DB_URL = GeneralMethods.getEnvData("db_url"); 
	private static final String DB_USER = GeneralMethods.getEnvData("db_userName");
	private static final String DB_PASSWORD = GeneralMethods.getEnvData("db_password");

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}
	
	public static boolean searchRecord(String clientID) {  
        String searchQuery = "SELECT COUNT(*) FROM candidates WHERE candidate_id = ?";  
          
        try (Connection conn = getConnection();  
             PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {  
              
            pstmt.setString(1, clientID);  
              
            try (ResultSet rs = pstmt.executeQuery()) {  
                if (rs.next()) {  
                    return rs.getInt(1) > 0;  
                }  
            }  
        } catch (SQLException e) {  
            System.err.println("Database search error: " + e.getMessage());  
            return false;  
        }  
          
        return false;  
    }  

	public static void deleteRecord(String clientID) throws SQLException {
		String deleteQuery = "DELETE FROM candidates WHERE candidate_id = ?";

		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

			pstmt.setString(1, clientID);
			int rowsAffected = pstmt.executeUpdate();

			System.out.println(rowsAffected + " record(s) deleted");
		}
	}
}
