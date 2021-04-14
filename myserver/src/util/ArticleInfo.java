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

    public void transmitQuota() {
        mTitle = mTitle.replace("\"", "\\\"");
        mSummary = mSummary.replace("\"", "\\\"");
        mTags = mTags.replace("\"", "\\\"");
        mBodyMD = mBodyMD.replace("\"", "\\\"");
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"article_id\":").append(String.valueOf(mArticleId)).append(",");
        json.append("\"auther_id\":").append(String.valueOf(mAutherId)).append(",");
        json.append("\"title\":\"").append(mTitle).append("\",");
        json.append("\"summary\":\"").append(mSummary).append("\",");
        json.append("\"tags\":\"").append(mTags).append("\",");
        json.append("\"bodyMD\":\"").append(mBodyMD).append("\",");
        json.append("\"create_time\":\"").append(mCreateTime).append("\",");
        json.append("\"last_modify_time\":\"").append(mLastModifyTime).append("\",");
        json.append("\"permission\":").append(String.valueOf(mPermission)).append("}");

        return json.toString();
    }
}
