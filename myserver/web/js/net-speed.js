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

$(document).ready(function () {
    setInterval(getNetSpeed,2000);
});
