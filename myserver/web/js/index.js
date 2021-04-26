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
                        "<div id=article-" + receive.list[i].article_id + " class='card article-append'> \
                            <a class=article-title href='" + link + "'>" + unescape(receive.list[i].title) + "</a> \
                            <div class=article-info>" + info + "</div> \
                            <hr class='title-sum-hr'> \
                            <p class = article-summary>" + unescape(receive.list[i].summary) + "</p> \
                        </div>";
                    $("#left-frame").append(articleCard);
                    $("#article-" + receive.list[i].article_id).click(function() {
                        window.open(link, "_self");
                    });
                }
            } else {
                articleLeft = false;
            }
        }
    });
}

function getClasses() {
    $(".class-card").remove();
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
                let classCard =
                    "<div class = 'class-item class-card'> \
                        <button id=class-" + localClassId + " class='class-append item-title'> \
                            <img id=class-icon-" + localClassId + " src='../res/image/chooser.png' >" +
                            result.list[i].className +
                            "<span id=class-stat-" + localClassId + " class='class-stat'>" +
                                result.list[i].articleCount +
                            "</span> \
                        </button> \
                    </div>";
                $("#class-container").append(classCard);
                /*$("#class-" + localClassId).hover(
                    function() {
                        $("#class-icon-" + localClassId).css("display", "inline");
                    }, function() {
                        $("#class-icon-" + localClassId).css("display", "none");
                    }
                );*/
                /*
                   when the user click a specific class,
                   its font would go bigger to hint the user which class he is viewing.
                   when the user click anther class,
                   all of the font go back to their origin size except the one he choose.
                 */
                $("#class-" + localClassId).on("click", function() {
                    $(".article-append").remove();
                    $(".class-stat").css("padding-right", "0");
                    $(".class-append").children("img").hide();
                    $(".class-chosen").children("img").hide();


                    articleLeft = true;
                    articleIndex = 0;

                    if (classId == localClassId) {
                        classId = 0;
                        $("#class-" + localClassId).removeClass('class-chosen');
                        $("#class-" + localClassId).addClass('class-append item-title');
                        $(".class-append").children("img").hide();
                    } else {
                        /* Two actions when choose another button:
                         * 1. clear all other button's css
                         * 2. set a new btn's css
                        */
                        classId = localClassId;
                        // restore
                        $(".class-chosen").addClass("class-append").removeClass("class-chosen");
                        // add css
                        $("#class-" + localClassId).removeClass('class-append').addClass('class-chosen item-title');
                        $("#class-stat-" + localClassId).css("padding-right", "0.5rem");
                        // $("#class-icon-" + localClassId).show();
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

function turnToMobile() {
    $("#right-frame").hide();
    $("#left-frame").css("width", "100%");
    $("#main-frame").css("width", "100%");
    $(".article-info").css("font-size", "0.7rem");
    $("body").css("background", "#F4E7D7");
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
    if (isMobile()) {
        turnToMobile();
    }
});
