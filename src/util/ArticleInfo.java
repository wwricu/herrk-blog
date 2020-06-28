package util;

public class ArticleInfo {
    public int mArticleId;
    public int mAutherId;
    public String mTitle;
    public String mSummary;
    public String mTags;
    public String mCreateTime;
    public String mLastModifyTime;
    public int mPermission;

    public void setValue(int articleId, int autherId, String title,
                String summary, String tags, String createTime,
                String lastModifyTime, int permission) {
        mArticleId = articleId;
        mAutherId = autherId;
        mTitle = title;
        mSummary = summary;
        mTags = tags;
        mCreateTime = createTime;
        mLastModifyTime = lastModifyTime;
        mPermission = permission;

        return;
    }
}
