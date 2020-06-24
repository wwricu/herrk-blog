javascript:

function autoLogin() {
    var token = sessionStorage.getItem("token");
    if (null == token) {
        document.getElementById("signBtn").style.display = "block";
        document.getElementById("loginName").style.display = "none";
        return;
    }
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (4 == xmlHttp.readyState && 200 == xmlHttp.status) {
            if (null != xmlHttp.responseText && "fail" != xmlHttp.responseText) {
                document.getElementById("signBtn").style.display = "none";
                document.getElementById("loginName").style.display = "block";
                document.getElementById("loginName").innerHTML = xmlHttp.responseText;

            } else {
                document.getElementById("signBtn").style.display = "block";
                document.getElementById("loginName").style.display = "none";
            }

        }
    }

    xmlHttp.open("POST", "usermanage", true);
    xmlHttp.setRequestHeader('content-type', 'application/x-www-form-urlencoded');
    var parameter = 'action=token&username=admin&token='+token;
    xmlHttp.send(parameter);
}

autoLogin();