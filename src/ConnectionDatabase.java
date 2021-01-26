import java.sql.*;
import java.util.ArrayList;
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
        String appartment = info[12];
        String postCode = info[13];
        if (!pw.equals(pwRepeat))
            return -1;
        openConnection();
        try (Statement statement = conn.createStatement()){
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
                    + street + "','" + home + "','" + appartment + "','" + postCode + "')");
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
                int id = rs.getInt("CLIENT_ID");
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

    public Map getClientData(int id) {
        Map<String, String> clientData = new HashMap<>();
        String clientID = Integer.toString(id);
        clientData.put("ID", clientID);
        openConnection();
        try (Statement statement = conn.createStatement()){
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
            String countryID = "";
            String country = "";
            String city = "";
            String street = "";
            String home = "";
            String addressID = "";
            String appartment = "";
            String postCode = "";
            ResultSet rs = statement.executeQuery("SELECT * FROM CLIENTS WHERE CLIENT_ID=" + clientID);
            while (rs.next()) {
                firstName = rs.getString("FIRST_NAME");
                secondName = rs.getString("SECOND_NAME");
                birthDate = rs.getDate("BIRTH_DATE").toString();
                phoneNumber = rs.getString("PHONE_NUMBER");
                email = rs.getString("EMAIL");
                addressID = rs.getString("ADDRESS_ID");
            }
            clientData.put("FIRST_NAME", firstName);
            clientData.put("SECOND_NAME", secondName);
            clientData.put("BIRTH_DATE", birthDate);
            clientData.put("PHONE_NUMBER", phoneNumber);
            clientData.put("EMAIL", email);

            rs = statement.executeQuery("SELECT * FROM BANK_ACCOUNTS WHERE CLIENT_ID=" + clientID);
            while (rs.next()) {
                bankAccount = rs.getString(BANK_ACCOUNT);
                bankAccountFunds = rs.getString("AVAILABLE_FUNDS");
            }
            clientData.put(BANK_ACCOUNT, bankAccount);
            clientData.put("BANK_ACCOUNT_FUNDS", bankAccountFunds);

            rs = statement.executeQuery("SELECT * FROM SAVING_BANK_ACCOUNTS WHERE CLIENT_ID=" + clientID);
            while (rs.next()) {
                savingBankAccount = rs.getString(BANK_ACCOUNT);
                savingBankAccountFunds = rs.getString("AVAILABLE_FUNDS");
            }
            clientData.put("SAVING_BANK_ACCOUNT", savingBankAccount);
            clientData.put("SAVING_BANK_ACCOUNT_FUNDS", savingBankAccountFunds);

            rs = statement.executeQuery("SELECT * FROM USERS WHERE CLIENT_ID=" + clientID);
            while (rs.next()) {
                username = rs.getString(USERNAME);
                pw = rs.getString(PW);
            }
            clientData.put(USERNAME, username);
            clientData.put(PW, pw);

            rs = statement.executeQuery("SELECT * FROM ADDRESSES WHERE ADDRESS_ID=" + addressID);
            while (rs.next()) {
                countryID = rs.getString("COUNTRY_ID");
                city = rs.getString("CITY");
                street = rs.getString("STREET");
                home = rs.getString("HOME");
                appartment = rs.getString("APPARTMENT");
                postCode = rs.getString("POST_CODE");
            }
            rs = statement.executeQuery("SELECT * FROM COUNTRIES WHERE COUNTRY_ID=" + countryID);
            while (rs.next()){
                country = rs.getString("COUNTRY_NAME");
            }
            address = country + ", " + city + ", " + street + " " + home + ", " + appartment + ", " + postCode;
            clientData.put("ADDRESS", address);

        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return clientData;
    }

    public Object[][] getSavingsAccount(int id){
        return new Object[][] { { "Konto 1", 123123, 1000, 3, "" },
                            { "Konto 2", 1123, 13000, 11, ""  },
                            { "Konto 3", 11, 22, 33, ""  } ,
                            { "Konto 3", 11, 22, 33, ""  } };
    }

    public Object[][] getTransactionHistory(int id){
        openConnection();
        try (Statement statement = conn.createStatement()){
            String bankAccount = new String("");
            ResultSet rs = statement.executeQuery("SELECT bank_account FROM bank_accounts WHERE CLIENT_ID = " + id);
            ArrayList<Object[]> data = new ArrayList<>();
            ArrayList<String> set = new ArrayList<>();
            while(rs.next()){bankAccount = rs.getString(BANK_ACCOUNT);}
            rs = statement.executeQuery("SELECT * FROM TRANSACTIONS WHERE SOURCE = " + bankAccount + "  OR TARGET = " + bankAccount + "ORDER BY TRANSACTION_DATE DESC");
            while(rs.next()){
                set.clear();
                set.add(rs.getString("source"));
                set.add(rs.getString("target"));
                set.add(rs.getString("amount"));
                set.add(rs.getString("transaction_date"));
                data.add(set.toArray());
            }
            Object[][] ret = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++){
                ret[i] = data.get(i);
            }
            return ret;

        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return new Object[][] { { } };
    }

    public int makeTransaction(String id, String amount, String target) {
        openConnection();
        try (CallableStatement  callstatement = conn.prepareCall("{call make_transaction(?, ?, ?, ?)}")) {
            callstatement.setString(1, id);
            callstatement.setString(2, amount);
            callstatement.setString(3, target);
            callstatement.registerOutParameter(4, Types.NUMERIC);
            return callstatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return -2;
    }

}
