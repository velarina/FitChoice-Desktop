package fitChoice;

import java.sql.*;

public class Connect {

    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String HOST = "localhost:3306";
    private final String DATABASE = "fitchoice";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    private Connection con;
    private Statement st; //untuk melempar statement query ke database

    public ResultSet rs;
    public ResultSetMetaData rsm;

    public static Connect connect;

    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //	SINGLETON METHOD
    public static Connect getInstance() {
        if(connect == null) return new Connect();
        return connect;
    }

    //	Ambil Data
    public ResultSet execQuery(String query) {
        try {
            rs = st.executeQuery(query);
            rsm = rs.getMetaData();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rs;
    }

    //	Modify Data
    public void execUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}