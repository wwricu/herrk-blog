function bindBtns() {
    $("#top").click(function() {
        window.open("#goTop", "_self");
    });
    $("#personal-link").click(function () {
        window.open("about.html", "_self");
    });
}

$(function () {
    bindBtns();
});