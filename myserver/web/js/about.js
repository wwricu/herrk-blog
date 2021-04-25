function isMobile() {
    var system = {
        win: false,
        mac: false,
        x11: false,
        ipad: false
    };

    var p = navigator.platform;

    system.win = p.indexOf("Win") == 0;
    system.mac = p.indexOf("Mac") == 0;
    system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
    system.ipad = (navigator.userAgent.match(/iPad/i) != null) ? true : false;

    if (system.win || system.mac || system.x11 || system.ipad) {
        return false;
    } else {
        return true;
    }
}

function turnToMobile() {
    $("#main-frame").css("width", "100%");
    $("body").css("background", "#F4E7D7");
    $("#main-frame").children("img").css("display", "none");
    $("#about-site").css("padding-left", 0);
    $("#about-site").children("a").css("padding-left", "5rem");
    $("#about-site").children("p").css("padding-left", "5rem");
}

$(function() {
    if (isMobile()) {
        turnToMobile();
    }
});