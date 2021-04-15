javascript:

var editor;
$(function() {
    editor = editormd("editor", {
        width  : "60%",
        height : "90%",
        syncScrolling : "single",
        path   : "res/editormd/lib/"
    });
});

$(document).ready(function() {
    $("#title").val("no title article");
    $("button").click(function() {
        var md = editor.getMarkdown();
        var sum = md.substr(0, 50);
        var title = $("#title").val();
        if (title == null || title.length <= 0) {
            alert("please input title!");
            return;
        }

        var msg = {
            action: 'post',
            articleId: '0',
            title: escape(title),
            summary: escape(sum),
            tags: escape('default'),
            bodyMD: escape(md),
            permission: '0'
        };

        $.ajax({
            type: 'POST',
            url: 'articlemanager',
            data: msg,
            // dataType: 'JSON',
            async: 'true',
            error : function() {
                console.log('submit failure');
            },
            success: function(result) {
                if (result == "fail") {
                    console.log('server failed to submit');
                } else {
                    window.open("../index.html", "_self");
                }
            }
        });
    });
});
