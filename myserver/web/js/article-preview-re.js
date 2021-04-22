let articleLeft = true;
let articleIndex = 0;
let classId = 0;

function addArticle(num) {
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
}

function getClasses() {
    $.ajax({
        type: "POST",
        async: true,
        url: "classmanager",
        data: {
            "action": "allclasses"
        },
        dataType: "json",
        timeout: 1000,
        success: function(result) {
            if (result == "failure") {
                return;
            }
            for (let i = 0; i < result.list.length; i++) {
                let localClassId = result.list[i].classId;
                let button = "<button id=class-" + localClassId + " class='class-append'></button>"
                let classCard = $("<div class = \"card-item\"></div>")
                        .append($(button).text(result.list[i].className));
                $("#class-container").append(classCard);
                $("#class-" + localClassId).click(function() {
                    $(".article-append").remove();
                    articleLeft = true;
                    articleIndex = 0;
                    classId = localClassId;
                    renderPage();
                });
            }
        }
    });
}

function scrollLoad() {
    "use strict";
    $(window).scroll(function() {
        if ($(document).scrollTop() + $(window).height() >= $(document).height() - 1) {
            // console.log($(document).scrollTop() + $(window).height() + $(document).height());
            if (articleLeft) {
                addArticle(1);
            }
        }
    });
}

function renderPage() {
    addArticle(5);
}

$(document).ready(function () {
    "use strict";
    getClasses();
    renderPage();
    // setInterval(renderPage(0),2000);
    scrollLoad();
});
