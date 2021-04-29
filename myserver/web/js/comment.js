let mainCommentNum = 0;
let pageNum = 1;
let currentPage = 0;
let commentPerPage = 5;


function loadComment() {
    $.ajax({
        type: 'POST',
        url: 'commentmanager',
        data: {
            action: 'getlatest',
            index: currentPage * commentPerPage,
            num: commentPerPage
        },
        dataType: 'json',
        async: 'true',
        error : function() {
            console.log('no token ajax fail');
        },
        success: function(result) {
            if (result == "failure") {
                return;
            }
        }
    });
}

function setBtns() {
    $("#prev-page").click(function() {
        if (currentPage == 0) {
            return;
        }
        currentPage++;
        loadComment();
    });
    $("#prev-page").click(function() {
        if (currentPage == pageNum - 1) {
            return;
        }
        currentPage--;
        loadComment();
    });
    let btn0 = $("#page-btn-frame").children(".page-btn").eq(0).html("1");
    btn0.click(function() {
        currentPage = 0;
        loadComment();
    });

    if (currentPage > 3) {
        $("#btn-dot-1").show();
    } else {
        $("#btn-dot-1").hide();
    }

    if (pageNum < 2) {
        $("#btn-2").show();
    } else {
        $("#btn-2").hide();
    }
    $("#btn-2").click(function() {
        if (currentPage == $("#btn-2").text()) {
            return;
        }
        currentPage = $("#btn-2").text();
        loadComment();
    });

    if (pageNum - currentPage < 3) {
        $("#btn-dot-2").show();
    } else {
        $("#btn-dot-2").hide();
    }
    if (pageNum > 4) {
        $("btn-5").show();
        $("#btn-5").text(pageNum);
    } else {
        $("#btn-5").hide();
    }
    $("#btn-5").click(function() {
        currentPage = pageNum - 1;
        loadComment();
    });
}

function configPages() {

    $.ajax({
        type: 'POST',
        url: 'commentmanager',
        data: {
            action: 'maincommentnum',
        },
        dataType: 'text',
        async: 'false',
        success: function(result) {
            if (result == "failure") {
                return;
            }
            mainCommentNum = result;
            pageNum = mainCommentNum / 5;
        }
    });
}

$(function() {
    configPages();
    setBtns();
    loadComment();
});