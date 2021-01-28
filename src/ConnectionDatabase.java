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
        if (!pw.equals(pwRepeat)) return -6;
        openConnection();

        try (Statement statement = conn.createStatement()) {
            String id = "";
            String bankAccount = "";
            String addressID = "";
            String countryID = "";
            int rnd;

            ResultSet rs = statement.executeQuery("SELECT country_id FROM countries WHERE country_name = UPPER('"+ country + "')");
            while (rs.next()) countryID = rs.getString(1);
            if(countryID.length() == 0) return -2;

            try(CallableStatement  callstatement = conn.prepareCall("{? = call test_date(?)}")){
                callstatement.registerOutParameter(1, Types.VARCHAR);
                callstatement.setString(2, birthdate);
                callstatement.execute();
                if(callstatement.getString(1).equals("Invalid")) return -3;
            }
            
            if(!phone.matches("\\d+")||phone.length()!=9) return -4;
            rs = statement.executeQuery("SELECT username FROM users");
            while (rs.next()) if (username.equals(rs.getString(1))) return -5;

            if(!home.matches("\\d+")) return -7;

            if(countryID.length() == 0) return -2;


            rs = statement.executeQuery("SELECT SEQ_CLIENTS.NEXTVAL FROM DUAL");
            
            while (rs.next()) {
                id = rs.getString(1);
            }

            rs = statement.executeQuery("SELECT BANK_ACCOUNT FROM ALL_ACCOUNTS");
            ArrayList<String> allAccounts = new ArrayList<>();
            while (rs.next()) {
                allAccounts.add(rs.getString(BANK_ACCOUNT)); // getting bank account
            }
            rnd = 100000 + rand.nextInt(900000);
            while(allAccounts.contains(bankAccount) || rnd/100000 == 5){
                rnd = 100000 + rand.nextInt(900000);
                bankAccount = String.valueOf(rnd);
            }
            bankAccount = String.valueOf(rnd);
            rs = statement.executeQuery("SELECT SEQ_ADDRESSES.NEXTVAL FROM DUAL");
            while(rs.next()) addressID = rs.getString(1);

            statement.executeUpdate("INSERT INTO ADDRESSES VALUES("+ addressID+ " , " + countryID + " , '" + city + "','"+ street + "', "+ home+ " , '"+appartment+"', '"+postCode+"')");

            statement.executeUpdate("INSERT INTO CLIENTS VALUES(" + id + ",'" + firstName + "','" + secondName + "','"+ birthdate + "'," + phone + ",'" + email + "', " + addressID + ", 1)");

            statement.executeUpdate("INSERT INTO ALL_ACCOUNTS VALUES("+ bankAccount +", "+ 1 + ", SYSDATE, NULL, " + id + " )");

            statement.executeUpdate("INSERT INTO BANK_ACCOUNTS VALUES("+ id +", "+ bankAccount + ", " + 0 + ")");

            statement.executeUpdate("INSERT INTO USERS VALUES(" + id + ", '" + username + "', '" + pw + "')");

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
        String tableUsername = "";
        String tablePassword = "";
        int id = -1;
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM USERS");
            while (rs.next()) {
                tableUsername = rs.getString(USERNAME);
                tablePassword = rs.getString(PW);
                if (username.equals(tableUsername) && pw.equals(tablePassword)) {
                    id = rs.getInt("CLIENT_ID");
                    System.out.println(id);
                    closeConnection();
                    return id;
                }
            }
            return -2;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return id;
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
            while (rs.next()) {
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

    public Object[][] getSavingAccounts(int id) {
        openConnection();
        try (Statement statement = conn.createStatement()) {
            String bankAccount = "";
            String savingBankAccount = "";
            String bankAccountProfit = "";
            String bankAccountFunds = "";
            String savingBankAccountFunds = "";
            String savingBankAccountProfit = "";
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM BANK_ACCOUNTS JOIN CLIENTS using(client_id) JOIN client_type using(type_id) WHERE CLIENT_ID ="
                            + id);
            while (rs.next()) {
                bankAccount = rs.getString(BANK_ACCOUNT);
                bankAccountFunds = rs.getString("AVAILABLE_FUNDS");
                bankAccountProfit = rs.getString("ACCOUNT_PROFIT");
                savingBankAccountProfit = rs.getString("SAVING_ACCOUNT_PROFIT");
            }
            ArrayList<Object[]> data = new ArrayList<>();
            ArrayList<String> set = new ArrayList<>();
            set.add("Main Bank Account");
            set.add(bankAccount);
            set.add(bankAccountFunds);
            set.add(bankAccountProfit);
            data.add(set.toArray());
            int savingBankAccountCounter = 1;
            rs = statement.executeQuery("SELECT * FROM SAVING_BANK_ACCOUNTS WHERE CLIENT_ID =" + id);
            while (rs.next()) {
                savingBankAccount = rs.getString(BANK_ACCOUNT);
                savingBankAccountFunds = rs.getString("AVAILABLE_FUNDS");
                set.clear();
                set.add("Saving Bank Account " + Integer.toString(savingBankAccountCounter));
                set.add(savingBankAccount);
                set.add(savingBankAccountFunds);
                set.add(savingBankAccountProfit);
                data.add(set.toArray());
                savingBankAccountCounter++;
            }
            closeConnection();
            Object[][] ret = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                ret[i] = data.get(i);
            }
            return ret;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return new Object[][] { { "Konto 1", 1, 2, 3, "" }, { "Konto 2", 4, 5, 6, "" } };
    }

    public Object[][] getTransactionHistory(int id) {
        openConnection();
        try (Statement statement = conn.createStatement()){
            String bankAccount = "";
            ResultSet rs = statement.executeQuery("SELECT bank_account FROM bank_accounts WHERE CLIENT_ID = " + id);
            ArrayList<Object[]> data = new ArrayList<>();
            ArrayList<String> set = new ArrayList<>();
            while (rs.next()) {
                bankAccount = rs.getString(BANK_ACCOUNT);
            }
            rs = statement.executeQuery("SELECT * FROM TRANSACTIONS WHERE SOURCE = " + bankAccount + "  OR TARGET = "
                    + bankAccount + "ORDER BY TRANSACTION_DATE DESC");

            while (rs.next()) {
                set.clear();
                set.add(rs.getString("source"));
                set.add(rs.getString("target"));
                set.add(rs.getString("amount"));
                set.add(rs.getString("transaction_date"));
                data.add(set.toArray());
            }
            Object[][] ret = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                ret[i] = data.get(i);
            }
            return ret;

        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return new Object[][] { {} };
    }

    public int makeTransaction(String id, String amount, String target) {
        int clientID = Integer.parseInt(id);
        int amnt = Integer.parseInt(amount);
        int trgt = Integer.parseInt(target);
        if (amnt <= 0 || target.length() != 6) return -4;
        openConnection();
        try (CallableStatement callstatement = conn.prepareCall("{call make_transaction(?, ?, ?, ?)}")) {
            callstatement.setInt(1, clientID);
            callstatement.setInt(2, amnt);
            callstatement.setInt(3, trgt);
            callstatement.registerOutParameter(4, Types.NUMERIC);
            return callstatement.executeUpdate();
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return -3;
    }

    public int changeClientUsername(String username, int id){
        String oldUsername = "";
        openConnection();
        try(Statement statement = conn.createStatement()){
            ResultSet rs = statement.executeQuery("SELECT username from users where client_id =" + id);
            while(rs.next()) oldUsername = rs.getString(USERNAME);
            if (oldUsername.equals(username)) return -3;
            rs = statement.executeQuery("SELECT username from users");
            while(rs.next()) if(username.equals(rs.getString(USERNAME))) return -2;
            statement.executeUpdate("UPDATE USERS SET username = '"+ username +"' WHERE client_id ="+id);
            return 0;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return -1;
    }

    public int changeClientEmail(String email, int id){
        openConnection();
        try(Statement statement = conn.createStatement()){
            statement.executeUpdate("UPDATE CLIENTS SET email = '"+ email +"' WHERE client_id ="+id);
            return 0;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int changeClientName(String name, int id){
        openConnection();
        try(Statement statement = conn.createStatement()){
            statement.executeUpdate("UPDATE CLIENTS SET first_name = '"+ name +"' WHERE client_id ="+id);
            return 0;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int changeClientSurame(String surname, int id){
        openConnection();
        try(Statement statement = conn.createStatement()){
            statement.executeUpdate("UPDATE CLIENTS SET second_name = '"+ surname +"' WHERE client_id ="+id);
            return 0;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int changeClientPhone(String phone, int id){
        openConnection();
        try(Statement statement = conn.createStatement()){
            statement.executeUpdate("UPDATE CLIENTS SET phone_number = '"+ phone +"' WHERE client_id ="+id);
            return 0;
        } catch (SQLException e) {
            System.err.format(SQLSTATE, e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
