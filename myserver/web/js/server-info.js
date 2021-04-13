javascript:

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
            $("#day").text(days);
            $("#hour").text(hrs - days * 24);
            $("#minute").text(mins - hrs * 60);
        }
    });
}

function updateUptime() {
    var min = $("#minute").text();
    if (min == 59) {
        $("#minute").text(0);
        var hour = $("#hour").text();
        if (hour == 23) {
            $("#hour").text(0);
            var day = $("#day").text();
            $("#day").text(day + 1);
        } else {
            $("#hour").text(hour + 1);
        }
    } else {
        $("#minute").text(min + 1);
    }
}

$(document).ready(function () {
    setInterval(getNetSpeed,2000);
    getUptime();
    setInterval(updateUptime,60000);
});
