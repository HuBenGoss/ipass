package persistence;



import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

public class PostgresBaseDao {
    protected final Connection getConnection() {
        Connection result = null;

        try {
            Class.forName("org.postgresql.Driver");

//            InitialContext ic = new InitialContext();
//            DataSource datasource = (DataSource) ic.lookup("java:comp/env/jdbc/PostgresDS");
//
//            result = datasource.getConnection();
//            source.getConnection();
            result = DriverManager.getConnection(
                    "jdbc:postgresql://ec2-79-125-6-160.eu-west-1.compute.amazonaws.com:5432/d4235js315g98f?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory","eqegubxfglkhao", "ffbeb1ea8d91bb69f731b11f5637303b212730b6bcd6d78ce5c706482cbb558e");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }
}

