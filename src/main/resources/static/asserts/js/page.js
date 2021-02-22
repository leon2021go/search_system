$(document).ready(function () {
//简单分页功能之查询
    $("#search_btn").click(function () {

        var post_content = $("#post_content").val();
        var status = $("#status").val();
        var pet_tag = $("#pet_tag").val();
        var label_content = $("#label_content").val();
        var pageNumber = $("#pageNumber").val().trim();
        showStudentsByPage(post_content, status, pet_tag, label_content, pageNumber);
    });

//简单分页功能之go
//     $("#label_content").click(function () {
//         var classID = $("#queryClassId").val();
//         var pageNumber = $("#pageNumber").val().trim();
//         showStudentsByPage(classID,pageNumber);
//     });
//
// //简单分页功能之上一页
//     $("#status").click(function(){
//         var classID = $("#queryClassId").val();
//         var pageNumber = $("#currentPage").text();
//         var nextPageNumber = parseInt(pageNumber)-1;
//         showStudentsByPage(classID,nextPageNumber);
//     });

    $("#Previous").click(function() {
        var post_content = $("#post_content").val();
        var status = $("#status").val();
        var pet_tag = $("#pet_tag").val();
        var label_content = $("#label_content").val();
        // var pageNumber = $("#pageNumber").val().trim();
        console.log($("#currentPage").text().trim());
        var nextPageNumber = 1;
        if ($("#currentPage").text().trim() > 1) {
            nextPageNumber = parseInt($("#currentPage").text().trim()) - 1;
         }
        // if($("#pageNumber").val() == "" || $.trim($("#pageNumber").val()).length == 0) {
        $("#pageNumber").text(nextPageNumber);
        // }else{
        //
        // }
        showStudentsByPage(post_content, status, pet_tag, label_content, nextPageNumber);

    });
//简单分页功能之下一页
    $("#Next").click(function(){
        var post_content = $("#post_content").val();
        var status = $("#status").val();
        var pet_tag = $("#pet_tag").val();
        var label_content = $("#label_content").val();
        // var pageNumber = $("#pageNumber").val().trim();
        console.log($("#currentPage").text().trim())
        var nextPageNumber = parseInt($("#currentPage").text().trim()) + 1;
        // if($("#pageNumber").val() == "" || $.trim($("#pageNumber").val()).length == 0) {
            $("#pageNumber").text(nextPageNumber);
        // }else{
        //
        // }
        showStudentsByPage(post_content, status, pet_tag, label_content, nextPageNumber);

    });

    $("#goPage").click(function(){
        var post_content = $("#post_content").val();
        var status = $("#status").val();
        var pet_tag = $("#pet_tag").val();
        var label_content = $("#label_content").val();
        var pageNumber = $("#currentPage").text();
        console.log('pageNumber:' + pageNumber)
        var nextPageNumber = parseInt(pageNumber)+1;
        console.log("nextPageNumber"+nextPageNumber)
        showStudentsByPage(post_content, status, pet_tag, label_content, nextPageNumber);

    });


    function showStudentsByPage(post_content, status, pet_tag,  label_content,pageNumber) {
        var curWwwPath=window.document.location.href;
        console.log(curWwwPath)
        var pathName=window.document.location.pathname;
        var pos=curWwwPath.indexOf(pathName);
        //获取主机地址，如： http://localhost:8083
        var localhostPaht=curWwwPath.substring(0,pos);
        console.log(localhostPaht)
//ajax请求
        $.ajax({
//请求方式
            type: 'GET',
//路径
            url: "/poster/list/data?petTag="+pet_tag+"&content="+post_content+"&status="+status+"&label="+label_content+"&pageNo="+pageNumber,
//传递的参数
//             data: {petTag:pet_tag, content:post_content, status:status, label:label_content, pageNo:pageNumber},
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
//返回的数据类型
            dataType: "json",
//回调函数 ,成功时返回的数据存在形参data里
            success: function(data){
//拼写HTML代码
                $("#t_list_data").html("");
                var HTML_CODE=data;
                var head_content = "<thead><tr><th>#</th><th>帖子内容</th><th>标签</th><th>发帖人</th>\n" +
                   "<th>发帖状态</th><th>发帖时间</th><th>操作</th></tr></thead><tbody>";

                for(i = 0; i < data.items.length; i++){
                    head_content += "<tr><td>"+data.items[i].id+"</td>";
                    head_content +="<td style=\"max-width: 400px;white-space:nowrap;overflow:hidden;text-overflow: ellipsis;\">"+data.items[i].content+"</td>";
                    head_content +="<td>"+data.items[i].petTag+"</td>";
                    head_content +="<td>"+data.items[i].creatorName+"</td>";
                    if(data.items[i].status == 0){
                        head_content +="<td>未发布</td>";
                    }else if(data.items[i].status == 1){
                        head_content +="<td>已发布</td>";
                    }else{
                        head_content +="<td>已屏蔽</td>";
                    }
                    head_content +="<td>"+data.items[i].createTime+"</td>";
                    head_content +="<td><a class=\"btn btn-sm btn-primary\" href=\""+localhostPaht+"/poster/get/" + data.items[i].id + "\">编辑</a>";
                    head_content += "<span>";
                    if(data.items[i].status == 2){
                        head_content +="<a class=\"btn btn-sm btn-primary\" href=\""+localhostPaht+"/poster/down/0/"+data.items[i].id+"\">取消屏蔽</a>";
                    }else if(data.items[i].status == 1){
                        head_content +="<a class=\"btn btn-sm btn-primary\" href=\""+localhostPaht+"/poster/down/2/"+data.items[i].id+"\">屏蔽</a>";
                    }else{
                        head_content +="<a class=\"btn btn-sm btn-primary\" href=\""+localhostPaht+"/poster/down/1/"+data.items[i].id+"\">发布</a>";
                    }
                    head_content += "</span>";
                    head_content += "<span>";
                    if(data.items[i].isTop == 1){
                        head_content +="<a class=\"btn btn-sm btn-primary\" href=\""+localhostPaht+"/poster/top/0/"+data.items[i].id+"\">取消置顶</a>";
                    }else{
                        head_content +="<a class=\"btn btn-sm btn-primary\" href=\""+localhostPaht+"/poster/top/1/"+data.items[i].id+"\">置顶</a>";
                    }
                    head_content += "</span>";
                    // head_content +="<button th:attr=\"del_uri=@{/poster/}+"+data.items[i].id+"\" class=\"btn btn-sm btn-danger deleteBtn\">删除</button>";
                    head_content +="</td></tr>";
                }
                head_content += "</tbody>";
                $("#t_list_data").append(head_content);
                // for(var i=0;i<data.testStudents.length;i++) {
                    // HTML_CODE+=
                    //     "<tr><td>" + data.testStudents[i].studentId + "</td>" +
                    //     "<td>" + data.testStudents[i].studentName + "</td>" +
                    //     "<td>" + data.testStudents[i].studentClassId + "</td></tr>"
                // }
                // $("#showStudentByPage").html(HTML_CODE);
//显示总条数
                $("#totalNumber").text(data.totalCount);
//显示当前页数
                $("#currentPage").text(data.currentPage);
//显示总页数
                $("#totalPage").text(data.totalPage);


            },
            error: function(){
                alert("连接失败");
            }
        });
    }
});
