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
});