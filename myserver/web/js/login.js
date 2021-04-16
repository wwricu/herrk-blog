
function bindLogin() {
    $("#login").click(function() {
        $.ajax({
            type: 'POST',
            url: 'usermanage',
            data: {
                "action": "login",
                "username": $("#username").val(),
                "password": $("#password").val()
            },
            async: 'true',
            dataType: "json",
            error : function() {
                console.log('submit failure');
            },
            success: function(result) {
                if (result == "fail") {
                    console.log('Mismatch between Username and Password!');
                } else {
                    sessionStorage.setItem("token", result.token);
                    let link = "../index.html?id=" + result.userId + "&name=" + result.userName;
                    window.open(link, "_self");
                }
            }
        });
    });
}

$(function() {
    bindLogin();
});