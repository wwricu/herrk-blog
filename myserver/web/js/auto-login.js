javascript:

function autoLogin() {
    var token = localStorage.getItem("token");
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
                $('#editor').attr("href", "../editor.html");
                document.getElementById("loginName").innerHTML = result.username;
                localStorage.setItem("token", result.token);
            }
        });

        return;
    }
    $.ajax({
        type: 'POST',
        url: 'usermanage',
        data: {
            "action": 'token',
            "token":  token
        },
        dataType: 'json',
        async: 'true',
        error : function() {
            console.log('no token ajax fail');
        },
        success: function(result) {
            if (result.userId <= 0) {
                $("#signBtn").show();
                $("#loginName").hide();
                return;
            }
            $("#signBtn").hide();
            $("#loginName").show();
            $("#loginName").text(result.userName);
            $('#editor').attr("href", "../editor.html");
        }
    });
}

autoLogin();