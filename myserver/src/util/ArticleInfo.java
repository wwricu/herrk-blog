package util;

public class ArticleInfo {
    public int mArticleId;
    public int mAutherId;
    public String mTitle;
    public String mSummary;
    public String mTags;
    public String mBodyMD;
    public String mCreateTime;
    public String mLastModifyTime;
    public int mPermission;

    public void setValue(int articleId, int autherId, String title,
                String summary, String tags, String bodyMD,
                String createTime, String lastModifyTime, int permission) {
        mArticleId = articleId;
        mAutherId = autherId;
        mTitle = title;
        mSummary = summary;
        mTags = tags;
        mBodyMD = bodyMD;
        mCreateTime = createTime;
        mLastModifyTime = lastModifyTime;
        mPermission = permission;
    }
}
