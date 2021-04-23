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
        mGroup = 3;
    }
    public int mUserId;
    public String mUserName;
    public String mCreatedTime;
    /*
    -1: NUKED users
    0: Super user
    1: Premiere users who can do everything except managing users
    2. High-lever users who can publish articles
    3. normal users who can only publish comments to an articles, default
    */
    public int mGroup;
}