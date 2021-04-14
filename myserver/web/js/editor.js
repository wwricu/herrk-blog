javascript:

var editor;
$(function() {
    editor = editormd("editor", {
        width  : "60%",
        height : "80%",
        syncScrolling : "single",
        path   : "res/editormd/lib/"
    });
});

$(document).ready(function() {
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
            title: title,
            summary: sum,
            tags: 'default',
            bodyMD: md,
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
                    console.log(result);
                    alert("success");
                }
            }
        });
    });
});
