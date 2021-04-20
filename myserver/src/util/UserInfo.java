package util;

public class UserInfo {
    public UserInfo() {
        mUserId = 0;
        mUserName = null;
        mGroup = 0;
    }
    public UserInfo(int userId, String userName) {
        mUserId = userId;
        mUserName = userName;
    }
    public int mUserId;
    public String mUserName;
    public int mGroup;
}