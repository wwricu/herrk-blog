javascript:

function autoLogin() {
    var token = sessionStorage.getItem("token");
    if (null == token) {
        $("#signBtn").show();
        $("#loginName").hide();

        $.ajax({
            type: 'POST',
            url: 'usermanage',
            data: {
                action: 'notoken'
            },
            // dataType: 'JSON',
            async: 'true',
            error : function() {
                console.log('no token ajax fail');
            },
            success: function(result) {
                if (result == "fail") {
                    return;
                }
                // json string-->json object
                result = eval('(' + result + ')');
                $("#signBtn").hide();
                $("#loginName").show();
                //document.getElementById("signBtn").style.display = "none";
                //document.getElementById("loginName").style.display = "block";
                document.getElementById("loginName").innerHTML = result.username;
                sessionStorage.setItem("token", result.token);
            }
        });

        return;
    }

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (4 == xmlHttp.readyState && 200 == xmlHttp.status) {
            if (null != xmlHttp.responseText && "fail" != xmlHttp.responseText) {
                $("#signBtn").hide();
                $("#loginName").show();
                document.getElementById("loginName").innerHTML = xmlHttp.responseText;
            } else {
                $("#signBtn").show();
                $("#loginName").hide();
            }
        }
    }

    xmlHttp.open("POST", "usermanage", true);
    xmlHttp.setRequestHeader('content-type', 'application/x-www-form-urlencoded');
    var parameter = 'action=token&username=admin&token='+token;
    xmlHttp.send(parameter);
}

autoLogin();