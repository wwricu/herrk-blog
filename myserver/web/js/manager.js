function bindManagers() {
    $("#class-manager").click(function() {
        $(".append").remove();
        let classAdder = $("<div id=\"add-class\" class=\"append\" align=center></div>")
            .append($("<button id=add-class-add class=add-class-btn>Add a new class</button>"))
            .append($("<input id=add-class-name class=add-class-btn type=\"text\" placeholder=\"input class name\" style=\"display: none;\">"))
            .append($("<button id=add-class-submit class=add-class-btn style=\"display: none;\">confirm</button>"))
            .append($("<button id=add-class-cancel class=add-class-btn style=\"display: none;\">cancel</button>"));
        $("#right-frame").append(classAdder);
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
            /* result = {"list": [{
                    "classId":1,
                    "className":"1",
                    "fatherId": 1,
                    "group":0
                }]} */
            success: function (result) {
                if (result == "failure") {
                    return;
                }
                for (let i = 0; i < result.list.length; i++) {
                    let classCard = $("<div class=\"card append\"></div>")
                        .append($("<div class=container></div>")
                                .append($("<h4 class = class-name align = \"left\"></h4>")
                                    .text(result.list[i].className)));
                    $("#right-frame").append(classCard);
                }
            }
        });
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
            }
        });
    });
}

$(function() {
    bindManagers();
});