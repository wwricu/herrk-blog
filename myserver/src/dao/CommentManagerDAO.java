package dao;

import util.CommentInfo;
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

public class CommentManagerDAO {
    public CommentManagerDAO() {
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

    /* comment_table:

        comment_id INT UNSIGNED AUTO_INCREMENT
        auther_id INT UNSIGNED
        article_id INT UNSIGNED
        reply_comment_id INT UNSIGNED
        body_md TEXT
        created_time DATE
    */

    public static void init() {
        final String sql = "CREATE TABLE IF NOT EXISTS comment_table (comment_id INT UNSIGNED AUTO_INCREMENT, auther_id INT UNSIGNED, article_id INT UNSIGNED, reply_comment_id INT UNSIGNED, body_md TEXT, created_time DATE, PRIMARY KEY (comment_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {
             stat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int createComment(CommentInfo info) {
        if (!validComment(info)) {
            return -1;
        }

        final String sql = "INSERT INTO comment_table VALUES(null, ?, ?, ?, ?, ?);";
        String sqlId = "SELECT LAST_INSERT_ID();";

        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);
                Statement statement = conn.createStatement();) {
            stat.setInt(1, info.mAutherId);
            stat.setInt(2, info.mArticleId);
            stat.setInt(3, info.mReplyCommentId);
            stat.setString(4, info.mBodyMD);
            stat.setString(5, info.mCreatedTime);

            int line = stat.executeUpdate();
            if (line == 0) {
                return -2;
            }
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -3;
    }

    public static CommentInfo[] searchComment(CommentInfo info, int mode) {
        if (!validComment(info)) {
            return new CommentInfo[0];
        }

        StringBuilder base = new StringBuilder("SELECT * FROM comment_table WHERE ");
        String mode0 = "comment_id=?;";
        String mode1 = "auther_id=?;";
        String mode2 = "article_id=?;";
        String mode3 = "reply_comment_id=?;";
        int selector = -1;

        switch (mode) {
        case 0:
            base.append(mode0);
            selector = info.mCommentId;
            break;
        case 1:
            base.append(mode1);
            selector = info.mAutherId;
            break;
        case 2:
            base.append(mode2);
            selector = info.mArticleId;
            break;
        case 3:
            base.append(mode3);
            selector = info.mReplyCommentId;
            break;
        default:
            base.append(mode0);
            selector = info.mCommentId;
        }

        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(base.toString());) {

            stat.setInt(1, selector);

            ResultSet rs = stat.executeQuery();
            rs.last();
            int line = rs.getRow();
            rs.beforeFirst();

            CommentInfo[] res = new CommentInfo[line];
            for (int i = 0; i < line; i++) {
                rs.next();
                res[i] = new CommentInfo();
                res[i].setValue(
                    rs.getInt("comment_id"),
                    rs.getInt("auther_id"),
                    rs.getInt("article_id"),
                    rs.getInt("reply_comment_id"),
                    rs.getString("body_md"),
                    rs.getString("created_time")
                );
            }

            return res;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new CommentInfo[0];
    }

    public static int updateComment(CommentInfo info) {
        if (!validComment(info)) {
            return -1;
        }

        final String sql = "UPDATE comment_table WHERE comment_id=? SET auther_id=?, article_id=?, reply_comment_id=?;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, info.mCommentId);
            stat.setInt(2, info.mAutherId);
            stat.setInt(3, info.mArticleId);
            stat.setInt(4, info.mReplyCommentId);

            return(stat.executeUpdate());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -2;
    }

    public static int deleteComment(CommentInfo info, int mode) {
        if (!validComment(info)) {
            return -1;
        }

        StringBuilder base = new StringBuilder("DELETE FROM comment_table WHERE ");
        String mode0 = "comment_id=?;";
        String mode1 = "auther_id=?;";
        String mode2 = "article_id=?;";
        String mode3 = "reply_comment_id=?;";
        int selector = -1;

        switch (mode) {
        case 0:
            base.append(mode0);
            selector = info.mCommentId;
            break;
        case 1:
            base.append(mode1);
            selector = info.mAutherId;
            break;
        case 2:
            base.append(mode2);
            selector = info.mArticleId;
            break;
        case 3:
            base.append(mode3);
            selector = info.mReplyCommentId;
            break;
        default:
            base.append(mode0);
            selector = info.mCommentId;
        }

        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(base.toString());) {

            stat.setInt(1, selector);

            return stat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -2;
    }

    public static boolean validComment(CommentInfo info) {
        return true;
    }
}
