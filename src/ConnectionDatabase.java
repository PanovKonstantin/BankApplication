import java.sql.*;
import java.util.Random;

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

    public boolean addClient(String firstName, String lastName, String username, String email, String address,
            String birthdate, String phone, String password, String pwRepeat) {
        if (!password.equals(pwRepeat))
            return false;
        openConnection();
        try {
            Statement statement = conn.createStatement();
            String id = "";
            String bankAccount = "";
            String savingBankAccount = "";
            Random rand = new Random();
            ResultSet rs = statement.executeQuery("select seq_users.nextval from dual");
            while (rs.next()) {
                id = rs.getString(1); // getting client id
            }
            rs = statement.executeQuery("select BANK_ACCOUNT, IN_USE from ALL_ACCOUNTS where IN_USE=0");
            while (rs.next()) {
                bankAccount = rs.getString("BANK_ACCOUNT"); // getting bank account
                break;
            }
            while (rs.next()) {
                savingBankAccount = rs.getString("BANK_ACCOUNT"); // getting saving bank account
                break;
            }
            statement.executeUpdate("UPDATE ALL_ACCOUNTS SET IN_USE = 1 WHERE BANK_ACCOUNT=" + bankAccount);
            statement.executeUpdate("UPDATE ALL_ACCOUNTS SET IN_USE = 1 WHERE BANK_ACCOUNT=" + savingBankAccount);

            statement.executeUpdate("INSERT INTO CLIENTS VALUES('" + id + "','" + firstName + "','" + lastName + "','"
            + birthdate + "','" + phone + "','" + email + "')");
            statement.executeUpdate("INSERT INTO BANK_ACCOUNTS VALUES('" + id + "','" + bankAccount + "','"
                    + Integer.toString(rand.nextInt(5000)) + "')");
            statement.executeUpdate("INSERT INTO SAVING_BANK_ACCOUNTS VALUES('" + id + "','" + savingBankAccount + "','"
                    + Integer.toString(rand.nextInt(50000)) + "')");
            statement.executeUpdate("INSERT INTO USERS VALUES('" + id + "','" + username + "','" + password + "')");
            closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return false;
    }

    public boolean loginUser(String username, String password) {
        openConnection();
        String query = "select ID, USERNAME, PASSWORD from USERS";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
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

    public boolean addAccounts(String[] accounts) {
        openConnection();
        try {
            Statement statement = conn.createStatement();
            for (int i = 0; i < accounts.length; i++) {
                statement.executeUpdate("INSERT INTO ALL_ACCOUNTS VALUES('" + accounts[i] + "','0')");
            }
            closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return false;
    }

}
