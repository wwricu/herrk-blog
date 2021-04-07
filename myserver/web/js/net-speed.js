javascript:

function getNetSpeed() {
    $.ajax({
        type: "get",
        async: true,
        url: "daemon",
        data: {"action": "netspeed"},
        dataType: "json",
        timeout: 1000,
        success: function(netspeed) {
            // alert("inbount: " + netspeed.inbound + "outbound" + netspeed.outbound);
            $("#inbound").text(netspeed.inbound + " KB/s");
            $("#outbound").text(netspeed.outbound + " KB/s");
        }
    });
}

$(document).ready(function () {
    setInterval(getNetSpeed,2000);
});
