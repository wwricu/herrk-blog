let mainCommentNum = 0;
let pageNum = 0;
let currentPage = 0;
let commentPerPage = 5;

function addCommentsRec(jSelector, subComments) {
    for (let i = 0; i < subComments.length; i++) {
        let dom = "<div id=card-" + subComments[i].commentId + " class=comment-card> \
                       <span class=auther>" + unescape(subComments[i].nickname) + "</span><span class=created-time>" +
                           subComments[i].createdTime + "</span><span class=reply-btn>reply</span> \
                       <div class=website>" + unescape(subComments[i].website) + "</div> \
                       <div class=comment-body>" + unescape(subComments[i].body) + "</div> \
                       <hr> \
                       <div id=area-" + subComments[i].commentId + " class='reply-area'> \
                           <input class=reply-input placeholder='nickname'><input class=reply-input placeholder='your link'> \
                           <textarea class=reply-text placeholder='say something~'></textarea> \
                           <span class='reply-btn'>submit</span><span class='reply-btn'>cancel</span> \
                       </div> \
                   </div>";
        jSelector.append(dom);
        let thisSelector = $("#card-" + subComments[i].commentId);
        let comment = subComments[i];
        let area = $("#area-" + subComments[i].commentId);
        thisSelector.children(".reply-btn").eq(0).click(function() {
            area.show();
        }); // reply
        thisSelector.children(".reply-area").children(".reply-btn").eq(0).click(function() {
            let recData = {
                subComments: [{
                    "commentId": 0,
                    "autherId": 0,
                    "articleId": 0,
                    "replyCommentId": comment.commentId,
                    "nickname": area.children().eq(0).val(),
                    "avatarLink": "",
                    "email": "",
                    "website": area.children().eq(1).val(),
                    "body": area.children().eq(2).val(),
                    "createdTime": "",
                    "subComments": [
                    ]
                }]
            };
            $.ajax({
                type: 'POST',
                url: 'commentmanager',
                data: {
                    action: 'post',
                    autherId: 0,
                    articleId: 0,
                    replyCommentId: comment.commentId,
                    nickName: escape(area.children().eq(0).val()),
                    avatarLink: "",
                    email: "",
                    website: escape(area.children().eq(1).val()),
                    body: escape(area.children().eq(2).val())
                },
                dataType: 'json',
                async: 'true',
                success: function(result) {
                    if (result == "failure") {
                        return;
                    }
                    data.subComments[0].commentId = result;
                    addCommentsRec(thisSelector, recData);
                }
            });
        }); // submit
        thisSelector.children(".reply-area").children(".reply-btn").eq(1).click(function() {
            area.hide();
        }); // cancel

        addCommentsRec(thisSelector, subComments[i].subComments);
    }
}
/*?action=allcomments
{
    subComments: [{
        "commentId": 1,
        "autherId": 11,
        "articleId": 111,
        "replyCommentId": 1111,
        "nickname": "xxx",
        "avatarLink": "xxx",
        "email": "xxx",
        "website": "xxx",
        "body": "xxx",
        "createdTime": "xxx",
        "subComments": [
            {...}, {...}
        ]
    }, {
    }]
}*/
function loadComment() {
    $.ajax({
        type: 'POST',
        url: 'commentmanager',
        data: {
            action: 'getlatest',
            articleId: 0,
            index: currentPage * commentPerPage,
            num: commentPerPage
        },
        dataType: 'json',
        async: 'true',
        error : function() {
            console.log('no token ajax fail');
        },
        success: function(result) {
            if (result == "failure") {
                return;
            }
            addCommentsRec($("#thin-frame"), result.subComments);
        }
    });
}

/*
 * 1. pageNum <= 5:
 * Show btns as many as pages
 * 2. pageNum > 5 && currentPage <= 4:
 * show btn1 btn2 btn3 btn4 dot2 btn5
 *      1     2     3    4  ... final
 * 3. pageNum > 5 && PageNum - currentPage <= 4:
 * show btn1 dot1 btn2 btn3 btn4 btn5
 *        1   ...  f-3  f-2  f-1  f
 * 4. otherwise
 * show btn1 dot1 btn2 btn3 btn4 dot2 btn5
 *       1    ...  c-1  c   c+1   ...  f
 */
