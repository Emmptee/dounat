var vipFlg = localStorage.getItem("vipFlg");
var docUrl=null;
var userId = localStorage.getItem("userId");
var token = localStorage.getItem("token");
var goodsId = localStorage.getItem("goodsId");
var dataH5 = null;
var isInCar = null;
var needMember = null;

var isFromHtml = false;

function onDeviceReady(){

    setUserInfoAction(initLoad);
    document.addEventListener("resume", onResume, false);
}

function initLoad()
{
    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");
    var fromHtml = getQueryString("fromHtml");
    if(fromHtml == null){
        isFromHtml = false;
        goodsAction(goodsActionCalBack);
    } else {
        isFromHtml = true;
        goodsDetailsVideo();
    }

}


function onResume() {
    userId = localStorage.getItem("userId");
    if(userId != null && userId != ''){
        $(".goods_discount").hide();
        $(".list_discount").empty();
        $(".video_yes").empty();
        $(".video_no").show();
        $(".goods-details-footer").show();
        $(".gdv_title").empty();
        $(".gdv_detail").empty();
        $(".goods-video").empty();
        onDeviceReady();
    }
}

function goodsActionCalBack()
{
    goodsId = localStorage.getItem("goodsId");
    goodsDetailsVideo();
}

function goodsDetailsVideo(){
    data = '{"header":"00010302","userId":"'+userId+'","token":"'+token+'",'+
        '"data": "{\\"goodsId\\":\\"'+goodsId+'\\",\\"consumerId\\":\\"'+userId+'\\"}"}';

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
            needMember = data.needMember;
            isInCar = data.isInCar;
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

            var hei = $(window).width()*9/16;
            $(".video_no").height(hei);
            if(buyFlg==0){
                var videoData = '<video id="video1" class="video-js vjs-default-skin" controls width="100%" height="'+hei+'" poster="'+data.thumbnail +'" data-setup="{}"> <source id="videoId" src="'+data.playUrl +'" type="video/mp4" /> </video>';
                $(".video_yes").html(videoData);
                //$("#videoId").attr("src",data.playUrl);
                $(".video_no").hide();
                $(".goods-details-footer").hide();
            } else {
                var videoData = '<video id="video1" class="video-js vjs-default-skin" width="100%" height="'+hei+'" poster="'+data.thumbnail +'" data-setup="{}"> <source id="videoId" src="" type="video/mp4" /> </video>';
                $(".video_yes").html(videoData);
                //$("#video1").attr("poster",data.thumbnail);
                $(".video_no").show();
                $(".goods-details-footer").show();
            }

            if(data.appPromotion == 1){
                $(".goods-details-footer").hide();
                $(".shopping").hide();
            }

            /*  var video = " <source src="+data.playUrl+" type='video/mp4' />"
            $("#video1").empty().append(video);*/

            //设置图片所在P标签首行缩进为0
            $('.detail-container-content img').parent('p').css('text-indent', '0');
            //渐显详细内容
            //	$('.loading').delay(700).fadeOut(300);
            $('.detail-container').delay(1).fadeIn(1);
            /*-----------------------------------------------------------*/
            $(".gdv_title").html(data.description);
            $("#play_num").html(data.playTimes+"次");
            $(".gdv_detail").html(data.mediaText);

        //            $(".gdv_priceOrdinary").html(data.price);
        //            $(".gdv_priceVip").html(data.memberPrice);
        //            $(".gdv_buyTimes").html(data.salesNum);

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

            warp1+='<span class="video_three">销量：<i class="gdv_buyTimes">'+data.salesNum+'</i>次</span>';
            if (data.price > 0) {
                $(".goods-video").append(warp1);
            } else {
                $(".goods-video").hide();
            }
            var price;
            goodType = 1;
            if(vipFlg == 0 || vipFlg == null)
                price = data.price;
            else if (vipFlg == 1)
                price = data.memberPrice;

            var singlgGoogsInfo={"description":data.description,"price":price,"areaName":data.subjectName,
                    "goodType":goodType,"zhek":data.promotions,"residueNum":data.surplusNum,"thumbnail":data.thumbnail};
            localStorage.setItem("singlgGoogsInfo",JSON.stringify(singlgGoogsInfo));

            $("#loadingDiv").hide();
        },error:function(){
//			alert("请求数据失败");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });

    if(userId == null || userId == ''){
        $(".gdv_priceSum").html("¥ 0");
    } else{
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
                    $(".gdv_priceSum").html("¥"+data.totalAmount);
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

/*function getQueryString(name){
var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
var r = window.location.search.substr(1).match(reg);
if (r != null)
return unescape(r[2]);
return null;
}*/


/*-----------------------------------------------------------*/

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
    var data = '{"header":"00100304","userId":"'+userId+'","token":"'+token+'",'+
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
                $(".gdv_priceSum").html("¥"+data.totalAmount);
                addToCart=1;

            } else{
                // 弹框 showMsgAction
                setShowMsgAction(data.msg);
            }
        },error:function(){
            alert("添加购物车失败");
        }
    });
}


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
    location.href = "singleCloseAnAccount.html";
}

//点击跳转
function shopping() {
    if(!checkLogin()){
        return false;
    }
    setBehaviourAction("01207");
    var href = 'cart.html?type=detail';
    window.location.href = href;
}