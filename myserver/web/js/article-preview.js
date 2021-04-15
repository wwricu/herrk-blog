let articleIndex = 0;
let articleNum = 0;

function getArticleNum() {
    $.ajax({
        type: "POST",
        async: true,
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
            if (!(receive.article_id > 0)) {
                article.article_id = -1;
                return;
            }
            article.article_id = receive.article_id;
            article.auther_id = receive.auther_id;
            article.title = unescape(receive.title);
            article.summary = unescape(receive.summary);
            article.tags = unescape(receive.tags);
            article.create_time = receive.create_time;
            article.last_modify_time = receive.last_modify_time;
        }
    });
    if (article.article_id <= 0) {
        return -1;
    }
    let link = '../viewer.html?id=' + article.article_id;
    let title = "<a class = article-title href = \"" + link + "\"align = \"left\"></a>";
    let articleBody = $("<div class = article ></div>")
        .append($(title).text(article.title))
        .append($("<div class = article-preview align = \"left\"></div>").text(article.summary));
    $(".articleZone").append(articleBody);
    articleIndex++;
}

function scrollLoad() {
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

function renderPage() {
    while (articleIndex < articleNum &&
            $(window).height() == $(document).height()) {
        addArticle();
    }
}

$(document).ready(function () {
    "use strict";
    for (let i = 0; i < 10; i++) {
        if (addArticle() < 0) {
            break;
        }
    }
    getArticleNum();
    renderPage();
    setInterval(renderPage,2000);
    scrollLoad();
});
