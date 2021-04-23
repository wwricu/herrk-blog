let articleLeft = true;
let articleIndex = 0;
let classId = 0;

function addArticle(num) {
    "use strict";
    $.ajax({
        type: "POST",
        async: false,
        url: "articleviewer",
        /* receive = { list:[{
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
        }];*/
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

                /*
                <div class="card article-append">
                    <a class=article-title href="title-link">title</a>
                    <div class=article-info>class | Created at 2021/04/24  |  Last modified at 2021/04/25</div>
                    <hr class='title-sum-hr'>
                    <p class = article-summary>summary</p>
                </div>
                */
                for (let i = 0; i < receive.list.length; i++) {
                    let link = '../viewer.html?id=' + receive.list[i].article_id;
                    let info = receive.list[i].class_name +
                            " | Created at " + receive.list[i].create_time +
                            " | Last modified at " + receive.list[i].last_modify_time;
                    let articleCard =
                        "<div class='card article-append'> \
                            <a class=article-title href='" + link + "'>" + unescape(receive.list[i].title) + "</a> \
                            <div class=article-info>" + info + "</div> \
                            <hr class='title-sum-hr'> \
                            <p class = article-summary>" + unescape(receive.list[i].summary) + "</p> \
                        </div>";
                    $("#left-frame").append(articleCard);
                }
            } else {
                articleLeft = false;
            }
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
                let button = "<button id=class-" + localClassId + " class='class-append item-title'></button>"
                let classCard = $("<div class = \"card-item\"></div>")
                        .append($(button).text(result.list[i].className))
                        .append($("<span class = item-stat></span>").text(result.list[i].articleCount));
                $("#class-container").append(classCard);

                /*
                   when the user click a specific class,
                   its font would go bigger to hint the user which class he is viewing.
                   when the user click anther class,
                   all of the font go back to their origin size except the one he choose.
                 */
                $("#class-" + localClassId).on("click", function() {
                    $(".article-append").remove();
                    articleLeft = true;
                    articleIndex = 0;

                    $(".class-append").css("font-size","1.2rem");
                    if (classId == localClassId) {
                        classId = 0;
                    } else {
                        classId = localClassId;
                        $("#class-" + localClassId).css("font-size","1.5rem");
                    }

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
