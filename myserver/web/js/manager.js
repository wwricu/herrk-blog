$("#article-manager").click(function() {
    /* classData = {
        "list": [
            {
                "classId":1,
                "className":"1",
                "fatherId": 1,
                "group":0
            },
            {
                "classId":1,
                "className":"1",
                "fatherId": 1,
                "group":0
            },
        ]
    } */
    $.ajax({
        type: "POST",
        async: true,
        url: "classmanager",
        data: {
            "action": "allclasses",
        },
        dataType: "json",
        timeout: 1000,
        success: function (result) {
            if (result == "failure") {
                return;
            }
            for (let i = 0; i < result.list.length; i++) {
                let classCard = $("<div class=card></div>")
                .append("<div class=container></div>")
                    .append($("<h4 class = class-name align = \"left\"></h4>").text(result.list[i].className));
                $("#right-frame").append(classCard);
            }
        }
    });
});