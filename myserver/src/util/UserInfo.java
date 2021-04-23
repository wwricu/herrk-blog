package util;

public class UserInfo {
    public UserInfo() {
        mUserId = 0;
        mUserName = null;
        mGroup = -1;
    }
    public UserInfo(int userId, String userName) {
        mUserId = userId;
        mUserName = userName;
        mGroup = -1;
    }
    public int mUserId;
    public String mUserName;
    public String mCreatedTime;
    /*
    -1: invalid users
    0: normal users who can only publish comments to an articles, default
    1: High-lever users who can publish articles
    2: Premiere users who can do everything except managing users
    3: super user
    */
    public int mGroup;
}