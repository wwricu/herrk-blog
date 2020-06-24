javascript:

function printTime() {
    var d = new Date();
    var h = d.getHours();
    var m = d.getMinutes();
    var s = d.getSeconds();
    if (10 > h) {
        h = "0" + h;
    }
    if (10 > m) {
        m = "0" + m;
    }
    if (10 > s) {
        s = "0" + s;
    }

    document.getElementById("time").innerHTML= h + ":" + m + ":" + s;
}

setInterval(printTime,1000);