package dao;

import util.ClassInfo;
import util.ArticleInfo;
import util.Log;

import java.io.File;
import java.io.IOException;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class ClassManagerDAO {

    public ClassManagerDAO() {
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
    class_table:
    class_id INT UNSIGNED AUTO_INCREMENT
    class_name VARCHAR(1024)
    father_id INT UNSIGNED
    class_group INT
    */

    public static void init() {
        final String sql = "CREATE TABLE IF NOT EXISTS class_table (class_id INT UNSIGNED AUTO_INCREMENT, class_name VARCHAR(1024), father_id INT UNSIGNED, class_group INT, PRIMARY KEY (class_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
             stat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int createClass(ClassInfo info) {

        if (!validClass(info)) {
            return -1;
        }

        int insNums = 0;
        int classId = 0;
        String sql = "INSERT INTO class_table VALUES(null, ?, ?, ?);";
        String sqlId = "SELECT LAST_INSERT_ID();";
        NumberCountDAO numberDAO = new NumberCountDAO();

        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);
             Statement statement = conn.createStatement();) {

            stat.setString(1, info.mClassName);
            stat.setInt(2, info.mFatherId);
            stat.setInt(3, info.mGroup);

            stat.executeUpdate();

            ResultSet rs = statement.executeQuery(sqlId);
            if (rs.next()) {
                classId = rs.getInt(1);
                if (classId <= 0) {
                    return -2;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classId;
    }

    public int deleteClass(int classId) {
        if (classId < 0) {
            return -1;
        }

        int delNums = 0;
        // NumberCountDAO numberDAO = new NumberCountDAO();
        String sql = "DELETE FROM article_table WHERE class_id=?;";

        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, classId);
            /* delNums = stat.executeUpdate();
            if (delNums != 0) {
                numberDAO.articleCountDecrement(delNums);
            } */
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ClassInfo searchClass(int classId) {
        ClassInfo info = new ClassInfo();
        if (classId <= 0) {
            return info;
        }

        String sql = "SELECT * FROM class_table WHERE class_id=?;";
        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, classId);

            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                info.setValue(rs.getInt("class_id"),
                              rs.getString("class_name"),
                              rs.getInt("father_id"),
                              rs.getInt("class_group"));
            } else {
                Log.Error("result set is null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }

    public int updateClass(ClassInfo info) {
        if (info == null || info.mClassId <= 0) {
            return -1;
        }

        String sql = "UPDATE class_table WHERE class_id=? SET class_name=?, father_id=?, class_group=?;";
        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, info.mClassId);
            stat.setString(2, info.mClassName);
            stat.setInt(3, info.mFatherId);
            stat.setInt(4, info.mGroup);

            if (stat.executeUpdate() == 0) {
                Log.Error("nothing updated!");
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ClassInfo[] allTopClasses() {
        String sql = "SELECT * FROM class_table WHERE father_id=0;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);) {
            ResultSet rs = stat.executeQuery();
            rs.last();
            int line = rs.getRow();
            if (line == 0) {
                Log.Error("get no result");
                return new ClassInfo[0];
            }
            rs.beforeFirst();
            ClassInfo[] res = new ClassInfo[line];
            for (int i = 0; i < line; i++) {
                if (rs.next()) {
                    res[i] = new ClassInfo(
                        rs.getInt("class_id"),
                        rs.getString("class_name"),
                        rs.getInt("father_id"),
                        rs.getInt("class_group")
                    );
                }
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ClassInfo[0];
    }

    public ClassInfo[] subClasses(int classId) {
        if (classId <= 0) {
            Log.Error("invalid info");
            return null;
        }

        final String sql = "SELECT * FROM class_table WHERE father_id=?;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, classId);

            ResultSet rs = stat.executeQuery();
            rs.last();
            int line = rs.getRow();
            if (line == 0) {
                Log.Error("get no result");
                return null;
            }
            rs.beforeFirst();
            ClassInfo[] res = new ClassInfo[line];
            for (int i = 0; i < line; i++) {
                rs.next();
                res[i].mClassId = rs.getInt("class_id");
                res[i].mClassName = rs.getString("class_name");
                res[i].mFatherId = rs.getInt("father_id");
                res[i].mGroup = rs.getInt("class_group");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArticleInfo[] subArticles(ClassInfo info) {
        if (info == null || info.mClassId <= 0
                    || info.mGroup != 0) {
            return null;
        }

        String sql = "SELECT * FROM article_table WHERE class_id=?;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, info.mClassId);

            ResultSet rs = stat.executeQuery();
            rs.last();
            int line = rs.getRow();
            if (line == 0) {
                Log.Error("get no result");
                return null;
            }
            ArticleInfo[] res = new ArticleInfo[line];
            rs.beforeFirst();
            for (int i = 0; i < line; i++) {
                rs.next();
                res[i].mArticleId = rs.getInt("article_id");
                res[i].mAutherId = rs.getInt("auther_id");
                res[i].mClassId = rs.getInt("class_id");
                res[i].mTitle = rs.getString("title");
                res[i].mSummary = rs.getString("summary");
                res[i].mTags = rs.getString("tags");
                res[i].mBodyMD = rs.getString("body_md");
                res[i].mCreateTime = rs.getString("create_time");
                res[i].mLastModifyTime = rs.getString("last_modify_time");
                res[i].mPermission = rs.getInt("permission");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean validClass(ClassInfo info) {
        return true;
    }
}