function configBtns() {
    $("#prev-page").click(function() {
        if (currentPage == 0) {
            return;
        }
        currentPage++;
        loadComment();
    });
    $("#next-page").click(function() {
        if (currentPage == pageNum - 1) {
            return;
        }
        currentPage--;
        loadComment();
    });

    let btn1 = $("#page-btn-frame").children(".page-btn").eq(0);
    let btn2 = $("#page-btn-frame").children(".page-btn").eq(1);
    let btn3 = $("#page-btn-frame").children(".page-btn").eq(2);
    let btn4 = $("#page-btn-frame").children(".page-btn").eq(3);
    let btn5 = $("#page-btn-frame").children(".page-btn").eq(4);

    let dot1 = $("#page-btn-frame").children(".dot-btn").eq(0);
    let dot2 = $("#page-btn-frame").children(".dot-btn").eq(1);

    let decider = pageNum;
    if (decider > 6) {
        decider = 6;
    }

    switch (decider) {
        case 6:
            if (currentPage > 4) {
                dot1.show();
            }
            if (currentPage < pageNum - 3) {
                dot2.show();
            }
        case 5:
            btn5.show();
            btn5.html(pageNum);
            btn5.click(function() {
                currentPage = pageNum;
                loadComment();
            });
        case 4:
            btn4.show();
            if (currentPage <= 4) {
                btn4.html("4");
                dot1.hide();
                btn4.click(function() {
                    currentPage = 4;
                    loadComment();
                });
            } else if (pageNum - currentPage <= 4) {
                btn4.html(pageNum - 1);
                dot2.hide();
                btn4.click(function() {
                    currentPage = pageNum - 1;
                    loadComment();
                });
            } else {
                btn4.html(currentPage + 1);
                btn4.click(function() {
                    currentPage++;
                    loadComment();
                });
            }
        case 3:
            btn3.show();
            if (currentPage <= 4) {
                btn3.html("3");
                btn3.click(function() {
                    currentPage = 3;
                    loadComment();
                });
            } else if (pageNum - currentPage <= 4) {
                btn3.html(pageNum - 2);
                btn3.click(function() {
                    currentPage = pageNum - 2;
                    loadComment();
                });
            } else {
                btn3.html(currentPage);
                // current page itself, nothing will happen
            }
        case 2:
            btn2.show();
            if (currentPage <= 4) {
                btn2.html("2");
                btn2.click(function() {
                    currentPage = 2;
                    loadComment();
                });
            } else if (pageNum - currentPage <= 4) {
                btn2.html(pageNum - 3);
                btn2.click(function() {
                    currentPage = pageNum - 3;
                    loadComment();
                });
            } else {
                btn2.html(currentPage - 1);
                btn2.click(function() {
                    currentPage--;
                    loadComment();
                });
            }
        case 1:
            btn1.show();
            btn1.html("1");
            btn1.click(function() {
                currentPage = 0;
                loadComment();
            });
        default:
            console.log("no comment");
    }
}

function configPost() {
    // ?action=post&autherId=0&articleId=0&replyCommentId=0&nickName=ww&avatarLink=ww&email=ww&website=ww&body=ww
    $("#post-comment").click(function() {
        let nickname = $("#nickname").val();
        let website = $("#editor-panel").children("textarea").val();
        if (nickname == null || nickname.length == 0) {
            nickname = anonymous_user;
        }
        $.ajax({
            type: 'POST',
            url: 'commentmanager',
            data: {
                action: 'post',
                autherId: 0,
                articleId: 0,
                replyCommentId: 0,
                nickName: escape(nickname),
                avatarLink: "",
                email: "",
                website: "",
                body: escape(website)
            },
            dataType: 'text',
            async: 'true',
            success: function(result) {
                alert(result);
                if (result == "failure") {
                    return;
                }
                mainCommentNum++;
                pageNum = mainCommentNum / 5 + 1;
                configBtns();
                loadComment();
            }
        });
    });
}

function configPages() {
    $.ajax({
        type: 'POST',
        url: 'commentmanager',
        data: {
            action: 'maincommentnum',
            articleId: 0,
        },
        dataType: 'text',
        async: 'false',
        success: function(result) {
            if (result == "failure") {
                return;
            }
            mainCommentNum = result;
            pageNum = result / 5 + 1;
            configPost();
            configBtns();
            loadComment();
        }
    });
}

$(function() {
    configPages();
});