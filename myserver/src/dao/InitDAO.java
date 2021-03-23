package dao;

public class InitDAO {
    public static void init() {
        NumberCountDAO.init();
        UserManagerDAO.init();
        ArticleManagerDAO.init();
    }
}
