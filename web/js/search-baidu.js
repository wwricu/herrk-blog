javascript:

$(document).keypress(function(e) {

    if (13 == e.keyCode || 13 == e.which || 13 == e.charCode) {
        $("#search-box").children("input[type='button']").trigger("click");
    }
});

$(document).ready(function() {
    $("#search-box").children("input[type='text']").val("");
    $("#search-box").children("input[type='button']").click(
        function () {
            $("#search-box").children("input[type='text']").focus();
            var baiduHeader = "https://www.baidu.com/s?ie=utf-8&f=12&tn=baidu_local&wd="
            var keyWord = $("#search-box").children("input[type='text']").val();

            if (null != keyWord && "" != keyWord) {
                window.open(baiduHeader+keyWord, "_self");
                $("#search-box").children("input[type='text']").val("");
            }
        }
    );
});