javascript:

function getClickCount() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (4 == xmlHttp.readyState && 200 == xmlHttp.status) {
            document.getElementById("click_count").innerHTML = xmlHttp.responseText;
        }
    }
    xmlHttp.open("POST", "clickcount", true);
    xmlHttp.send();
}

/* Here is the script loaded when the page is opened */
getClickCount();