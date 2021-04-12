javascript:

var editor;
$(function() {
    editor = editormd("editor", {
        width  : "60%",
        height : "80%",
        syncScrolling : "single",
        path   : "res/editormd/lib/"
    });
});

$(document).ready(function() {
    $("button").click(function() {
        var md = editor.getMarkdown();
        var sum = md.substr(0, 50);
        var title = $("#title").val();
        if (title == null || title.length <= 0) {
            alert("please input title!");
            return;
        }

        var msg = {
            action: 'post',
            articleId: '0',
            title: title,
            summary: sum,
            tags: 'default',
            bodyMD: md,
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
                if (result == "fail") {
                    console.log('server failed to submit');
                } else {
                    console.log(result);
                    alert("success");
                }
            }
        });
    });
});

/* function mdToHml(){
    //先对容器初始化，在需要展示的容器中创建textarea隐藏标签，
    $("#testEditorMdview").html('<textarea id="appendTest" style="display:none;"></textarea>');
    var content=$("#demo1").val();//获取需要转换的内容
    $("#appendTest").val(content);//将需要转换的内容加到转换后展示容器的textarea隐藏标签中

    //转换开始,第一个参数是上面的div的id
    editormd.markdownToHTML("testEditorMdview", {
        htmlDecode: "style,script,iframe", //可以过滤标签解码
        emoji: true,
        taskList:true,
        tex: true,               // 默认不解析
        flowChart:true,         // 默认不解析
        sequenceDiagram:true,  // 默认不解析
    });
} */
