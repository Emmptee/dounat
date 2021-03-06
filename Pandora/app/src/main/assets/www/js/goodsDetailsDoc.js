var consumerId = localStorage.getItem("userId");
var vipFlg = localStorage.getItem("vipFlg");
var docUrl=null;
var userId = localStorage.getItem("userId");
var token = localStorage.getItem("token");
var goodsId = localStorage.getItem("goodsId");
var data = null;
var dataH5 = null;
var isInCar = null;
var needMember = null;

var isFromHtml = false;

function onDeviceReady(){
    setUserInfoAction(initLoad);

    document.addEventListener("resume", onResume, false);
}

function onResume() {
    userId = localStorage.getItem("userId");
    if(userId != null && userId != ''){
        $(".goods_discount").hide();
        $(".list_discount").empty();
        $(".gdv_title").empty();
        $(".gdd_priceOrdinary").empty();
        $(".gdd_priceVip").empty();
        $(".gdd_buyTimes").empty();
        $(".docType").empty();
        $(".gdv_detail").empty();
        $(".goods-price").empty();
        onDeviceReady();
    }
}

function goodsActionCalBack()
{
    goodsId = localStorage.getItem("goodsId");
    goodsDetailsDoc();
}

function goodsDetailsDoc(){
    data = '{"header":"00010302","userId":"'+userId+'","token":"'+token+'",'+
        '"data": "{\\"goodsId\\":\\"'+goodsId+'\\",\\"consumerId\\":\\"'+consumerId+'\\"}"}';

    setBehaviourAction("01200", data);
    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json", //必须有
        timeout: 10000,
        dataType:'json',
        url:urlR,
        data:data,
        type:"post",
        success:function(data) {
            //商品活动
            var bot="";
            $.each(data.promotions, function(n,value) {
                if (value.type==0) {
                    $(".goods_discount").show();
                    bot+='<li ><i>折扣</i><em>'+value.rules+'</em></li>'
                }
            });
		    $(".list_discount").append(bot);
            var img = '<div class=""><img src="'+data.thumbnail+'" width="100%" alt="" /> </div>';
            $("#slider").empty().append(img);

            //设置图片所在P标签首行缩进为0
            $('.detail-container-content img').parent('p').css('text-indent', '0');
            //渐显详细内容
            //	$('.loading').delay(700).fadeOut(300);
            $('.detail-container').delay(1).fadeIn(1);
            /*-----------------------------------------------------------*/
            $(".gdd_Title").html(data.description);
            $(".gdd_priceOrdinary").html(data.price);
            $(".gdd_priceVip").html(data.memberPrice);
            $(".gdd_buyTimes").html(data.salesNum);
            $(".docType").html(data.docType.toUpperCase());
            $(".gdd_detail").html(data.mediaText);

            docUrl=data.docHtmlUrl;
            isInCar = data.isInCar;
            needMember = data.needMember;
            var buyFlg = data.isBuy;
            if(buyFlg != 0){
                if ((needMember != 1 && data.price <= 0) ||
                            (userId != null && userId != ''
                                && vipFlg == 1 && data.memberPrice <= 0)){
                    if(data.appPromotion != 1){
                        buyFlg = 0;
                    }
                }
            }

            if(buyFlg==0){
                $(".buy_no").hide();
                $(".buy_yes").show();
            } else {
                $(".buy_no").show();
                $(".buy_yes").hide();
            }

            if(data.appPromotion == 1){
                $(".buy_no").hide();
                $(".shopping").hide();
            }

            var price;
            goodType = 2;
            if(needMember==1||(vipFlg == 1 && data.memberPrice != null))
                price = data.memberPrice;
            else
                price = data.price;

            var warp1="" ;
            if(needMember == 1)
            {
                warp1+='<span class="h_price">会员价：<i class="price_h">¥'+data.memberPrice+'</i></span>';
            }
            else
            {
                if(data.memberPrice!=null&&data.price!=data.memberPrice)
                {
                    if(vipFlg==1){
                          warp1+='<span class="p_price">¥<i class="price_p">'+data.price+'</i></span>';
                          warp1+='<span class="h_price">会员价：<i class="price_h">¥'+data.memberPrice+'</i></span>';

                    } else {
                          warp1+='<span class="pno_price">¥<i class="price_p">'+data.price+'</i></span>'
                          warp1+='<span class="pno_price">会员价：<i class="price_h">¥'+data.memberPrice+'</i></span>';
                    }
                }else
                {
                    warp1+='<span class="pno_price">¥<i class="price_p">'+data.price+'</i></span>'
                }
            }
            $(".goods-price").append(warp1);

            var singlgGoogsInfo={"description":data.description,"price":price,"areaName":data.subjectName,"goodType":goodType,"zhek":data.promotions,"residueNum":data.surplusNum,"thumbnail":data.thumbnail};
            localStorage.setItem("singlgGoogsInfo",JSON.stringify(singlgGoogsInfo));

            $("#loadingDiv").hide();
        },error:function(){
            $("#loadingDiv").hide();
            $("#errorDiv").show();
		    //alert("请求数据失败");
        }
    });

    if(userId == null || userId == ''){
        $(".gdd_priceSum").html("¥ 0");
    } else {

        data = '{"header":"00000307","userId":"'+userId+'","token":"'+token+'",'+
              '"data": "{\\"page\\":\\"0\\",\\"rows\\":\\"'+"0"+'\\"}"}';

        $.ajax({
            contentType: "application/json", //必须有
            timeout: 10000,
            dataType:'json',
            url:urlR,
            data:data,
            type:"post",
            success:function(data) {
                if("0002"==data.code){
                    // 跳转登录页面
               		setLoginAction(data.msg);
                } else if("0000"==data.code){
                    $(".gdd_priceSum").html("¥"+data.totalAmount);
                } else{
                    // 弹框 showMsgAction
                    setShowMsgAction(data.msg);
                }
            },error:function(){
                //alert("购物车数据取得失败");
            }
        });
    }
}

