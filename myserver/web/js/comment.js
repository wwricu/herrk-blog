let mainCommentNum = 0;
let pageNum = 0;
let currentPage = 1;
let commentPerPage = 5;
let localGuestName = "";
let localGuestLink = "";

function addCommentsRec(jSelector, subComments) {
    for (let i = 0; i < subComments.length; i++) {
        let dom = "<div id=card-" + subComments[i].commentId + " class=comment-card> \
                       <span class=auther>" + unescape(subComments[i].nickname) + "</span><span class=created-time>" +
                           subComments[i].createdTime + "</span><span class=reply-btn>reply</span> \
                       <div class=website>" + unescape(subComments[i].website) + "</div> \
                       <div class=comment-body>" + unescape(subComments[i].body) + "</div> \
                       <hr> \
                       <div id=area-" + subComments[i].commentId + " class='reply-area'> \
                           <input class=reply-input placeholder='nickname' value=" + localGuestName + "> \
                           <input class=reply-input placeholder='your link' value=" + localGuestLink + "> \
                           <textarea class=reply-text>Reply @" + subComments[i].nickname + ":</textarea> \
                           <div style='height: 2rem'> \
                           <span id=reply-submit class='sub-btn'>submit</span><span id=reply-cancel class='sub-btn'>cancel</span> \
                           <div> \
                       </div> \
                   </div>";
        jSelector.append(dom);
        let thisSelector = $("#card-" + subComments[i].commentId);
        let comment = subComments[i];
        let area = $("#area-" + subComments[i].commentId);
        thisSelector.children(".reply-btn").eq(0).click(function() {
            area.show();
        }); // reply
        thisSelector.find(".sub-btn").eq(0).click(function() {
            let guest = {
                guestName: area.children().eq(0).val(),
                guestLink: area.children().eq(1).val()
            };
            if (guest.guestName == null || guest.guestName.length == 0) {
                guest.guestName = "default user";
            }
            localStorage.setItem("guestInfo", JSON.stringify(guest));

            let replyData = {
                subComments: [{
                    "commentId": 0,
                    "autherId": 0,
                    "articleId": 0,
                    "replyCommentId": comment.commentId,
                    "nickname": escape(guest.guestName),
                    "avatarLink": "",
                    "email": "",
                    "website": escape(guest.guestLink),
                    "body": escape(area.children().eq(2).val()),
                    "createdTime": "",
                    "subComments": []
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
                    nickName: escape(guest.guestName),
                    avatarLink: "",
                    email: "",
                    website: escape(guest.guestLink),
                    body: escape(area.children().eq(2).val())
                },
                dataType: 'json',
                async: 'true',
                success: function(result) {
                    if (result == "failure") {
                        return;
                    }
                    replyData.subComments[0].commentId = result.commentId;
                    area.hide();
                    // alert(JSON.stringify(replyData));
                    addCommentsRec(thisSelector, replyData.subComments);
                    // loadComment();
                }
            });
        }); // submit
        thisSelector.find(".sub-btn").eq(1).click(function() {
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
    $(".page-btn").css("color", "whitesmoke")
                  .css("background", "#03a9f4");
    $.ajax({
        type: 'POST',
        url: 'commentmanager',
        data: {
            action: 'getlatest',
            articleId: 0,
            index: (currentPage - 1) * commentPerPage,
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
            $(".comment-card").remove();
            addCommentsRec($("#thin-frame"), result.subComments);

            let currentBtn = 1;
            if (currentPage <= 4) {
                currentBtn = currentPage - 1;
            } else if (pageNum - currentPage <= 4) {
                currentBtn = 4 - (pageNum - currentPage);
            } else {
                currentBtn = 3;
            }

            $("#page-btn-frame").children(".page-btn").eq(currentBtn)
                .css("color", "black")
                .css("background", "#00CCCC");
        }
    });
    configBtns();
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
    $("#prev-page").unbind("click");
    $("#next-page").unbind("click");

    $("#prev-page").click(function() {
        if (currentPage == 1) {
            return;
        }
        currentPage--;
        loadComment();
    });
    $("#next-page").click(function() {
        if (currentPage == pageNum) {
            return;
        }
        currentPage++;
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
            /* use .css("display", "inline-block") instead of .show()
                to set specified width and height of this.
             */
            if (currentPage > 4) {
                dot1.css("display", "inline-block");
            }
            if (currentPage < pageNum - 3 ||
                    (currentPage < 5 && pageNum > 5)) {
                dot2.css("display", "inline-block");
            }
        case 5:
            btn5.css("display", "inline-block");
            btn5.unbind("click");
            btn5.html(pageNum);
            btn5.click(function() {
                currentPage = pageNum;
                loadComment();
            });
        case 4:
            btn4.css("display", "inline-block");
            btn4.unbind("click");
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
            btn3.css("display", "inline-block");
            btn3.unbind("click");
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
            btn2.css("display", "inline-block");
            btn2.unbind("click");
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
            btn1.css("display", "inline-block");
            btn1.unbind("click");
            btn1.html("1");
            btn1.click(function() {
                currentPage = 1;
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
        let website = $("#website").val();
        let body = $("#editor-panel").children("textarea").val();
        if (nickname == null || nickname.length == 0) {
            nickname = "default user";
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
                website: escape(website),
                body: escape(body)
            },
            dataType: 'text',
            async: 'true',
            success: function(result) {
                if (result == "failure") {
                    return;
                }
                mainCommentNum++;
                pageNum = Math.floor(mainCommentNum / 5) + 1;
                // configBtns();
                loadComment();
                $("#editor-panel").children("textarea").val("");
                alert("commented successfully");
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
            pageNum = Math.floor(mainCommentNum / 5) + 1;
            if (mainCommentNum % 5 == 0) {
                pageNum--;
            }
            configPost();
            // configBtns();
            loadComment();
        }
    });
}

$(function() {
    let guestInfo =localStorage.getItem("guestInfo");
    if (guestInfo != null) {
        localGuestName =  JSON.parse(guestInfo).guestName;
        localGuestLink =  JSON.parse(guestInfo).guestLink;
    }
    $("#nickname").val(localGuestName);
    $("#website").val(localGuestLink);
    configPages();
});