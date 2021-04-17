var editor;

function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i=0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if(pair[0] == variable) {return pair[1];}
    }
    return false;
}

function updateArticle() {
    let articleId = getQueryVariable("id");
    if (articleId == false) {
        return;
    }
    let article = {
        "article_id": articleId,
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
        async: true,
        url: "articleviewer",
        data: {
            "action": "view",
            "articleId": articleId,
        },
        dataType: "json",
        timeout: 1000,
        success: function (receive) {
            if (receive.bodyMD != null) {
                $("textarea").val(unescape(receive.bodyMD));
                $("#submit").unbind("click");
                bindUpdate(articleId);
            }
            if (receive.title != null) {
                $("#title").val(unescape(receive.title));
            }
        }
    });
}

function bindUpdate(articleId) {
    $("button").click(function() {
        var md = editor.getMarkdown();
        var sum = md.substr(0, 256);
        var title = $("#title").val();
        if (title == null || title.length <= 0) {
            alert("please input title!");
            return;
        }

        var msg = {
            action: 'update',
            articleId: articleId,
            title: escape(title),
            summary: escape(sum),
            tags: escape('default'),
            bodyMD: escape(md)
        };

        $.ajax({
            type: 'POST',
            url: 'articlemanager',
            data: msg,
            // dataType: 'JSON',
            async: 'true',
            error : function() {
                console.log('submit failure');
            },
            success: function(result) {
                if (result == "fail") {
                    console.log('server failed to submit');
                } else {
                    alert("successfully updated");
                    window.open("../viewer.html?id=" + articleId, "_self");
                }
            }
        });
    });
}

function bindPost() {
    $("#title").val("no title article");
    $("button").click(function() {
        var md = editor.getMarkdown();
        var sum = md.substr(0, 256);
        var title = $("#title").val();
        if (title == null || title.length <= 0) {
            alert("please input title!");
            return;
        }

        var msg = {
            action: 'post',
            articleId: '0',
            title: escape(title),
            summary: escape(sum),
            tags: escape('default'),
            bodyMD: escape(md),
            permission: '0'
        };

        $.ajax({
            type: 'POST',
            url: 'articlemanager',
            data: msg,
            // dataType: 'JSON',
            async: 'true',
            error : function() {
                console.log('submit failure');
            },
            success: function(result) {
                if (result == "fail") {
                    console.log('server failed to submit');
                } else {
                    window.open("../index.html", "_self");
                }
            }
        });
    });
}

$(function() {
    editor = editormd("editor", {
        // placeholder: "",
        width  : "62%",
        height : "94%",
        syncScrolling : "single",
        path   : "res/editormd/lib/",
        onload : function() {
            bindPost();
            updateArticle();
        }
    });
});