function initLoad(){

    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");

    var fromHtml = getQueryString("fromHtml");
    if(fromHtml == null){
        isFromHtml = false;
        goodsAction(goodsActionCalBack);
    } else {
        isFromHtml = true;
        goodsDetailsDoc();
    }
}
$(document).ready(function(){
    $(".add_email").on("click",function(){
        $(".alertFloat").show();
    });

    $(".close_nav").on("click",function(){
        $(".alertFloat").hide();
    });

    $(".nav_go").on("click",function(){

        var emailAddress = $(".gdd_emailAddress").val()

        if(emailAddress==''){
            setShowMsgAction("请输入邮箱地址");
            return false;
        }

        if(!fChkMail(emailAddress)){
            setShowMsgAction("请输入正确的邮箱地址");
            return false;
        }

        $(".alertFloat").hide();

        data = '{"header":"00000308","userId":"'+userId+'","token":"'+token+'",'+
              '"data": "{\\"email\\":\\"'+emailAddress+'\\",\\"goodId\\":\\"'+goodsId+'\\"}"}';

        setBehaviourAction("01206", data);

        $.ajax({
            contentType: "application/json", //必须有
            timeout: 10000,
            dataType:'json',
            url:urlR,
            data:data,
            type:"post",
            success:function(data) {
                if("0002"==data.code){
                    // 跳转登录页面
                    setLoginAction(data.msg);
                } else if("0000"==data.code){
                    // 弹框 showMsgAction
                    setShowMsgAction("邮件发送成功");
                } else{
                    // 弹框 showMsgAction
                    setShowMsgAction(data.msg);
                }
            },error:function(){
                alert("发送邮件异常");
            }
        });
    });

        //  在线观看
    $(".add_saw").on("click",function (){
         setBehaviourAction("01203");
          // location.href = docUrl;
//         readDocAction(docUrl);
         localStorage.setItem("document_content_url",docUrl);
         location.href = "goods_details_doc_display.html";
    });

});

function fChkMail(szMail){
    var szReg=/^[A-Za-z0-9d]+([-_.][A-Za-z0-9d]+)*@([A-Za-z0-9d]+[-.])+[A-Za-z0-9d]{2,5}$/;
    var bChk=szReg.test(szMail);
    return bChk;
}
// 返回
function back(){
    setBehaviourAction("01207");
//    var urlBack ="more.html";
//    location.href = urlBack;
    if(isFromHtml) {
        history.go(-1);
    } else {
        setBackAction();
    }
}

function onBackKeyDown() {
    back();
}

 // 立即购买
function immediatelyBuy(){

    if(!checkLogin()){
        return false;
    }

    if(needMember=="1"&&!checkMember())
    {
        return false;
    }
  
    setBehaviourAction("01205");
    localStorage.setItem("dataUuidList",'');
    window.location.href = 'singleCloseAnAccount.html';
}

// 添加购物车
var cartFlg = 0;
function addToCart(){

    if(!checkLogin()){
        return false;
    }
    if(needMember=="1"&&!checkMember())
    {
        return false;
    }

	
    if(userId == null || userId == ''){
        //如果未登录，进入登录页面
        // 跳转登录页面
        setLoginAction("未登录，请先登录");
    }else{
        if(cartFlg == 1){
            // 弹框 showMsgAction
            setShowMsgAction("购物车中已经存在该商品");
            return false;
        }
        if(isInCar == 0){
            // 弹框 showMsgAction
            setShowMsgAction("购物车中已经存在该商品");
            return false;
        }
        data = '{"header":"00100304","userId":"'+userId+'","token":"'+token+'",'+
            '"data": "{\\"d02Id\\":\\"'+goodsId+'\\",\\"count\\":\\"'+"1"+'\\"}"}';
        // 已经登录，调用添加购物车接口，添加购物车
        setBehaviourAction("01204", data);

        $.ajax({
            contentType: "application/json", //必须有
            timeout: 10000,
            dataType:'json',
            url:urlW,
            data:data  ,
            type:"post",
            success:function(data) {
                if("0002"==data.code){
                    // 跳转登录页面
                    setLoginAction(data.msg);
                } else if("0000"==data.code){
                	setShowMsgAction("添加成功");
                    $(".gdd_priceSum").html("¥"+data.totalAmount);
                    addToCart=1;
                } else {
                    // 弹框 showMsgAction
                    setShowMsgAction(data.msg);
                }
            },error:function(){
                alert("添加购物车失败");
            }
        });
    }
	
	
}

//点击跳转
function shopping() {
    if(!checkLogin()){
        return false;
    }

    setBehaviourAction("01201");
    var href = 'cart.html?type=detail';
    window.location.href = href;
}