package util;

public class CommentInfo {
    public int mCommentId;
    public int mAutherId;
    public int mArticleId;
    public int mReplyCommentId;
    public String mBodyMD;
    public String mCreatedTime;

    public void setValue(int commentId, int autherId,
                    int articleId, int replyCommentId,
                    String bodyMD, String createdTime) {
        mCommentId = commentId;
        mAutherId = autherId;
        mArticleId = articleId;
        mReplyCommentId = replyCommentId;
        mBodyMD = bodyMD;
        mCreatedTime = createdTime;
    }

    public String toJson() {
        return new StringBuilder("{")
            .append("\"commentId\":").append(mCommentId)
            .append(",\"autherId\":").append(mAutherId)
            .append(",\"articleId\":").append(mArticleId)
            .append(",\"replyCommentId\":").append(mReplyCommentId)
            .append(",\"bodyMD\":\"").append(mBodyMD)
            .append("\",\"createdTime\":\"").append(mCreatedTime)
            .append("\"}").toString();
    }
}
