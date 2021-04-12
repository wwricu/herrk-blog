package util;

public class UserInfo {
    public UserInfo() {
        mUserId = 0;
        mUserName = null;
    }
    public UserInfo(int userId, String userName) {
        mUserId = userId;
        mUserName = userName;
    }
    public int mUserId;
    public String mUserName;
}