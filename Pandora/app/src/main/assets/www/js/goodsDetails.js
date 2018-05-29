var goodsId = localStorage.getItem("goodsId");
var vipFlg = 0;
var userId = "";
var token = "";

var dataH5;
var needMember;
var goodsData = null;

var isFromHtml = false;

function onDeviceReady(){
    setUserInfoAction(initLoad);

    document.addEventListener("resume", onResume, false);
}

function onResume() {
    userId = localStorage.getItem("userId");
    if(userId != null && userId != ''){
        $(".goods_discount").hide();
        onDeviceReady();
    }
}

function goodsActionCalBack()
{
    goodsId = localStorage.getItem("goodsId");
    goods();
}

function initLoad() {

    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");

    var fromHtml = getQueryString("fromHtml");
    if(fromHtml == null){
        isFromHtml = false;
        goodsAction(goodsActionCalBack);
    } else {
        isFromHtml = true;
        goods();
    }

    // 购物车价格显示...
	if(userId == null || userId == ''){
        $(".gdd_priceSum").html("¥ 0");
    } else{
        var data2 = '{"header":"00000307","userId":"'+userId+'","token":"'+token+'",'+
              '"data": "{\\"page\\":\\"0\\",\\"rows\\":\\"'+"0"+'\\"}"}';
        $.ajax({
            contentType: "application/json", //必须有
            timeout: 10000,
            dataType:'json',
            url:urlR,
            data:data2,
            type:"post",
            success:function(data) {
                if("0002"==data.code){
                    // 跳转登录页面
                    setLoginAction(data.msg);
                } else if("0000"==data.code){
                    $(".gdd_priceSum").html("¥"+data.totalAmount);
                }
            },
            error:function(){
                //alert("查询购物车失败");
            }
        });
    }

    //购物车点击跳转
    $('.shopping').click(function() {
        if(!checkLogin()){
            return false;
        }
        var href = 'cart.html?type=detail';
        window.location.href = href;
        /*dataH5*/
		setBehaviourAction("01201");
    });

    /*-----------------------立即购买-------------------------*/
    //选择规格购买或加入购物车
    //增减购买数量
    var purchaseNumber = $('.purchase-number .quantity input[type="hidden"]'); //数量
    var minus = $('.purchase-number .quantity span.minus'); //减少
    var plus = $('.purchase-number .quantity span.plus'); //增加
    plus.click(function() {
               if (parseInt(purchaseNumber.val()) >= parseInt($(".img_num").text())) {
               //alert("库存不足");
               // 弹框 showMsgAction
               setShowMsgAction("库存不足");
               plus.attr('disabled', true).addClass('disabled');
               return false;
               }
               purchaseNumber.val(parseInt(purchaseNumber.val()) + 1);
               //               alert(purchaseNumber.val());
               $('.add_num').html(""+purchaseNumber.val());
               minus.removeAttr('disabled').removeClass('disabled');
               });
    
    minus.click(function() {
                if (parseInt(purchaseNumber.val()) == 1) {
                minus.attr('disabled', true).addClass('disabled');
                return false;
                }
                purchaseNumber.val(parseInt(purchaseNumber.val()) - 1);
                //                alert(purchaseNumber.val());
                $('.add_num').html(""+purchaseNumber.val());
                plus.removeAttr('disabled').removeClass('disabled');
                });
    //打开弹出层
    var id = null; //ID
    var alias = null; //别名
    var goodsimg = null; //商品图片
    var title = null; //标题
    var price = null; //价格
    var memPrice = null; //邮费
    $('.buy-response-btn').on('click', function(event) {

        if(!checkLogin()){
            return false;
        }

        if(needMember=="1"&&!checkMember())
        {
            return false;
        }

        $('body').addClass('hidden');
        id = $(this).data('id'); //ID
        setBehaviourAction("01205","{goodsId:"+id+"}");
        alias = $(this).data('alias'); //别名
        goodsimg = $(this).parents().find('.goodsimg').attr('src'); //商品图片
        if(!goodsimg){
            goodsimg = $(this).data('titlepic');
        }
        title = $(this).data('title'); //标题

        $('.popup-goods-thumb').attr('src', $(".xiao_img").html()); //设置弹出层商品小图
        $('.popup-goods-title').html($(".title").html()); //设置弹出层商品标题
        $('input[name="alias"]').val(alias); //设置弹出层商品别名
        $('input[name="goodsid"]').val(id); //设置弹出层商品ID
        $("._price").html($(".price_p").html());
        $("._postage").html($(".price_h").html());
        var price=$(".price_p").html();
        var memPrice=$(".price_h").html();
        $(".img_num").html($(".surplus").html());

        event.preventDefault();
        $('.cd-popup').addClass('is-visible');
              	//判断是不是vip会员
        var warp2="" ;
//        if(vipFlg==1){
//            warp2+='<span class="h_price">会员价：<i class="_postage">'+memPrice+'</i></span>';
//            } else {
//            if(price==null||memPrice==null ){
//            warp2+='<span class="pno_price">会员价：<i class="_postage">'+memPrice+'</i></span>';
//            }else{
//            warp2+='<span class="pno_price">¥<i class="_price">'+price+'</i></span>'
//                        +'<span class="pno_price">会员价：<i class="_memPrice">'+memPrice+'</i></span>';
//            }
//        }
        if(needMember == 1)
        {
            warp2+='<span class="h_price">会员价：'
                    +'¥<i class="price_h">'+memPrice
                    +'</i></span>';
        }
        else
        {
            if(memPrice!=null&&memPrice!=""&&price!=memPrice)
            {
                if(vipFlg==1){
                      warp2+='<span class="p_price">'
                            +'¥<i class="price_p">'+price
                            +'</i></span>'
                      warp2+='<span class="h_price">会员价：'
                              +'¥<i class="price_h">'+memPrice
                              +'</i></span>';
                } else {
                      warp2+='<span class="pno_price">'
                            +'¥<i class="price_p">'+price
                            +'</i></span>'
                      warp2+='<span class="pno_price">会员价：'
                              +'¥<i class="price_h">'+memPrice
                              +'</i></span>';
                }
            }else
            {
                warp2+='<span class="pno_price">'
                        +'¥<i class="price_p">'+price
                        +'</i></span>'
            }
        }


        $(".popup-goods-price").empty().append(warp2);
//      $('.standard-ul label').click(function () {
//      // $('.popup-goods-price .price').html('¥' + $(this).data('price'));
//      });
        return false;
       // 判断登录
    });
    //关闭弹出层
    $('.cd-popup').on('click', function(event) {
        if ($(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup')) {
            $('body').removeClass('hidden');
            event.preventDefault();
            $(this).removeClass('is-visible');
        }
    });
    $(document).keyup(function(event) {
        if (event.which == '27') {
            $('body').removeClass('hidden');
        }
    });

    /*-----------------------立即购买-确定------------------------*/
    
 	 $(".popup-button").click(function(){

        if(!checkLogin()){
            return false;
        }
        if(needMember=="1"&&!checkMember())
        {
            return false;
        }
        if (parseInt(purchaseNumber.val()) > parseInt($(".img_num").text())) {
            setShowMsgAction("库存不足");
            return false;
        }
        localStorage.setItem("number",purchaseNumber.val())
        window.location.href = 'buy.html';
        /*dataH5*/
        setBehaviourAction("01205", data);
 	});

    /*标记返回*/
    /*商品实物*/
    $(".fack_back").click(function(){
//        var url =localStorage.getItem("refer");alert(url);
//        location.href = url;
        onBackKeyDown();
    })

}

function onBackKeyDown() {
    setBehaviourAction("01207");
    // location.href = "more.html";
    if(isFromHtml) {
        history.go(-1);
    } else {
        setBackAction();
    }
}

/*---------------------------实物商品详情------------------------*/
function goods(){
    var warp="";
    var data = '{"header":"00010302","userId":"'+userId+'","token":"'+token+'","data":"{goodsId:'+goodsId+'}"}' ;

    setBehaviourAction("01200", data);
    warp+='<div class="swipe-wrap">';
    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json", //必须有
        timeout: 10000,
        dataType:'json',
        url:urlR,
        data: data,
        type:"post",
        success:function(data) {
            try{
            //商品活动
            if(data.code!="0000")
            {
                setShowMsgAction(data.msg);
                return;
            }
            var bot="";
            $.each(data.promotions, function(n,value) {
                if (value.type==0) {
                $(".goods_discount").show();
                bot+='<li ><i>折扣</i><em>'+value.rules+'</em></li>'
                }
            });
            $(".list_discount").empty().append(bot);
			needMember = data.needMember;
            $.each(data.imgUrls, function(n,value) {
                warp+='<figure><div class="top_img"><img src="'+value+'" width="100%" alt="" draggable="false" /> </div></figure>';
            });

            warp+=' </div> <nav> <ul id="banner-nav"><li class="on"></li><li class=""></li><li class=""></li></ul></nav>'
            $("#slider").empty().append(warp);
            //轮播图
            var bullets = document.getElementById('banner-nav').getElementsByTagName('li');

            var slider =Swipe(document.getElementById('slider'), {
                auto: 3000,
                continuous: true,
                callback: function(pos) {
                var i = bullets.length;
                while (i--) {
                    bullets[i].className = '';
                }
                bullets[pos].className = 'on';
                }
            });

            //设置图片所在P标签首行缩进为0
            $('.detail-container-content img').parent('p').css('text-indent', '0');

            //渐显详细内容
            //	$('.loading').delay(700).fadeOut(300);
            $('.detail-container').delay(1).fadeIn(1);
            /*-----------------------------------------------------------*/
            $(".title").html(data.description);
            var warp1="" ;

              if(data.needMember==1)
              {
                  $(".cost_money").html(data.memberFreight == 0||data.memberFreight==null ? (data.postageStatus == 1 ? "邮费到付":"免邮") : (data.memberFreight + "元"));
                  warp1+='<span class="h_price">会员价：'
                        +'¥<i class="price_h">'+data.memberPrice
                        +'</i></span>';
              }
              else
              {
                  if(vipFlg==1){
                      if(data.memberFreight!=null)
                      {
                        $(".cost_money").html(data.memberFreight == 0 ? (data.postageStatus == 1 ? "邮费到付":"免邮") : (data.memberFreight + "元"));
                      }else{
                        $(".cost_money").html(data.freight == 0 ? (data.postageStatus == 1 ? "邮费到付":"免邮") : (data.freight + "元"));
                      }
                      if(data.memberPrice!=null&&data.price!=data.memberPrice)
                      {
                         warp1+='<span class="p_price">'
                               +'¥<i class="price_p">'+data.price
                               +'</i></span>'
                         warp1+='<span class="h_price">会员价：'
                                 +'¥<i class="price_h">'+data.memberPrice
                                 +'</i></span>';
                      }else
                      {
                        warp1+='<span class="pno_price">'
                               +'¥<i class="price_p">'+data.price
                               +'</i></span>'
                      }
                  }else {
                     $(".cost_money").html(data.freight == 0||data.freight==null ? (data.postageStatus == 1 ? "邮费到付":"免邮") : (data.freight + "元"));

                     warp1+='<span class="pno_price">'
                            +'¥<i class="price_p">'+data.price
                            +'</i></span>'
                     if(data.memberPrice!=null&&data.price!=data.memberPrice)
                     {
                        warp1+='<span class="pno_price">会员价：'
                                 +'¥<i class="price_h">'+data.memberPrice
                                 +'</i></span>';
                     }
                 }
            }
    	    $(".goods-price").empty().append(warp1);
            $(".buy_times").html(data.salesNum);
            $(".surplus").html(data.surplusNum);
            $(".logistics").html(data.logistics);
            $(".detail-container").html(data.mediaText);
            $(".xiao_img").html(data.thumbnail);
            $(".xiao_sub").html(data.subjectName);
            $(".xiao_type").html(data.type);
            $(".xiao_goodId").html(goodsId);
            /*------------------------存储到 LocalStorage---------------------------------*/
            var goods={"needMember":data.needMember,"title":data.description,"price_p":data.price,"price_h":data.memberPrice,
                        "cost_money":data.freight,"cost_vip":data.memberFreight,"postageStatus":data.postageStatus,
                        "buy_times":data.salesNum,"surplus":data.surplusNum,"logistics":data.logistics,
                        "detail-container":data.mediaText,"xiao_img":data.thumbnail,"xiao_sub":data.subjectName,
                        "xiao_type":data.type,"zhek":data.promotions,"xiao_goodId":goodsId};
            localStorage.setItem("good",JSON.stringify(goods));

//判断产品的剩余量是否是0;
            if(data.surplusNum==0){
                $(".goods-details-footer").hide();
                $(".goods-no").show();
            } else {
                $(".goods-details-footer").show();
                $(".goods-no").hide();
            }

            if(data.appPromotion == 1){
                $(".goods-details-footer").hide();
                $(".goods-no").hide();
                $(".shopping").hide();
            }

    }catch(e)
    {
    // alert(e);
    }
            $("#loadingDiv").hide();
        },error:function(){
            $("#loadingDiv").hide();
            $("#errorDiv").show();
            //alert("请求数据失败");
        }
    });
}

// 添加购物车
function addToCart(){

    if(!checkLogin()){
        return false;
    }
    if(needMember=="1"&&!checkMember())
    {
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
        data:data,
        type:"post",
        async: false,
        success:function(data) {
            if("0002"==data.code){
                // 跳转登录页面
                setLoginAction("账户信息已过期，请重新登录");
            } else if("0000"==data.code){
                setShowMsgAction("添加成功");
                 $(".gdd_priceSum").html("¥"+data.totalAmount);
            } else{
                 // 弹框 showMsgAction
                  setShowMsgAction(data.msg);
            }
        },
        error:function(){
            alert("添加购物车失败");
        }
    });
}
