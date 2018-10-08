$(function () {
    $.ajax({
        url:"http://localhost:8085/sso/islogin",
        dataType:"jsonp"
    })
});


function callback(data){
    if(data!=null){
        $("#pid").html(data.username+" 您好，欢迎来到<b>ShopCZ商城</b>&nbsp;&nbsp;<a href='http://localhost:8085/sso/logout'>注销</a>");
    }else{
        $("#pid").html(" [<a href='javascript:login();'>登录</a>][<a href=\"\">注册</a>]</p>");
    }
};

function login(){
    var returnUrl = location.href;
    //转码，tomcat那边自动转码
    returnUrl= encodeURI(returnUrl);
    returnURl = returnUrl.replace("&","*");
    //再把当前的url携带过去到tologin那里
    //alert(returnUrl);
    location.href="http://localhost:8085/sso/tologin?returnUrl="+returnUrl;
};