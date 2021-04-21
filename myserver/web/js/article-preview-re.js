let articleLeft = true;
let articleIndex = 0;

function addArticle(classId, num) {
    "use strict";
    let article = {
        "article_id": 0,
        "auther_id": 0,
        "class_id": 0,
        "class_name": "",
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
            "classId": classId,
            "index": articleIndex,
            "num": num,
            "order": "last_modify_time"
        },
        dataType: "json",
        timeout: 1000,
        success: function (receive) {
            if (receive.list.length > 0) {
                articleIndex += receive.list.length;
                if (receive.list.length < num) {
                    articleLeft = false;
                }

                for (let i = 0; i < receive.list.length; i++) {
                    let link = '../viewer.html?id=' + receive.list[i].article_id;
                    let title = "<a class = \"article-title\" href = \"" + link + "\"align = \"left\"></a>";
                    let articleCard = $("<div class=\"card article-append\"></div>")
                        .append("<div class=container></div>")
                        .append($(title).text(receive.list[i].title));
                    $("#left-frame").append(articleCard);
                }
                return;
            }
            articleLeft = false;
        }
    });
    /*if (article.article_id <= 0) {
        return -1;
    }
    let link = '../viewer.html?id=' + article.article_id;
    let title = "<a class = article-title href = \"" + link + "\"align = \"left\"></a>";
    let articleBody = $("<div class = article ></div>")
        .append($(title).text(article.title))
        .append($("<div class = article-preview align = \"left\"></div>").text(article.summary))
        .append($("<div class = article-auther align = \"right\"></div>")
                .append($("<span class=crttime-prefix></span>").text("Created time: "))
                .append($("<span class=created-time></span>").text(article.create_time))
                .append($("<span class=auther-prefix></span>").text("Auther: "))
                .append($("<span class=auther-name></span>").text(article.auther_name)));
    $(".articleZone").append(articleBody);
    articleIndex++;*/
}

function scrollLoad() {
    "use strict";
    $(window).scroll(function() {
        if ($(document).scrollTop() + $(window).height() >= $(document).height() - 1) {
            // console.log($(document).scrollTop() + $(window).height() + $(document).height());
            if (articleLeft) {
                addArticle(0, 1);
            }
        }
    });
}

function renderPage() {
    while (articleLeft && $(window).height() == $(document).height()) {
        addArticle(0, 5);
    }
}

$(document).ready(function () {
    "use strict";
    renderPage();
    setInterval(renderPage,2000);
    scrollLoad();
});
