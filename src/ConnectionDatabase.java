import java.sql.*;

public class ConnectionDatabase {

    private final String URL = "jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    private final String login = "BD1_Z09";
    private final String password = "7fncmp";
    private Connection conn;

    public void openConnection() {
        try {
            conn = DriverManager.getConnection(URL, login, password);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int addUser(String firstName, String secondName, String username, String email, String address,
            String birthdate, String phone, String password, String pwRepeat) {
        openConnection();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT username FROM users WHERE username = '" + username + "'");
            if (!rs.next()) {
                
                
                //ADD CLIENT


                stmt.executeUpdate(
                        "INSERT INTO USERS " + "VALUES(seq_users.currval,'" + username + "','" + password + "')");
                return 1;
            }
            return 2;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return -1;
    }

    public boolean loginUser(String username, String password) {
        openConnection();
        String query = "select ID, USERNAME, PASSWORD from USERS";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String table_username = rs.getString("USERNAME");
                String table_password = rs.getString("PASSWORD");
                if (username.equals(table_username) && password.equals(table_password)) {
                    closeConnection();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return false;
    }

    public void addAccounts(String[] accounts) {
        openConnection();
        try {
            Statement statement = conn.createStatement();
            for (int i = 0; i < accounts.length; i++) {
                statement.executeUpdate("INSERT INTO ALL_ACCOUNTS " + "VALUES('" + accounts[i] + "','0')");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

}
