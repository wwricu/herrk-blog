javascript:

$(document).keypress(function(e) {
    if (13 == e.keyCode || 13 == e.which || 13 == e.charCode) {
        if ($("#search-input").is(":focus")) {
            $("#search-button").trigger("click");
        } else {
            $("#search-input").focus();
        }
    }
});

$(document).ready(function() {
    $("#search-input").val("");
    $("#search-button").click(function () {
        $("#search-input").focus();
        let baiduHeader = "https://www.baidu.com/s?ie=utf-8&f=12&tn=baidulocal&wd="
        let keyWord = $("#search-input").val();
        if (null != keyWord && "" != keyWord) {
            window.open(baiduHeader+keyWord, "_self");
            $("#search-input").val("");
        }
    });
});