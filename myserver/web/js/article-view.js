function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i=0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if(pair[0] == variable) {return pair[1];}
    }
    return (false);
}

function renderArticle(articleId) {
    "use strict";
    if (articleId <= 0) {
        return;
    }
    let article = {
        "article_id": 0,
        "auther_id": 0,
        "title": "",
        "summary": "",
        "tags": "",
        "bodyMD": "",
        "create_time": "",
        "last_modify_time": "",
        "permission": 0
    };

    $.ajax({
        type: "POST",
        async: false,
        url: "articleviewer",
        data: {
            "action": "view",
            "articleId": articleId,
        },
        dataType: "json",
        timeout: 1000,
        success: function (receive) {
            article.article_id = receive.article_id;
            article.auther_id = receive.auther_id;
            article.title = unescape(receive.title);
            article.summary = unescape(receive.summary);
            article.tags = receive.tags;
            article.bodyMD = unescape(receive.bodyMD);
            article.create_time = receive.create_time;
            article.last_modify_time = receive.last_modify_time;
            article.permission = receive.permission;
        }
    });

    getAutherName(article);
    $("#title").text(article.title);
    $("#time").text("Created at " + article.create_time
        + " Last modified at " + article.last_modify_time);

    let testEditor;
    testEditor = editormd.markdownToHTML("content", {
        markdown: article.bodyMD
    });
}

function editArticle(articleId) {
    let link = "../editor.html?id=" + articleId;
    window.location.href = link;
}

function bindClick(articleId) {
    $("#update").click(function() {
        editArticle(articleId);
    });
    $("#delete").click(function() {
        $.post("articlemanager", {
            "action": "delete",
            "articleId": articleId
        }, function () {
            alert("successfully deleted");
            window.location.href = "../index.html";
        });
    });
}

function getAutherName(article) {
    if (article == null ||
            article.article_id <= 0 ||
            article.auther_id < 0) {
        return -1;
    }
    $.ajax({
        type: "POST",
        async: true,
        url: "articleviewer",
        data: {
            "action": "modify",
            "articleId": article.article_id,
            "autherId": article.auther_id,
        },
        dataType: "json",
        timeout: 1000,
        success: function (result) {
            $("#auther").text("Auther: " + result.autherName);
            if (result.isOwner == true) {
                bindClick(article.article_id);
                $("#modify").show();
            } else {
                $("#modify").hide();
            }
        }
    });
}

$(document).ready(function () {
    "use strict";
    renderArticle(getQueryVariable("id"));
});
