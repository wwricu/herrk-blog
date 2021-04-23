package dao;

import util.UserInfo;
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
    user_group INT
    passwd_salt VARCHAR NOT NULL
    */

    public static void init() {
        // UserManagerDAO thiz = new UserManagerDAO();
        String sql = "CREATE TABLE IF NOT EXISTS user_table (user_id INT UNSIGNED AUTO_INCREMENT, user_name VARCHAR(100) NOT NULL, user_passwd VARCHAR(100) NOT NULL, create_time DATE, user_group INT, passwd_salt VARCHAR(100) NOT NULL, PRIMARY KEY (user_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;";
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
        } else if (passWd.indexOf('\'') != -1 ||
                   passWd.indexOf('\\') != -1 ||
                   passWd.indexOf('"') != -1 ||
                   passWd.indexOf('/') != -1 ||
                   passWd.indexOf('{') != -1 ||
                   passWd.indexOf('}') != -1 ||
                   passWd.indexOf('[') != -1 ||
                   passWd.indexOf(']') != -1 ||
                   passWd.indexOf(',') != -1 ||
                   passWd.indexOf(';') != -1) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean validUserName(String userName) {
        if (null == userName) {
            return false;
        } else if (userName.length() < 6 || userName.length() > 20) {
            Log.Error("userName's length is " + userName.length());
            return false;
        } else if (userName.indexOf('\'') != -1 ||
                userName.indexOf('\\') != -1 ||
                userName.indexOf('"') != -1 ||
                userName.indexOf('/') != -1 ||
                userName.indexOf('{') != -1 ||
                userName.indexOf('}') != -1 ||
                userName.indexOf('[') != -1 ||
                userName.indexOf(']') != -1 ||
                userName.indexOf(',') != -1 ||
                userName.indexOf(';') != -1) {
            Log.Error("userName " + userName + " contain invalid charactors");
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
            // generate a MessageDigiest Object which implements specified digest algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Update the digest with specified byte array
            md.update(plainText.getBytes());
            // Complete Hash calculation with executing final operations like fullfilling
            byte b[] = md.digest();
            // generate concrete md5 password to the buf array
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
            //System.out.println("16bit: " + buf.toString().substring(8, 24)); 16bit encryption which is the segment of 32bit encryption
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static UserInfo getUserInfo(int userId)
    {
        UserInfo info = new UserInfo();
        if (userId <= 0) {
            return info;
        }

        String sql = "SELECT * FROM user_table WHERE user_id=?";
        try (Connection conn = getConnection();
            PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, userId);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                info.mUserId = rs.getInt("user_id");
                info.mUserName = rs.getString("user_name");
                info.mCreatedTime = rs.getString("create_time");
                info.mGroup = rs.getInt("user_group");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }

    public static UserInfo getUserInfo(String userName)
    {
        UserInfo info = new UserInfo();
        if (null == userName) {
            return info;
        }

        String sql = "SELECT * FROM user_table WHERE user_name=?";
        try (Connection conn = getConnection();
            PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setString(1, userName);
            ResultSet rs = stat.executeQuery();

            if (rs.next()) {
                info.mUserId = rs.getInt("user_id");
                info.mUserName = rs.getString("user_name");
                info.mCreatedTime = rs.getString("create_time");
                info.mGroup = rs.getInt("user_group");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }

    public UserInfo addUser(String userName, String UserPassWd) {
        UserInfo info = new UserInfo();
        if (null == userName) {
            Log.Info("userName is null");
            info.mUserId = -1;
            return info;
        } else if (true != validUserName(userName)) {
            Log.Info("invalid username " + userName);
            info.mUserId = -2;
            return info;
        } else if (true != validPassWd(UserPassWd)) {
            Log.Info("invalid password " + UserPassWd);
            info.mUserId = -3;
            return info;
        } else {
            Log.Info("valid name and pwd for " + userName);
        }

        Date currentDate = new java.sql.Date(System.currentTimeMillis());

        String salt = generateSalt();
        if (null == salt) {
            Log.Error("failed to generate salt");
            info.mUserId = -4;
            return info;
        }

        String PassWdStor = toMD5(toMD5(UserPassWd) + salt);

        String sql = "INSERT INTO user_table VALUES(null, ?, ?, ?, ?, ?);";
        String dupsql = "SELECT user_name FROM user_table WHERE user_name = ?;";
        String sqlId = "SELECT LAST_INSERT_ID();";

        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);
                PreparedStatement statId = conn.prepareStatement(sqlId);
                // add these two parameter to move pointer freely
                PreparedStatement dupstat = conn.prepareStatement(dupsql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);) {

            dupstat.setString(1, userName);
            ResultSet rs = dupstat.executeQuery();
            rs.last();
            int row = rs.getRow();
            rs.beforeFirst();
            if (0 != row) {
                info.mUserId = -5;
                return info;
            }

            stat.setString(1, userName);
            stat.setString(2, PassWdStor);
            stat.setString(3, currentDate.toString());
            stat.setInt(4, 3);
            stat.setString(5, salt);

            if (stat.executeUpdate() == 0) {
                Log.Error("mysql failed to insert new user info");
                info.mUserId = -6;
                return info;
            }

            ResultSet rsId = statId.executeQuery();
            if (rsId.next()) {
                info.mUserId = rsId.getInt(1);
                info.mUserName = userName;
                info.mGroup = 3;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            info.mUserId = -7;
        }

        return info;
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

        String sql = "SELECT user_id, user_passwd, user_group, passwd_salt FROM user_table WHERE user_name = ? limit 1;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, userName);

            ResultSet rs = stat.executeQuery();

            if (!rs.next()) {
                Log.Error("no such user " + userName);
                return -6;
            }
            int userId = rs.getInt("user_id");
            String passWdStor = rs.getString("user_passwd");
            int group = rs.getInt("user_group");
            String salt = rs.getString("passwd_salt");


            String authPassWd = toMD5(toMD5(userPassWd) + salt);
            if (true == authPassWd.equals(passWdStor)) {
                status = Integer.valueOf(userId);
            }

            if (0 > group) {
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