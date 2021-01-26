import java.sql.*;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class ConnectionDatabase {

    private static final String URL = "jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    private static final String LOGIN = "BD1_Z09";
    private static final String PASSWORD = "7fncmp";
    private Connection conn;
    static final String SQLSTATE = "SQL State: %s\n%s";
    static final String BANK_ACCOUNT = "BANK_ACCOUNT";
    static final String USERNAME = "USERNAME";
    static final String PW = "PASSWORD";
    Random rand = new Random();

    public void openConnection() {
        try {

            conn = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {

            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {

            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int addClient(String[] info) {
        String firstName = info[0];
        String secondName = info[1];
        String username = info[2];
        String email = info[3];
        String birthdate = info[4];
        String phone = info[5];
        String pw = info[6];
        String pwRepeat = info[7];
        String country = info[8];
        String city = info[9];
        String street = info[10];
        String home = info[11];
        String apartment = info[12];
        String postCode = info[13];
        if (!pw.equals(pwRepeat))
            return -1;
        openConnection();

        try (Statement statement = conn.createStatement()) {
            String id = "";
            String bankAccount = "";
            String savingBankAccount = "";
            ResultSet rs = statement.executeQuery("SELECT SEQ_USERS.NEXTVAL FROM DUAL");
            while (rs.next()) {
                id = rs.getString(1); // getting client id
            }

            rs = statement.executeQuery("SELECT BANK_ACCOUNT, IN_USE FROM ALL_ACCOUNTS WHERE IN_USE=0");
            while (rs.next()) {

                bankAccount = rs.getString(BANK_ACCOUNT); // getting bank account
                break;
            }
            while (rs.next()) {

                savingBankAccount = rs.getString(BANK_ACCOUNT); // getting saving bank account
                break;
            }
            statement.executeUpdate("UPDATE ALL_ACCOUNTS SET IN_USE = 1 WHERE BANK_ACCOUNT=" + bankAccount);
            statement.executeUpdate("UPDATE ALL_ACCOUNTS SET IN_USE = 1 WHERE BANK_ACCOUNT=" + savingBankAccount);

            statement.executeUpdate("INSERT INTO CLIENTS VALUES('" + id + "','" + firstName + "','" + secondName + "','"
                    + birthdate + "','" + phone + "','" + email + "')");
            statement.executeUpdate("INSERT INTO BANK_ACCOUNTS VALUES('" + id + "','" + bankAccount + "','"
                    + Integer.toString(rand.nextInt(5000)) + "')");
            statement.executeUpdate("INSERT INTO SAVING_BANK_ACCOUNTS VALUES('" + id + "','" + savingBankAccount + "','"
                    + Integer.toString(rand.nextInt(50000)) + "')");

            statement.executeUpdate("INSERT INTO USERS VALUES('" + id + "','" + username + "','" + pw + "')");
            statement.executeUpdate("INSERT INTO ADDRESSES VALUES('" + id + "','" + country + "','" + city + "','"
                    + street + "','" + home + "','" + apartment + "','" + postCode + "')");
            closeConnection();
            return Integer.parseInt(id);
        } catch (

        SQLException e) {

            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeConnection();

        return -1;

    }

    public int loginUser(String username, String pw) {
        openConnection();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM USERS");
            while (rs.next()) {
                int id = rs.getInt("ID");

                String tableUsername = rs.getString(USERNAME);
                String tablePassword = rs.getString(PW);
                if (username.equals(tableUsername) && pw.equals(tablePassword)) {
                    closeConnection();
                    return id;
                }
            }
        } catch (SQLException e) {

            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return -1;
    }

    public boolean addAccounts(String[] accounts) {
        openConnection();

        try (Statement statement = conn.createStatement()) {
            for (int i = 0; i < accounts.length; i++) {
                statement.executeUpdate("INSERT INTO ALL_ACCOUNTS VALUES('" + accounts[i] + "','0')");
            }
            closeConnection();
            return true;
        } catch (SQLException e) {

            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return false;
    }

    public Map getClientData(int id) {

        Map<String, String> clientData = new HashMap<>();
        String clientID = Integer.toString(id);
        clientData.put("ID", clientID);
        openConnection();

        try (Statement statement = conn.createStatement()) {
            String firstName = "";
            String secondName = "";
            String birthDate = "";
            String phoneNumber = "";
            String email = "";
            String bankAccount = "";
            String savingBankAccount = "";
            String bankAccountFunds = "";
            String savingBankAccountFunds = "";
            String username = "";
            String pw = "";
            String address = "";
            String country = "";
            String city = "";
            String street = "";
            String home = "";
            String apartment = "";
            String postCode = "";
            ResultSet rs = statement.executeQuery("SELECT * FROM CLIENTS WHERE ID=" + clientID);
            while (rs.next()) {
                firstName = rs.getString("FIRST_NAME");
                secondName = rs.getString("SECOND_NAME");
                birthDate = rs.getDate("BIRTH_DATE").toString();
                phoneNumber = rs.getString("PHONE_NUMBER");
                email = rs.getString("EMAIL");
            }
            clientData.put("FIRST_NAME", firstName);
            clientData.put("SECOND_NAME", secondName);
            clientData.put("BIRTH_DATE", birthDate);
            clientData.put("PHONE_NUMBER", phoneNumber);
            clientData.put("EMAIL", email);

            rs = statement.executeQuery("SELECT * FROM BANK_ACCOUNTS WHERE ID=" + clientID);
            while (rs.next()) {

                bankAccount = rs.getString(BANK_ACCOUNT);
                bankAccountFunds = rs.getString("AVAILABLE_FUNDS");
            }

            clientData.put(BANK_ACCOUNT, bankAccount);
            clientData.put("BANK_ACCOUNT_FUNDS", bankAccountFunds);

            rs = statement.executeQuery("SELECT * FROM SAVING_BANK_ACCOUNTS WHERE ID=" + clientID);
            while (rs.next()) {

                savingBankAccount = rs.getString(BANK_ACCOUNT);
                savingBankAccountFunds = rs.getString("AVAILABLE_FUNDS");
            }
            clientData.put("SAVING_BANK_ACCOUNT", savingBankAccount);
            clientData.put("SAVING_BANK_ACCOUNT_FUNDS", savingBankAccountFunds);

            rs = statement.executeQuery("SELECT * FROM USERS WHERE ID=" + clientID);
            while (rs.next()) {

                username = rs.getString(USERNAME);
                pw = rs.getString(PW);
            }

            clientData.put(USERNAME, username);
            clientData.put(PW, pw);

            rs = statement.executeQuery("SELECT * FROM ADDRESSES WHERE ID=" + clientID);
            while (rs.next()) {
                country = rs.getString("COUNTRY");
                city = rs.getString("CITY");
                street = rs.getString("STREET");
                home = rs.getString("HOME");
                apartment = rs.getString("APARTMENT");
                postCode = rs.getString("POST_CODE");
            }
            address = country + ", " + city + ", " + street + " " + home + ", " + apartment + ", " + postCode;
            clientData.put("ADDRESS", address);

        } catch (SQLException e) {

            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return clientData;
    }

    public Object[][] getSavingsAccount(int id) {
        return new Object[][] { { "Konto 1", 123123, 1000, 3, "" }, { "Konto 2", 1123, 13000, 11, "" },
                { "Konto 3", 11, 22, 33, "" }, { "Konto 3", 11, 22, 33, "" } };
    }

    public Object[][] getTransactionHistory(int id) {
        openConnection();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT bank_account FROM bank_accounts WHERE id = " + id);
            while (rs.next()) {
                String bankAccount = rs.getString(BANK_ACCOUNT);
            }
            rs = statement.executeQuery("SELECT * FROM TRANSACTION_HISTORY");
            while (rs.next()) {
                System.out.println(rs.getString("TRANSACTION_DATE"));
            }

        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return new Object[][] { { "vxvc", "xqerz", "wfda", 2222, "0000-00-00" } };
    }

    public int makeTransfer(String id, String amount, String target) {
        String bankAccount = "";
        String funds = "";
        String targetFunds = "";
        openConnection();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM BANK_ACCOUNTS WHERE ID = " + id);
            while (rs.next()) {
                bankAccount = rs.getString(BANK_ACCOUNT);
                funds = rs.getString("AVAILABLE_FUNDS");
            }
            rs = statement.executeQuery("SELECT * FROM BANK_ACCOUNTS WHERE BANK_ACCOUNT = " + target);
            while (rs.next()) {
                targetFunds = rs.getString("AVAILABLE_FUNDS");
            }
            if (Integer.parseInt(amount) > Integer.parseInt(funds)) {
                closeConnection();
                return -1;
            }
            statement.executeUpdate("UPDATE BANK_ACCOUNTS SET AVAILABLE_FUNDS = " + Integer.toString(Integer.parseInt(targetFunds) + Integer.parseInt(amount)) +  " WHERE BANK_ACCOUNT=" + target);
            statement.executeUpdate("UPDATE BANK_ACCOUNTS SET AVAILABLE_FUNDS = " + Integer.toString(Integer.parseInt(funds) - Integer.parseInt(amount)) +  " WHERE BANK_ACCOUNT=" + bankAccount);
            statement.executeUpdate("INSERT INTO TRANSACTIONS VALUES(seq_transactions.NEXTVAL,'" + bankAccount + "','" + target + "','"
                    + amount + "',SYSDATE)");
            closeConnection();
            return 1;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return -2;
    }
}
