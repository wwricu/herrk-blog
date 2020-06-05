package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;


public class ClickCountDAO {

    public ClickCountDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOGDB", "sql_admin", "153226");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clickPlus() {

        try (Connection conn = getConnection(); Statement stat = conn.createStatement();) {
            String sql = "update CLICK_COUNT set count=count+'1'";
            stat.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;
    }

    public int getCount() {

        int count = 0;

        try (Connection conn = getConnection(); Statement stat = conn.createStatement();) {
            String sql = "select * from CLICK_COUNT LIMIT 1";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
};
