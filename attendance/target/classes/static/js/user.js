$(function(){
    $.ajax({
        type:"POST",
        url:"userinfo",
        dataType:"json",
        contentType:"application/json",
        data:{},
        success:function (data) {
        	console.log(data);
            $(".user_head_img").attr("src",data.headImage);
            $(".user_name").html(data.username);
        }
    });
  /*  $.ajax({
        type:"POST",
        url:"userinfo",
        dataType:"json",
        contentType:"application/json",
        data:{},
        success:function (data) {
        	console.log(data);
            $(".user_head_img").attr("src",data.headImage);
            $(".user_name").html(data.realName);
        }
    });*/
});