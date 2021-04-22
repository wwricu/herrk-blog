function getClasses() {
    $.ajax({
        type: "POST",
        async: true,
        url: "classmanager",
        data: {
            "action": "allclasses"
        },
        dataType: "json",
        timeout: 1000,
        success: function(result) {
            if (result == "failure") {
                return;
            }
            for (let i = 0; i < result.list.length; i++) {
                let classId = result.list[i].classId;
                let button = "<button id=class-" + classId + " class='class-append'></button>"
                let classCard = $("<div class = \"card-item\"></div>")
                    .append($("<button class='item-title class-append'></button>")).text(result.list[i].className);
                $("#class-container").append(classCard);
                $("#" + classId).click(function() {
                    $(".article-append").remove();
                    addArticle(classId);
                });
            }
        }
    });
}



$(function() {
    getClasses();
});
