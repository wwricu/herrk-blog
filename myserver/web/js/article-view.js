function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i=0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
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
            article.title = receive.title;
            article.summary = receive.summary;
            article.tags = receive.tags;
            article.bodyMD = receive.bodyMD;
            article.create_time = receive.create_time;
            article.last_modify_time = receive.last_modify_time;
            article.permission = receive.permission;
        }
    });

    $(content).text(article.title + "\n");

    let testEditor;
    testEditor = editormd.markdownToHTML("content", {
        markdown: article.bodyMD
    });
}

$(document).ready(function () {
    "use strict";
    renderArticle(getQueryVariable("id"));
});
