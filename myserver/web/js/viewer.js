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
        "permission": 0,
        "class_name": ""
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
            getAutherName(receive);
            $("#title").text(unescape(receive.title));
            $("#className").text(receive.class_name);
            $("#time").text("Created at " + receive.create_time
                + " Last modified at " + receive.last_modify_time);

            let testEditor = editormd.markdownToHTML("content", {
                markdown: unescape(receive.bodyMD)
            });
        }
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

function isMobile() {
    var system = {
        win: false,
        mac: false,
        x11: false,
        ipad: false
    };

    var p = navigator.platform;

    system.win = p.indexOf("Win") == 0;
    system.mac = p.indexOf("Mac") == 0;
    system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
    system.ipad = (navigator.userAgent.match(/iPad/i) != null) ? true : false;

    if (system.win || system.mac || system.x11 || system.ipad) {
        return false;
    } else {
        return true;
    }
}

$(function () {
    "use strict";
    if (isMobile()) {
        $("#center-frame").css("width", "100%");
        $("body").css("background", "#F4E7D7");
    }
    renderArticle(getQueryVariable("id"));
});
