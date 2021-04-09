javascript:

$(document).ready(function() {
    $("button").click(function() {
        alert("submit!");
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
