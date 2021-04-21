function bindManagers() {
    $("#article-manager").click(function() {
        /* classData = {"list": [{
                    "classId":1,
                    "className":"1",
                    "fatherId": 1,
                    "group":0
            }]} */
        $.ajax({
            type: "POST",
            async: true,
            url: "classmanager",
            data: {
                "action": "allclasses"
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
}

function bindAddClass() {
    $("#add-class-add").click(function() {
        $("#add-class-add").hide();
        $("#add-class-name").show();
        $("#add-class-submit").show();
        $("#add-class-cancel").show();
    });
    $("#add-class-cancel").click(function() {
        $("#add-class-add").show();
        $("#add-class-name").hide();
        $("#add-class-submit").hide();
        $("#add-class-cancel").hide();
    });
    $("#add-class-submit").click(function() {
        let className = $("#add-class-name").val();
        if (className.length < 0 || className.length > 100) {
            alert("invalid length");
            return;
        }
        $.ajax({
            type: "POST",
            async: true,
            url: "classmanager",
            data: {
                "action": "create",
                "className": className,
                "fatherId": 0,
                "group": 0
            },
            dataType: "text",
            timeout: 1000,
            success: function (result) {
                if (result == "failure") {
                    return;
                }
                $("#add-class-add").show();
                $("#add-class-name").hide();
                $("#add-class-submit").hide();
                $("#add-class-cancel").hide();
            }
        });
    });
}

$(function() {
    bindAddClass();
    bindManagers();
});