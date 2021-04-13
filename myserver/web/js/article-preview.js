let pageMax = 5;
let articleIndex = 0;
let articleNum;
let pageNum;
let pageIndex;

function getArticleNum() {
    $.ajax({
        type: "get",
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

function getPageNum() {
    "use strict";
    pageNum = Math.floor(articleNum / pageMax);
    if (articleNum != pageNum * pageMax) {
        pageNum++;
    }
}

function addArticle() {
    "use strict";
    var article = $("<div class = article></div>")
        .append($("<div class = article-title align = \"left\"></div>").text("title"))
        .append($("<div class = article-preview align = \"left\"></div>").text("summary"));
    $(".articleZone").append(article);
}

function renderPage() {
    "use strict";
    while ($(window).height() == $(document).height()) {
        addArticle();
    }
    $(window).scroll(function() {
        if ($(document).scrollTop() + $(window).height() >= $(document).height() - 1) {
            console.log($(document).scrollTop() + $(window).height() + $(document).height());
            addArticle();
        }
    });
}

$(document).ready(function () {
    "use strict";
    getArticleNum();
    renderPage();
});
