javascript:

function getClickCount() {
    $.ajax({
        type: "post",
        async: true,
        url: "clickcount",
        data: {"action": "netspeed"},
        dataType: "text",
        timeout: 1000,
        success: function(click) {
            $("#click_count").text(click);
        }
    });
}

function getNetSpeed() {
    var sendTime = new Date().getTime();
    $.ajax({
        type: "get",
        async: true,
        url: "daemon",
        data: {"action": "netspeed"},
        dataType: "json",
        timeout: 1000,
        success: function(netspeed) {
            $("#inbound").text(netspeed.inbound + " KB/s");
            $("#outbound").text(netspeed.outbound + " KB/s");
            $("#latency").text(new Date().getTime() - sendTime - 5 + " ms");
        }
    });
}

function getUptime() {
    $.ajax({
        type: "get",
        async: true,
        url: "daemon",
        data: {"action": "uptime"},
        dataType: "text",
        timeout: 1000,
        success: function(uptime) { // second
            var mins = Math.floor(uptime / 60);
            var hrs = Math.floor(mins / 60);
            var days = Math.floor(hrs / 24);
            $("#uptime").text(String(days) + "d " +
                    String(hrs - days * 24) + "h " +
                    String(mins - hrs * 60) + "m");
        }
    });
}

function updateUptime() {
    var min = parseInt($("#minute").text()) + 1;
    if (min == 60) {
        $("#minute").text(0);
        var hour = parseInt($("#hour").text()) + 1;
        if (hour == 24) {
            $("#hour").text(0);
            var day = parseInt($("#day").text()) + 1;
            $("#day").text(day);
        } else {
            $("#hour").text(hour);
        }
    } else {
        $("#minute").text(min);
    }
}

$(document).ready(function () {
    setInterval(getNetSpeed,2000);
    getUptime();
    setInterval(updateUptime,60000);
    getClickCount();
});
