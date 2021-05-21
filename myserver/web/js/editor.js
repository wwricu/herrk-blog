var editor;

function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i=0; i < vars.length; i++) {
        let pair = vars[i].split("=");
        if(pair[0] == variable) {return pair[1];}
    }
    return false;
}

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
        success: function (result) {
            if (result == "failure") {
                return;
            }
            for (let i = 0; i < result.list.length; i++) {
                let classId = result.list[i].classId;
                let classOption = $("<option value =\"" +
                            classId +
                            "\" class='class-option'>" +
                            result.list[i].className +
                            "</option>");
                $("#class-select").append(classOption);
            }
        }
    });
}

function updateArticle() {
    let articleId = getQueryVariable("id");
    if (articleId == false) {
        return;
    }
    let article = {
        "article_id": articleId,
        "auther_id": 0,
        "class_id": 0,
        "title": "",
        "summary": "",
        "tags": "",
        "bodyMD": "",
        "create_time": "",
        "last_modify_time": "",
        "permission": 0
    };

    $.ajax({
        type: "POST",
        async: true,
        url: "articleviewer",
        data: {
            "action": "view",
            "articleId": articleId,
        },
        dataType: "json",
        timeout: 1000,
        success: function (receive) {
            if (receive.bodyMD != null) {
                $("textarea").val(unescape(receive.bodyMD));
                $("#submit").unbind("click");
                bindUpdate(articleId);
            }
            $("#class-select").val(receive.class_id);
            if (receive.title != null) {
                $("#title").val(unescape(receive.title));
            }
        }
    });
}

function bindUpdate(articleId) {
    $("#submit").text("Update");
    $("#submit").click(function() {
        var md = editor.getMarkdown();
        var sum = md.substr(0, 256);
        var title = $("#title").val();
        if (title == null || title.length <= 0) {
            alert("please input title!");
            return;
        }

        var msg = {
            action: 'update',
            articleId: articleId,
            classId: $('#class-select option:selected').val(),
            title: escape(title),
            summary: escape(sum),
            tags: escape('default'),
            bodyMD: escape(md)
        };

        $.ajax({
            type: 'POST',
            url: 'articlemanager',
            data: msg,
            // dataType: 'JSON',
            async: 'true',
            error : function() {
                console.log('submit failure');
            },
            success: function(result) {
                if (result == "failure") {
                    console.log('server failed to submit');
                } else {
                    alert("successfully updated");
                    window.open("../viewer.html?id=" + articleId, "_self");
                }
            }
        });
    });
}

function bindPost() {
    $("#title").val("no title article");
    $("button").click(function() {
        var md = editor.getMarkdown();
        var sum = md.substr(0, 256);
        var title = $("#title").val();
        if (title == null || title.length <= 0) {
            alert("please input title!");
            return;
        }

        var msg = {
            action: 'post',
            articleId: '0',
            classId: $('#class-select option:selected').val(),
            title: escape(title),
            summary: escape(sum),
            tags: escape('default'),
            bodyMD: escape(md),
            permission: '0'
        };

        $.ajax({
            type: 'POST',
            url: 'articlemanager',
            data: msg,
            // dataType: 'JSON',
            async: 'true',
            error : function() {
                console.log('submit failure');
            },
            success: function(result) {
                if (result == "failure" && result < 0) {
                    console.log('server failed to submit');
                } else {
                    window.open("../index.html", "_self");
                }
            }
        });
    });
}

$(function() {
    editor = editormd("editor", {
        // placeholder: "",
        width: "100%",
        autoHeight: true,
        syncScrolling: "single",
        path: "res/editormd/lib/",
        onload: function() {
            bindPost();
            updateArticle();
        },

        tabSize              : 8,
        indentUnit           : 8,
        lineNumbers          : true,
        lineWrapping         : true,
        autoCloseBrackets    : true,
        showTrailingSpace    : true,
        matchBrackets        : true,
        indentWithTabs       : false,
        styleSelectedText    : true,

        toolbarAutoFixed: true,
        codeFold: true,
        emoji: true,
        taskList: true,
        tocm: true,
        tex: true,
        flowChart: true,
        sequenceDiagram: true,
    });
    getClasses();
});