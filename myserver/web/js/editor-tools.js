javascript:

$(document).ready(function () {
    $(".tool-button").eq(0).click(function () {
        document.execCommand("bold", false, null);
    });
    $(".tool-button").eq(1).click(function () {
        document.execCommand("italic", false, null);
    });
    $(".tool-button").eq(2).click(function () {
        var fontSize = $(".tool-input").eq(0).val();
        document.execCommand("fontsize", false, fontSize);
    });
    $(".tool-button").eq(3).click(function () {
        var title = $("#topFrame").children("input[type='text']").val();
        var article = $("#editor").html();

        var url = "articlemanager";

        var params = {
            "action" : "post",
            "title" : title,
            "article" : article
        };

        httpPost(url, params);
        /*$.post(url, params,
            function(data) {
                alert(data);
            },
        "json"); jquery post is async so client cannot jump to a new page easily*/
    });
});

function httpPost(URL, PARAMS) {
    var temp = document.createElement("form");
    temp.action = URL;
    temp.method = "post";
    temp.style.display = "none";

    for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp.appendChild(opt);
    }

    document.body.appendChild(temp);
    temp.submit();

    return temp;
}