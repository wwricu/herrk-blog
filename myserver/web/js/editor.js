javascript:

$(document).ready(function() {
    $("button").click(function() {
        var data = {
            action: 'post',
            articleId: '0',
            autherId: 'unknown',
            title: 'title',
            summary: 'summary',
            tags: 'tags',
            body: 'body',
            permission: '0',
        },
    });
});

$(function() {
    var editor = editormd("editor", {
        width  : "60%",
        height : "80%",
        syncScrolling : "single",
        path   : "res/editormd/lib/"
    });
});
