package dao;

import java.util.Random;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.*;
import java.sql.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

import util.Log;

public class UserManagerDAO {

    public UserManagerDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected static Connection getConnection() {
        try {
            return DriverManager.getConnection(InitDAO.DBURL, InitDAO.DBUSER, InitDAO.DBCODE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    user_table:

    user_id INT UNSIGNED AUTO_INCREMENT
    user_name VARCHAR NOT NULL
    user_passwd VARCHAR NOT NULL
    create_time DATE
    permission INT
    passwd_salt VARCHAR NOT NULL
    */

    public static void init() {
        // UserManagerDAO thiz = new UserManagerDAO();
        String sql = "CREATE TABLE IF NOT EXISTS user_table (user_id INT UNSIGNED AUTO_INCREMENT, user_name VARCHAR(100) NOT NULL, user_passwd VARCHAR(100) NOT NULL, create_time DATE, permission INT, passwd_salt VARCHAR(100) NOT NULL, PRIMARY KEY (user_id ))ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        // String queryAdmin = "SELECT * FROM user_table WHERE user_name='administrator';";

        try (Connection conn = getConnection();
             Statement stat = conn.createStatement();) {

            stat.executeUpdate(sql);

            /* ResultSet rs = stat.executeQuery(queryAdmin);
            if (!rs.next()) {
                thiz.addUser("administrator", "2Mg+O2=2MgO");
            } */
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;
    }

    public static boolean validPassWd(String passWd) {
        if (null == passWd) {
            return false;
        } else if (passWd.length() < 8) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validUserName(String userName) {
        if (null == userName) {
            return false;
        } else if (userName.length() < 6 || userName.length() > 20) {
            return false;
        } else {
            return true;
        }
    }

    public static String generateSalt() {
        Random ranGen = new SecureRandom();
        byte[] aesKey = new byte[20];
        ranGen.nextBytes(aesKey);

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < aesKey.length; i++) {
            String hex = Integer.toHexString(0xff & aesKey[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String toMD5(String plainText) {
        String result = "";
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            //System.out.println("16位: " + buf.toString().substring(8, 24));// 16位的加密，其实就是32位加密后的截取
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getUserName(int userId) {
        if (userId < 0) {
            return "";
        }

        String sql = "SELECT user_name FROM user_table WHERE user_id=?";
        try (Connection conn = getConnection();
            PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, userId);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                return rs.getString("user_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static int getUserId(String userName)
    {
        if (null == userName) {
            return 0;
        }

        String sql = "SELECT user_id FROM user_table WHERE user_name=?";
        try (Connection conn = getConnection();
            PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setString(1, userName);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int addUser(String userName, String UserPassWd) {

        if (null == userName) {
            Log.Info("userName is null");
            return -1;
        } else if (true != validUserName(userName)) {
            Log.Info("invalid username " + userName);
            return -2;
        } else if (true != validPassWd(UserPassWd)) {
            Log.Info("invalid password " + UserPassWd);
            return -3;
        } else {
            // log
        }

        Date currentDate = new java.sql.Date(System.currentTimeMillis());

        String salt = generateSalt();
        if (null == salt) {
            Log.Error("failed to generate salt");
            return -4;
        }

        String PassWdStor = toMD5(toMD5(UserPassWd) + salt);

        String sql = "INSERT INTO user_table VALUES(null, ?, ?, ?, ?, ?);";
        String dupsql = "SELECT user_name FROM user_table WHERE user_name = ?;";

        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);
                // add these two parameter to move pointer freely
                PreparedStatement dupstat = conn.prepareStatement(dupsql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);) {

            dupstat.setString(1, userName);
            ResultSet rs = dupstat.executeQuery();
            rs.last();
            int row = rs.getRow();
            rs.beforeFirst();
            if (0 != row) {
                // have dup
                return -5;
            }

            stat.setString(1, userName);
            stat.setString(2, PassWdStor);
            stat.setString(3, currentDate.toString());
            stat.setInt(4, 0);
            stat.setString(5, salt);

            stat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return -6;
        }

        return 0;
    }

    public int authUser(String userName, String userPassWd) {
        int status = -3;

        if (true != validUserName(userName)) {
            return -1;
        } else if (true != validPassWd(userPassWd)) {
            return -2;
        } else {
            status = -3;
        }

        String sql = "SELECT user_id, user_passwd, permission, passwd_salt FROM user_table WHERE user_name = ? limit 1;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, userName);

            ResultSet rs = stat.executeQuery();

            rs.next();
            String userId = rs.getString("user_id");
            String passWdStor = rs.getString("user_passwd");
            int permission = rs.getInt("permission");
            String salt = rs.getString("passwd_salt");


            String authPassWd = toMD5(toMD5(userPassWd) + salt);
            if (true == authPassWd.equals(passWdStor)) {
                status = Integer.valueOf(userId);
            }

            if (0 > permission) {
                status = -5;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -4;
        }

        return status;
    }

    public String getPasswd(String userName) {
        if (null == userName) {
            return null;
        }

        String sql = "SELECT user_passwd FROM user_table WHERE user_name = ? limit 1;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, userName);

            ResultSet rs = stat.executeQuery();

            rs.next();
            String passWdStor = rs.getString("user_passwd");
            if (null != passWdStor) {
                return passWdStor;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

};