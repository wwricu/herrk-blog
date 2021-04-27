package util;

public class CommentInfo {
    public int mCommentId;
    public int mAutherId;
    public int mArticleId;
    public int mReplyCommentId;
    public String mNickname;
    public String mAvatarLink;
    public String mEmail;
    public String mWebsite;
    public String mBodyMD;
    public String mCreatedTime;
    public String mIpAddress;

    public void setValue(int commentId, int autherId, int articleId, int replyCommentId,
                    String nickname, String avatarLink, String email, String website,
                    String bodyMD, String createdTime, String ipAddress) {
        mCommentId = commentId;
        mAutherId = autherId;
        mArticleId = articleId;
        mReplyCommentId = replyCommentId;
        mNickname = nickname;
        mAvatarLink = avatarLink;
        mEmail = email;
        mWebsite = website;
        mBodyMD = bodyMD;
        mCreatedTime = createdTime;
        mIpAddress = ipAddress;
    }

    public String toJson() {
        return new StringBuilder("{")
            .append("\"commentId\":").append(mCommentId)
            .append(",\"autherId\":").append(mAutherId)
            .append(",\"articleId\":").append(mArticleId)
            .append(",\"replyCommentId\":").append(mReplyCommentId)
            .append(",\"nickname\":\"").append(mNickname)
            .append("\",\"avatarLink\":\"").append(mAvatarLink)
            .append(",\"email\":\"").append(mEmail)
            .append(",\"website\":\"").append(mWebsite)
            .append("\",\"bodyMD\":\"").append(mBodyMD)
            .append("\",\"createdTime\":\"").append(mCreatedTime)
            .append("\",\"ipAddress\":\"").append(mIpAddress)
            .append("\"}").toString();
    }
}
