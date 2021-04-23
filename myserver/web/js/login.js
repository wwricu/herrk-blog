
function bindLogin() {
    $("#login").click(function() {
        $.ajax({
            type: 'POST',
            url: 'usermanager',
            data: {
                "action": "login",
                "userName": escape($("#username").val()),
                "password": escape($("#password").val())
            },
            async: 'true',
            dataType: "json",
            error : function() {
                console.log('submit failure');
            },
            success: function(result) {
                if (result == "failure") {
                    console.log('Mismatch between Username and Password!');
                } else {
                    localStorage.setItem("token", result.token);
                    let link = "../index.html?id=" + result.userId + "&name=" + unescape(result.userName);
                    window.open(link, "_self");
                }
            }
        });
    });
}

$(function() {
    bindLogin();
});