package util;

public class ArticleInfo {
    public int mArticleId;
    public int mAutherId;
    public int mClassId;
    public String mTitle;
    public String mCoverLink;
    public String mSummary;
    public String mTags;
    public String mBodyMD;
    public String mCreateTime;
    public String mLastModifyTime;
    public int mPermission;

    public void setValue(int articleId, int autherId, int classId, String title,
                String coverLink, String summary, String tags, String bodyMD,
                String createTime, String lastModifyTime, int permission) {
        mArticleId = articleId;
        mAutherId = autherId;
        mClassId = classId;
        mTitle = title;
        mCoverLink = coverLink;
        mSummary = summary;
        mTags = tags;
        mBodyMD = bodyMD;
        mCreateTime = createTime;
        mLastModifyTime = lastModifyTime;
        mPermission = permission;
    }

    public String toJson() {
        return new StringBuilder("{")
            .append("\"article_id\":").append(String.valueOf(mArticleId))
            .append(",\"auther_id\":").append(String.valueOf(mAutherId))
            .append(",\"class_id\":").append(String.valueOf(mClassId))
            .append(",\"title\":\"").append(mTitle)
            .append(",\"coverLink\":\"").append(mCoverLink)
            .append("\",\"summary\":\"").append(mSummary)
            .append("\",\"tags\":\"").append(mTags)
            .append("\",\"bodyMD\":\"").append(mBodyMD)
            .append("\",\"create_time\":\"").append(mCreateTime)
            .append("\",\"last_modify_time\":\"").append(mLastModifyTime)
            .append("\",\"permission\":").append(String.valueOf(mPermission))
            .append("}").toString();
    }

    public String toJson(String autherName) {
        return new StringBuilder("{")
                .append("\"article_id\":").append(String.valueOf(mArticleId))
                .append(",\"auther_id\":").append(String.valueOf(mAutherId))
                .append(",\"class_id\":").append(String.valueOf(mClassId))
                .append(",\"auther_name\":\"").append(autherName)
                .append("\",\"title\":\"").append(mTitle)
                .append(",\"coverLink\":\"").append(mCoverLink)
                .append("\",\"summary\":\"").append(mSummary)
                .append("\",\"tags\":\"").append(mTags)
                .append("\",\"bodyMD\":\"").append(mBodyMD)
                .append("\",\"create_time\":\"").append(mCreateTime)
                .append("\",\"last_modify_time\":\"").append(mLastModifyTime)
                .append("\",\"permission\":").append(String.valueOf(mPermission))
                .append("}").toString();
    }
}
