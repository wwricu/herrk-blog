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
        nickname VARCHAR(1024)
        avatar_link VARCHAR(1024)
        email VARCHAR(1024)
        website VARCHAR(65535)
        body_md TEXT
        created_time DATE
        ip_address VARCHAR(1024)
    */

    public static void init() {
        final String sql =
            new StringBuilder("CREATE TABLE IF NOT EXISTS comment_table (")
                .append("comment_id INT UNSIGNED AUTO_INCREMENT,")
                .append("auther_id INT UNSIGNED,")
                .append("article_id INT UNSIGNED,")
                .append("reply_comment_id INT UNSIGNED,")
                .append("nickname VARCHAR(1024),")
                .append("avatar_link VARCHAR(1024),")
                .append("email VARCHAR(1024),")
                .append("website VARCHAR(65535),")
                .append("body_md TEXT,")
                .append("created_time DATE,")
                .append("ip_address VARCHAR(1024),")
                .append("PRIMARY KEY (comment_id)")
                .append(")ENGINE=InnoDB DEFAULT CHARSET=utf8;").toString();
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

        final String sql = "INSERT INTO comment_table VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        String sqlId = "SELECT LAST_INSERT_ID();";

        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);
                Statement statement = conn.createStatement();) {
            int i = 1;
            stat.setInt(i++, info.mAutherId);
            stat.setInt(i++, info.mArticleId);
            stat.setInt(i++, info.mReplyCommentId);
            stat.setString(i++, info.mNickname);
            stat.setString(i++, info.mAvatarLink);
            stat.setString(i++, info.mEmail);
            stat.setString(i++, info.mWebsite);
            stat.setString(i++, info.mBodyMD);
            stat.setString(i++, info.mCreatedTime);
            stat.setString(i++, info.mIpAddress);

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

    public static int getCommentNum(int articleId) {
        if (articleId < 0) {
            articleId = 0;
        }

        String sql = "SELECT count(*) FROM comment_table WHERE articleId=?, reply_comment_id=0;";
        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);) {
            stat.setInt(1, articleId);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static CommentInfo[] getLatestComments(int start, int num, int articleId,
                                                  int replyCommentId, String order) {
        if (start < 0) {
            start = 0;
        }
        if (num <= 0) {
            num = 1;
        }
        if (articleId < 0) {
            articleId = 0;
        }
        if (order == null ||
                !order.equals("comment_id") &&
                !order.equals("create_time")) {
            order = "created_time";
        }

        StringBuilder sql_builder = new StringBuilder("SELECT comment_id,")
                                    .append("auther_id,").append("article_id,")
                                    .append("reply_comment_id,").append("nickname,")
                                    .append("avatar_link,").append("email,")
                                    .append("website,").append("body_md,")
                                    .append("created_time,").append("ip_address ")
                                    .append("FROM comment_table WHERE article_id=?, reply_comment_id=? ")
                                    .append("ORDER BY ? DESC LIMIT ?, ?;");
        String sql = sql_builder.toString();

        try (Connection conn = getConnection();
             PreparedStatement stat = conn.prepareStatement(sql);) {

            int s = 0;
            stat.setInt(s++, articleId);
            stat.setInt(s++, replyCommentId);
            stat.setString(s++, order);
            stat.setInt(s++, start);
            stat.setInt(s++, num);

            int count = 0;
            ResultSet rs = stat.executeQuery();
            rs.last();
            int line = rs.getRow();
            if (line == 0) {
                Log.Error("get no result");
                return new CommentInfo[0];
            }
            rs.beforeFirst();
            CommentInfo[] result = new CommentInfo[line];

            while (rs.next() && count < num) {
                CommentInfo info = new CommentInfo();
                info.mCommentId = rs.getInt("comment_id");
                info.mAutherId = rs.getInt("auther_id");
                info.mArticleId = rs.getInt("article_id");
                info.mReplyCommentId = rs.getInt("reply_comment_id");
                info.mNickname = rs.getString("nickname");
                info.mAvatarLink = rs.getString("avatar_link");
                info.mEmail = rs.getString("email");
                info.mWebsite = rs.getString("website");
                info.mBodyMD = rs.getString("body_md");
                info.mCreatedTime = rs.getString("created_time");
                info.mIpAddress = rs.getString("ip_address");
                result[count++] = info;
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new CommentInfo[0];
    }

    public static CommentInfo[] searchComment(int id, int articleId, int mode) {
        if (id < 0 || articleId < 0) {
            return new CommentInfo[0];
        }

        StringBuilder base = new StringBuilder("SELECT * FROM comment_table WHERE article_id=? ");

        String mode0 = "comment_id=?;";
        String mode1 = "auther_id=?;";
        String mode2 = "reply_comment_id=?;";

        switch (mode) {
        case 0:
            base.append(mode0);
            break;
        case 1:
            base.append(mode1);
            break;
        case 2:
            base.append(mode2);
            break;
        default:
            base.append(mode0);
        }

        try (Connection conn = getConnection();
                PreparedStatement stat = conn.prepareStatement(base.toString());) {

            stat.setInt(1, articleId);
            stat.setInt(2, id);

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
                    rs.getString("nickname"),
                    rs.getString("avatar_link"),
                    rs.getString("email"),
                    rs.getString("website"),
                    rs.getString("body_md"),
                    rs.getString("created_time"),
                    rs.getString("ip_address")
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
