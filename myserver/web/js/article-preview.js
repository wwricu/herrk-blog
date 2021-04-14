let articleIndex = 0;
let articleNum = 0;

function getArticleNum() {
    $.ajax({
        type: "POST",
        async: false,
        url: "articleviewer",
        data: {"action": "getnum"},
        dataType: "text",
        timeout: 1000,
        success: function (num) {
            articleNum = parseInt(num);
        }
    });
}

function addArticle() {
    "use strict";
    if (articleIndex == articleNum) {
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
            "action": "preview",
            "index": articleIndex,
            "order": "last_modify_time"
        },
        dataType: "json",
        timeout: 1000,
        success: function (receive) {
            if (receive === null) {
                return -1;
            }
            article.article_id = receive.article_id;
            article.auther_id = receive.auther_id;
            article.title = unescape(receive.title);
            article.summary = unescape(receive.summary);
            article.create_time = receive.create_time;
            article.last_modify_time = receive.last_modify_time;
        }
    });
    let link = '../viewer.html?id=' + article.article_id;
    let title = "<a class = article-title href = \"" + link + "\"align = \"left\"></a>";
    let articleBody = $("<div class = article ></div>")
        .append($(title).text(article.title))
        .append($("<div class = article-preview align = \"left\"></div>").text(article.summary));
    $(".articleZone").append(articleBody);
    articleIndex++;
}

function renderPage() {
    "use strict";
    $(window).scroll(function() {
        if ($(document).scrollTop() + $(window).height() >= $(document).height() - 1) {
            // console.log($(document).scrollTop() + $(window).height() + $(document).height());
            if (articleIndex < articleNum) {
                addArticle();
            }
        }
    });
}

$(document).ready(function () {
    "use strict";
    getArticleNum();
    while (articleIndex < articleNum &&
        ($(window).height() == $(document).height() || articleIndex < 10)) {
        addArticle();
    }
    renderPage();
});
