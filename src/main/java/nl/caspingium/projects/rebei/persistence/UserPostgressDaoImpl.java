package nl.caspingium.projects.rebei.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserPostgressDaoImpl extends PostgresBaseDao implements UserDao{

    private String getUserRole(String query) {
        try (Connection con = super.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while (dbResultSet.next()) {
                String role = dbResultSet.getString("role");
                return role;
            }
            return null;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
    }

    public String findRoleForUser(String username, String password) {
        return getUserRole("SELECT * FROM public.\"gebruiker\" WHERE gebruikersnaam = '"+username+"' AND password = '"+password+"'");
    }
}
