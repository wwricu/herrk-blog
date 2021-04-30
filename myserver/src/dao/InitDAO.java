package dao;

public class InitDAO {
    public final static String DBURL  = "jdbc:mysql://127.0.0.1:3306/BLOGDB?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    public final static String DBUSER = "sql_admin";
    public final static String DBCODE = "153226";

    public static void init() {
        NumberCountDAO.init();
        UserManagerDAO.init();
        ArticleManagerDAO.init();
        ClassManagerDAO.init();
        CommentManagerDAO.init();
    }
}
