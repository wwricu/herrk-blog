function classAdder() {
    let classAdder =
        "<div id=add-class class='manager-card append' align='center'> \
            <div id=add-class-add></div> \
            <input id=add-class-name type=\"text\" placeholder=\"input class name\" style=\"display: none;\"> \
            <div id=add-class-submit style=\"display: none;\">confirm</div> \
            <div id=add-class-cancel style=\"display: none;\">cancel</div> \
        </div>";
    $("#main-frame").append(classAdder);
    bindAddClass();
}

function bindManagers() {
    $("#class-manager").on("click", classManager);
}

function classManager() {
    $(".append").remove();
    bindAddClass();
    $.ajax({
        type: "POST",
        async: true,
        url: "classmanager",
        data: {
            "action": "allclasses"
        },
        dataType: "json",
        timeout: 1000,
        error: function() {
            classAdder();
        },
        /* result = {"list": [{
                "classId":1,
                "className":"1",
                "fatherId": 1,
                "group":0,
                "articleCount":0
            }]} */
        success: function (result) {
            if (result == "failure") {
                classAdder();
                return;
            }
            for (let i = 0; i < result.list.length; i++) {
                let classCard =
                    "<div id=class-card-" + result.list[i].classId + " class='manager-card append'> \
                        <span class=class-name>" + result.list[i].className + "</span> \
                        <div id=delete-" + result.list[i].classId + " class=class-delete>delete</div> \
                        <div id=update-" + result.list[i].classId + " class=class-update>update</div> \
                        <span class=class-count>article count: " + result.list[i].articleCount + "</span> \
                    </div>";
                $("#main-frame").append(classCard);
                $("#update-0").hide();
                $("#delete-0").hide();
                if (result.list[i].classId == 0) {
                    continue;
                }
                $("#delete-" + result.list[i].classId).click(function() {
                    $.ajax({
                        type: "POST",
                        async: true,
                        url: "classmanager",
                        data: {
                            "action": "delete",
                            "classId": result.list[i].classId,
                        },
                        dataType: "text",
                        timeout: 1000,
                        success: function(result) {
                            if (result > 0) {
                                $("#class-card-" + result).hide();
                            }
                        }
                    });
                });
            }
            classAdder();
        }
    });
}

function showAddBtn(show) {
    if (show == false) {
        $("#add-class-add").hide();
        $("#add-class-name").show();
        $("#add-class-submit").show();
        $("#add-class-cancel").show();
    } else {
        $("#add-class-add").show();
        $("#add-class-name").hide();
        $("#add-class-submit").hide();
        $("#add-class-cancel").hide();
    }
}

function bindAddClass() {
    $("#add-class-add").click(function() {
        showAddBtn(false);
    });
    $("#add-class-cancel").click(function() {
        showAddBtn(true);
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
                showAddBtn(true);
                classManager();
            }
        });
    });
}

$(function() {
    bindManagers();
});