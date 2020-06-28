package dao;

import util.ArticleInfo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class ArticleManagerDAO {

    public ArticleManagerDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOGDB", "sql_admin", "153226");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     table:
     ArticleId, auther_id, title, summary, tags, CreateTime, LastModifyTime,
     */
    public static void init() {

        String sql = "CREATE TABLE IF NOT EXISTS article_table (article_id INT UNSIGNED AUTO_INCREMENT, auther_id VARCHAR(100) NOT NULL, title VARCHAR(100), summary VARCHAR(100), tags VARCHAR(100), create_time DATE, last_modify_time DATE, permission INT, PRIMARY KEY (article_id ))ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
             stat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;
    }

    public ArticleInfo[] getLatestArticles(int start, int num) {

        if (start < 0) {
            start = 0;
        }
        if (num <= 0) {
            num = 1;
        }

        ArticleInfo[] result = new ArticleInfo[num];
        String sql = "SELECT * FROM article_table ORDER BY article_id DESC LIMIT ?, ?";
        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, start);
            stat.setInt(2, num);

            int count = 0;
            ResultSet rs = stat.executeQuery();
            while (rs.next() && count < num) {
                ArticleInfo info = new ArticleInfo();
                info.mArticleId = rs.getInt("article_id");
                info.mAutherId = rs.getInt("auther_id");
                info.mTitle = rs.getString("title");
                info.mSummary = rs.getString("summary");
                info.mTags = rs.getString("tags");
                info.mCreateTime = rs.getString("create_time");
                info.mLastModifyTime = rs.getString("last_modify_time");
                info.mPermission = rs.getInt("permission");
                result[count++] = info;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    public int createArticle(ArticleInfo info) {

        if (!validArticle(info)) {
            return -1;
        }

        String sql = "INSERT INTO article_table values(null, ?, ?, ?, ?, ?, ?, ?);";
        String sqlId = "SELECT LAST_INSERT_ID();";

        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);
             Statement statId = conn.createStatement();) {
            stat.setInt(1, info.mAutherId);
            stat.setString(2, info.mTitle);
            stat.setString(3, info.mSummary);
            stat.setString(4, info.mTags);
            stat.setString(5, info.mCreateTime);
            stat.setString(6, info.mLastModifyTime);
            stat.setInt(7, info.mPermission);

            stat.executeUpdate();

            ResultSet rs = statId.executeQuery(sqlId);
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -2;
    }

    public void deleteArticle(int articleId) {
        if (0 > articleId) {
            return;
        }
        String sql = "DELETE * FROM article_table WHERE article_id=?";
        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, articleId);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return;
    }

    protected boolean validArticle(ArticleInfo info) {
        if (0 > info.mArticleId || 0 > info.mAutherId) {
            return false;
        }

        if (null == info.mTitle || 30 < info.mTitle.length()) {
            return false;
        }

        if (null != info.mSummary) {
            if (140 < info.mSummary.length()) {
                return false;
            }
        }

        return true;
    }
};
