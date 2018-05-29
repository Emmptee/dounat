var goodsId = localStorage.getItem("goodsId");
var vipFlg = 0;
var userId = "";
var token = "";
var userBindingPhoneStatus = 0;
var dataH5;
var needMember;
var goodsData = null;

var isFromHtml = false;

document.addEventListener("deviceready", onDeviceReady, false);
document.addEventListener("backbutton", onBackKeyDown, false);
function onDeviceReady(){
    setUserInfoAction(getGoodsId);
    document.addEventListener("resume", onResume, false);
}

function onResume() {
    userId = localStorage.getItem("userId");
    if(userId != null && userId != ''){
        onDeviceReady();
    }
}

function getGoodsId()
{
    var fromHtml = getQueryString("fromHtml");
    if(fromHtml == null){
        isFromHtml = false;
        goodsAction(initLoad);
    } else {
        isFromHtml = true;
        initLoad();
    }
}

function initLoad() {
	
    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");
	goodsId = localStorage.getItem("goodsId");

	setBehaviourAction("08200", '{"header":"00010314","userId":"'+userId+'","token":"'+token+'","data":"{goodsId:'+goodsId+'}"}' );
    /*-----------------------立即购买-------------------------*/
    //选择规格购买或加入购物车
    //增减购买数量
    var purchaseNumber = $('.purchase-number .quantity input[type="hidden"]'); //数量
    var minus = $('.purchase-number .quantity span.minus'); //减少
    var plus = $('.purchase-number .quantity span.plus'); //增加
    plus.click(function() {
	    	       var num = parseInt(purchaseNumber.val());
	    	       var money_one_time =  $('.money_one_time').html();
	    	       var max_price =  $('.max_price').html();
		           var nowPrice = $('.price_p').html();
	               nowPrice = (num + 1)*money_one_time + nowPrice*1;
	               if(nowPrice<=max_price)
	               {
		               purchaseNumber.val(num + 1);
		               $('.add_num').html(num + 1);
		               $('.pop_price_p').html(nowPrice.toFixed(2));
	               }
	               else
	               {
		        		setShowMsgAction("此价格已为最高竞拍价格");
	               }
               });
    
    minus.click(function() {
	          var num = parseInt(purchaseNumber.val());
	          if(num>1)
	          {
                  purchaseNumber.val(num - 1);
           		  $('.add_num').html(num - 1);
	    	      var money_one_time =  $('.money_one_time').html();
	              var nowPrice = $('.price_p').html();
	              nowPrice = (num - 1)*money_one_time + nowPrice*1;
	              $('.pop_price_p').html(nowPrice.toFixed(2));
	          }
                });
    //打开弹出层
    var id = null; //ID
    var alias = null; //别名
    var goodsimg = null; //商品图片
    var title = null; //标题
    var price = null; //价格
    var memPrice = null; //邮费
    
    $('.auction_title_all').on('click', function(event) {
        setBehaviourAction("08203","{goodsId:"+goodsId+"}");
        window.location.href = 'auctionRecord.html?goodsId='+goodsId;
    });
    
    $('.buy-response-btn').on('click', function(event) {
      
	    if(!buyCheck())
        {
        	return false;
        }
        
        $('body').addClass('hidden');
        id = $(this).data('id'); //ID
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
        var price=$(".price_p").html();
        $('.cd-popup').addClass('is-visible');
	    var money_one_time =  $('.money_one_time').html();
   		var purchaseNumber = $('.purchase-number .quantity input[type="hidden"]'); //数量
   		var maxPrice = money_one_time*purchaseNumber.val()+price*1;
        $(".pop_price_p").empty().append(maxPrice.toFixed(2));
        return false;
       // 判断登录
    });
    //关闭弹出层
    $('.cd-popup').on('click', function(event) {
        if ($(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup')) {
            $('body').removeClass('hidden');
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

        if(!buyCheck())
        {
        	return false;
        }
        

        var purchaseNumber = $('.purchase-number .quantity input[type="hidden"]'); 
	    var data = '{"header":"00100315","userId":"'+userId+'","token":"'+token+'","data":"{goodsId:'+goodsId+',auctionBeforePrice:'+$(".price_p").html()+',auctionAddTimes:'+purchaseNumber.val()+'}"}' ;
            setBehaviourAction("08201",data);
	    $("#loadingDiv").show();
	    $.ajax({
	        contentType: "application/json", //必须有
	        timeout: 10000,
	        dataType:'json',
	        url:urlW,
	        data: data,
	        type:"post",
	        success:function(data) {
		        $('.cd-popup').click();
		        $("#loadingDiv").hide();
	        	if("0505"==data.code)
	        	{
	        		setBehaviourAction("08202",data);
				layer.open({
			            content: '恭喜您以最高价拍得商品！',
			            btn: ['去支付', '取消'],
			            yes: function(index){
							layer.closeAll();
			    			window.location.href = 'payAuctionGoods.html?d10Id='+data.d10Id;
			            },
			            no: function(index) {
			              layer.closeAll();
			            }
			         });
	        	}else if("0000"==data.code)
	        	{
	        		setShowMsgAction("竞拍成功");
	        	}else
	        	{
	        		setShowMsgAction(data.msg);
	        	}
				loadGoodsDetail();
	        }
	    });
 	});

    /*标记返回*/
    /*商品实物*/
    $(".fack_back").click(function(){
//        var url =localStorage.getItem("refer");alert(url);
//        location.href = url;
        onBackKeyDown();
    })
	loadGoodsDetail();
}

function addEventAction()
{
	
}

function onBackKeyDown() {
    setBehaviourAction("08204");
    if(isFromHtml) {
        history.go(-1);
    } else {
        setBackAction();
    }
}

/*---------------------------实物商品详情------------------------*/
function loadGoodsDetail(){
    var warp="";
    var data = '{"header":"00010314","userId":"'+userId+'","token":"'+token+'","data":"{goodsId:'+goodsId+'}"}' ;

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
			needMember = data.needMember;
			userBindingPhoneStatus = data.userBindingPhoneStatus;
            $.each(data.imgUrls, function(n,value) {
                warp+='<figure><img src="'+value+'" width="100%" alt="" draggable="false" /></figure>';
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
            $('.auction_status').hide();
            $('.auction_button').hide();
            if(data.auctionStatus == 0)
            {
            	$('.auction_status_doing').show();
            	$('.auction_button_doing').show();
            }else if(data.auctionStatus == 1)
            {
            	$('.auction_status_accounting').show();
            	$('.auction_button_done').show();
            }else  if(data.auctionStatus == 2)
            {
            	$('.auction_status_done').show();
            	$('.auction_button_done').show();
            	$('.auction_result').show();
	            $(".success_arCont_title").html(data.successNickName);
	    	    $(".auction_success_pic").attr("src",data.successHeadPic);
	            $(".auction_success_price").html(data.successAuctionResultPrice);
            	
            }else  if(data.auctionStatus == 3)
            {
            	$('.auction_status_done').show();
            	$('.auction_button_done').show();
            	
            }else
            {
            	$('.auction_button_nostart').show();
            }
            
            $(".title").html(data.description);
            if(vipFlg==1){
				if(data.memberFreight!=null)
				{
					$(".cost_money").html(data.memberFreight == 0 ? (data.postageStatus == 1 ? "邮费到付":"免邮") : (data.memberFreight + "元"));
				}else{
					$(".cost_money").html(data.freight == 0 ? (data.postageStatus == 1 ? "邮费到付":"免邮") : (data.freight + "元"));
				}
            }else {
                $(".cost_money").html(data.freight == 0||data.freight==null ? (data.postageStatus == 1 ? "邮费到付":"免邮") : (data.freight + "元"));
            }
            $(".money_one_time").html(data.auctionStepLength);
    	    $(".price_p").empty().append(data.auctionCurrentPrice);
            $(".buy_pep_num").html(data.auctionUserNum);
            $(".logistics").html(data.logistics);
            $(".detail-container").html(data.mediaText);
            $(".xiao_type").html(data.type);
            $(".xiao_goodId").html(getQueryString("goodsId"));
            $(".xiao_img").html(data.thumbnail);
            $(".auction_finish_time").html(data.auctionTime);
            $(".max_price").html(data.auctionStopPrice);
            
            if(data.auctionLogsList == null || data.auctionLogsList.length ==0){
           		$(".price_start_title").html("起拍价格");
           		$(".auction_items_title").hide();
            }else{
           		$(".price_start_title").html("当前价格");
           		$(".auction_items_title").show();
           		$(".auction_items").empty();
           		if(data.auctionLogsList.length<3)
           		{
           			$(".auction_title_all").hide();
           		}else{
           			$(".auction_title_all").show()
           		}
	            $.each(data.auctionLogsList, function(index,data) {
	            	var headPic = "";
	            	if(data.headPic!=null&&data.headPic!="")
	            	{
	            		headPic="<img src=\""+data.headPic+"\"></img>";
	            	}else
	            	{
	            		headPic="<img src=\"images/default_head.png\"></img>";
	            	}
	            	
	            	var nickName = "小麦穗";
	            	if(data.nickName!=null&&data.nickName!="")
	            	{
	            		nickName=data.nickName;
	            	}
		            var item = "<li>"+
									"<span class=\"ar_img\">"+
									"	<i class=\"arImg\">"+headPic+"</i>"+
									"</span>"+
									"<div class=\"ar_cont\">"+
										"<span class=\"arCont_title\">"+nickName+"</span>"+
										"<span class=\"arCont_time\">"+data.createTime+"</span>"+
									"</div>"+
									"<span class=\"ar_money\">¥<i>"+data.auctionResultPrice+"</i></span>"+
								"</li>";
					$(".auction_items").append(item);
	            });
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

function buyCheck()
{
    if(!checkLogin()){
        return false;
    }
    if(needMember=="1"&&!checkMember())
    {
        return false;
    }
    
    if(userBindingPhoneStatus != 1)
    {
    	bindingPhone();
    	return false;
    }
    return true;
}


