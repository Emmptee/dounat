var goodsId = localStorage.getItem("goodsId");
var userId = localStorage.getItem("userId");
var token = localStorage.getItem("token");
var vipFlg = localStorage.getItem("vipFlg");
var dataH5;

$(document).ready(function() {
    document.addEventListener("deviceready", goods, false);
    // goods();
	if(userId == null || userId == ''){
        $(".gdd_priceSum").html(0);
    } else{
        var data2 = '{"header":"00000307","userId":"'+userId+'","token":"'+token+'",'+
              '"data": "{\\"page\\":\\"0\\",\\"rows\\":\\"'+"0"+'\\"}"}';
        $.ajax({
            contentType: "application/json", //必须有
            dataType:'json',
            url:urlR,
            data:data2,
            type:"post",
            success:function(data) {
                if("0002"==data.code){
                    // 跳转登录页面
                    setLoginAction(data.msg);
                } else if("0000"==data.code){
                    $(".gdd_priceSum").html(data.totalAmount);}
            },
            error:function(){
                //alert("查询购物车失败");
            }
        });
    }

    $(".address-get_btn").click(function(){
        $(".my_address").find(".address_detail em").html("");
        $(".address-get_btn").empty().append('<span class="get_btn"></span>');

        $(this).prev().find(".address_detail em").html("[默认地址]");
        $(this).find("span").removeClass("get_btn").addClass("get_btn_red");
        $(this).find("span").append("<i></i>");

        dataH5 = '{"h5Id":"01401","data":"'+data+'"}';
        setBehaviourAction(dataH5);
    });

    //点击跳转
    $('.shopping').click(function() {
        var href = 'cart.html?type=detail';
        window.location.href = href;
        /*dataH5*/
		var data='{type:'+type+',page:'+pageIndexArr[type]+',rows:10}';
        dataH5 ='{"h5Id":"01201","data":"'+data+'"}';
		setBehaviourAction(dataH5);
    });
	
    //底部菜单
    $('.nav-item').click(function() {
        $(this).children('.footer-submenu').slideToggle(300).parents('.nav-item').siblings('.nav-item').children('.footer-submenu').hide();
    });
    $(document).click(function(e) {
        if (e.target.className != 'footer-menu-text' && e.target.className != 'footer-menu') {
            $('.footer-submenu').hide();
        }
    });

    //选择规格购买或加入购物车
    //增减购买数量
    var purchaseNumber = $('.purchase-number .quantity input[type="text"]'); //数量
    var minus = $('.purchase-number .quantity button[type="button"].minus'); //减少
    var plus = $('.purchase-number .quantity button[type="button"].plus'); //增加
    plus.click(function() {
        if (parseInt(purchaseNumber.val()) >=parseInt($(".img_num").text())) {
            //alert("库存不足");
            // 弹框 showMsgAction
            setShowMsgAction("库存不足");
            plus.attr('disabled', true).addClass('disabled');
            return false;
        }
        purchaseNumber.val(parseInt(purchaseNumber.val()) + 1);     
        minus.removeAttr('disabled').removeClass('disabled');
    });

    minus.click(function() {
        if (parseInt(purchaseNumber.val()) == 1) {
            minus.attr('disabled', true).addClass('disabled');
            return false;
        }
        purchaseNumber.val(parseInt(purchaseNumber.val()) - 1);
        plus.removeAttr('disabled').removeClass('disabled');
    });
    //打开弹出层
    var id = null; //ID
    var alias = null; //别名
    var goodsimg = null; //商品图片
    var title = null; //标题
    var price = null; //价格
    var postage = null; //邮费
    $('.buy-response-btn').on('click', function(event) {
        if(userId == null || userId == ''){
           // 跳转登录页面
           setLoginAction();
        }

        /*dataH5*/
        dataH5 ='{"h5Id":"01205"}';
        setBehaviourAction(dataH5);

        $('body').addClass('hidden');
        id = $(this).data('id'); //ID
        alias = $(this).data('alias'); //别名
        goodsimg = $(this).parents().find('.goodsimg').attr('src'); //商品图片
        if(!goodsimg){
            goodsimg = $(this).data('titlepic');
        }
        title = $(this).data('title'); //标题
        price = $(this).data('price'); //价格
        postage = $(this).data('postage'); //邮费

        $('.popup-goods-thumb').attr('src', $(".xiao_img").html()); //设置弹出层商品小图
        $('.popup-goods-title').html($(".title").html()); //设置弹出层商品标题
        $('.popup-goods-price').html('<span class="price">¥<i class="_price">' + price + '</i></span><span class="postage">会员价' + '¥<i class="_postage">' + postage + '</i></span>'); //设置弹出层商品价格
        $('input[name="alias"]').val(alias); //设置弹出层商品别名
        $('input[name="goodsid"]').val(id); //设置弹出层商品ID
        $("._price").html($(".price_p").html());
        $("._postage").html($(".price_h").html());
        $(".img_num").html($(".surplus").html());
        $("._postage").html($(".price_h").html());
        event.preventDefault();
        $('.cd-popup').addClass('is-visible');
        $('.standard-ul label').click(function () {
            $('.popup-goods-price .price').html('¥' + $(this).data('price'));
        });
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
    
    /*-----------------------立即购买-------------------------*/
    
    $(".popup-button").click(function(){
        if(userId == null || userId == ''){
            //如果未登录，进入登录页面
            // 跳转登录页面
            setLoginAction();
        } else {
            //    var sm_img = $(".popup-goods-thumb").attr("src");
            //  var sm_title=encodeURI(encodeURI($(".popup-goods-title").text()));
            //  var sm_price=$("._price").val();
            //  var sm_postage=$("._postage").text();
            // var sm_number=$(".number").val();
            //  var sm_sub=encodeURI(encodeURI($(".xiao_sub").text()));
            //  var sm_yf=$(".cost_money").text();
            //  var sm_goodsId=$(".xiao_goodId").text();
            //  var sm_type=$(".xiao_type").text();
            //  var url = 'buy.html?img='+sm_img+'&title='+sm_title+'&price='+sm_price+'&postage='+sm_postage+                  '&number='+sm_number+'&sub='+sm_sub+'&yf='+sm_yf+'&goodId='+sm_goodsId+'&type='+sm_type;
            localStorage.setItem("number",purchaseNumber.val())
            var url = 'buy.html';
            window.location.href = url;
            /*dataH5*/
            dataH5 ='{"h5Id":"01300","data":"'+data+'"}';
		    setBehaviourAction(dataH5);
        }
    });

    var addressUuidDefault = null;
    var consignee = null;
    var phone = null;
    var address = null;
    $(document).on("click","#address-handle",function(){
        gotoAddressPage();
    });
    location.href.substring(location.href.indexOf("img="));
    location.href.substring(location.href.indexOf("img="));
    var h_price;
    if(vipFlg==0){
        h_price='<span class="vprice">¥'+v_price+'</span>';
    }else if(vipFlg==1){
        h_price='<span class="vprice">¥'+v_postage+'</span>';
    }
    var sm_swarp="";
    sm_swarp+='<div class="goods_title_t"><span>'+v_sub+'</span><em style="padding:0 3px">/</em><i class="v_type"></i></div>'
        +' <ul class="block-list-cart">'
        +' <li class="block-item block-item-cart" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+v_img+'" alt="" draggable="false" />'
        + '<div class="detail" >';
    if(vipFlg==1){
        sm_swarp+= '<h3 class="goods-title ellipsis"><span class="if_vip">会员专享</span>'+v_title+'</h3>';
    }else{
        sm_swarp+= '<h3 class="goods-title ellipsis">'+v_title+'</h3>';
    }
    sm_swarp+= '<div class="price-num ellipsis">'+h_price+'<span class="num">×'+v_number+'</span></div>'
       + ' </div>'
       +'</li>'
       +'</ul>';

    if(v_type==0){
        sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">快递</span>'+v_yf+'</span></div>';
    }else if(v_type==1||v_type==2){
        sm_swarp+='<div class="freight">在线观看<span class="price"><span style="padding-right:5px;">快递</span>'+v_yf+'</span></div>';
    }

    $(".goods-order").append(sm_swarp);
    $(".has_yf").html(v_yf);

    if(vipFlg==0){
        var sum = (parseFloat(v_price)*v_number+parseFloat(v_yf)).toFixed(3);
    }else if(vipFlg==1){
        var sum = (parseFloat(v_postage)*v_number+parseFloat(v_yf)).toFixed(3);
    }

    $(".sum_money").html(sum);
    // dressList();

    // 取得默认地址
    var addressUuid = localStorage.getItem("addressUuid");
    if(addressUuid == null || addressUuid == ''){
        var warp_address="";
        var addressId = getQueryString("addressId");
        var data='{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}';

        $.ajax({
            contentType: "application/json", //必须有
            dataType:'json',
            url:urlR,
            async: false,
            data:'{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}',
            type:"post",
            success:function(data) {

                /*-----解析购物车------*/
                $.each(data.deliveryAddressList, function(n,value) {
                    if (value.isDefault==1) {
                        addressUuidDefault = value.uuid;
                        consignee = value.consignee;
                        phone = value.phone;
                        address = value.address;
                        addressUuid = addressUuidDefault;
                    }
                });
            },error: function(){
                alert("收货地址列表请求数据失败");
            }
        });
    }

    // 取得默认地址结束
    if(v_type==0){
        if(addressUuid != null && addressUuid != ''){
            if(localStorage.getItem("consignee") != null && localStorage.getItem("consignee") !='')
                consignee = localStorage.getItem("consignee");
            if(localStorage.getItem("phone") != null && localStorage.getItem("phone") !='')
                phone = localStorage.getItem("phone");
            if(localStorage.getItem("address") != null && localStorage.getItem("address") !='')
                address = localStorage.getItem("address");

            var my_dress =' <section class="address-handle section serv-btn" id="address-handle">'+
                '<span class="uuid" style="display:none;">'+addressUuid+'</span>'+
                '<a href="javascript:void(0);" onclick="gotoAddressPage()"><span class="arrow-left" style="top:44%"></span></a>'+
                '<div class="wuliu-div">收货人:<span class="consignee2">'+consignee+'</span></div>'+
                '<div class="phone">'+phone+'</div><div class="clear"></div>'+
                '<div class="address_detail"><div class="wuliu-dive textOverflow2"><span class="address2">'+address+'</span></div></div></section>'+
                '<div class="add_bg"></div>'+
                '<div class="borderD"></div>';
            $(".buy_address").append(my_dress);
            localStorage.removeItem("addressUuid");
            localStorage.removeItem("consignee");
            localStorage.removeItem("phone");
            localStorage.removeItem("address");
        } else {
            $.ajax({
                contentType: "application/json", //必须有
                dataType:'json',
                url:urlR,
                data:'{"header":"00000511","userId":"'+userId+'","token":"'+token+'","data":"{}"}'  ,
                type:"post",
                success:function(data) {
                    //alert(data.addressId);
                    if (data.addressId== null || data.addressId=='') {
                        layer.open({
                            content: '你还没有设置收货地址,请点击这里设置！',
                            btn: ['确定', '取消'],
                            yes: function(index){
                                location.href='add_address.html';
                            },
                            no: function(index) {
                                location.href='goods-details.html?fromHtml=true';
                            }
                        });
                        $("#down_close").click(function(){
                            layer.closeAll();
                        })
                    } else{
                        var my_dress="";
                        my_dress+=' <section class="address-handle section serv-btn" id="address-handle" data-link="">'+
                            '<span class="uuid" style="display:none;">'+data.addressId+'</span>'+
                            '<a href="javascript:void(0);" onclick="gotoAddressPage()"><span class="arrow-left" style="top:44%"></span></a>'+
                            '<div class="wuliu-div">收货人:<span class="consignee2">'+data.consignee+'</span></div>'+
                            '<div class="phone">'+data.phone+'</div><div class="clear"></div>'+
                            '<div class="address_detail"><div class="wuliu-dive textOverflow2"><span class="address2">'+data.address+'</span></div></div></section>'+
                            '<div class="add_bg"></div><div class="borderD"></div>';
                        $(".buy_address").append(my_dress);
                    }
                },
                error: function(){}

            });
        }
    }
    /*--------------判断实物、片花、数据---------*/
    if (v_type==0) {
        $(".v_type").html("实物商品");
    } else if(v_type==1) {
        $(".v_type").html("视频商品");
    } else {
        $(".v_type").html("文档商品");
    }

});

//加入购物车
/*$(rt').click(function (){
	$.post('/Shopping/cart.html', {
		'id': $(this).data('id'),
		'operation': 'add'
	}, function () {
		alertFloat('加入购物车成功');
	});
});*/


//弹出浮层
function alertFloat(title) {
	if (title) {
		$("#show_mes").html(title);
	}
	$('body').addClass('hidden');
	$("#alertFloat").show(0);
	setTimeout(function () {
		$("#alertFloat").hide(0);
		$('body').removeClass('hidden');
	}, 1500);
}

/*---------------------------实物商品详情------------------------*/
function goods(){	
    var warp="";
    var data='{"header":"00010302","data":"{goodsId:'+goodsId+'}"}' ;
    dataH5 = '{"h5Id":"01200","data":"'+data+'"}';
    setBehaviourAction(dataH5);
    warp+='<div class="swipe-wrap">';
    $.ajax({
        contentType: "application/json", //必须有
        dataType:'json',
        url:urlR,
        data:'{"header":"00010302","data":"{goodsId:'+goodsId+'}"}'  ,
        type:"post",
        success:function(data) {

            /*-----解析购物车------*/
            //			sm_type = data.type;
            //			alert(data);
            //			alert(data.type);
            $.each(data.imgUrls, function(n,value) {
                warp+='<figure><div class="top_img"><img src="'+value+'" width="100%" alt="" draggable="false" /> </div></figure>';
            });
            warp+=' </div> <nav> <ul id="banner-nav"><li class="on"></li><li class=""></li><li class=""></li></ul></nav>'
            $("#slider").empty().append(warp);
            //轮播图
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
            var bullets = document.getElementById('banner-nav').getElementsByTagName('li');

            //设置图片所在P标签首行缩进为0
            $('.detail-container-content img').parent('p').css('text-indent', '0');

            //渐显详细内容
            //	$('.loading').delay(700).fadeOut(300);
            $('.detail-container').delay(1).fadeIn(1);
            /*-----------------------------------------------------------*/
            $(".title").html(data.description);
            $(".price_p").html(data.price);
            $(".price_h").html(data.memberPrice);
            $(".cost_money").html(data.memberFreight);
            $(".buy_times").html(data.salesNum);
            $(".surplus").html(data.surplusNum);
            $(".logistics").html(data.logistics);
            $(".detail-container").html(data.mediaText);
            $(".xiao_img").html(data.thumbnail);
            $(".xiao_sub").html(data.subjectName);
            $(".xiao_type").html(data.type);
            $(".xiao_goodId").html(getQueryString("goodsId"));
            /*------------------------存储到 LocalStorage---------------------------------*/
            var goods={"title":data.description,"price_p":data.price,"price_h":data.memberPrice,"cost_money":data.memberFreight,"buy_times":data.salesNum,"surplus":data.surplusNum,"logistics":data.logistics,"detail-container":data.mediaText,"xiao_img":data.thumbnail,"xiao_sub":data.subjectName,"xiao_type":data.type,"xiao_goodId":getQueryString("goodsId")};
            localStorage.setItem("good",JSON.stringify(goods));

        },error:function(){
            //alert("请求数据失败");
        }
    });

}
/*---------------------加入购物车-------------------------*/

//function gotoBuy(){
//   var sm_img = $(".popup-goods-thumb").attr("src");
//   var sm_title=encodeURI(encodeURI($(".popup-goods-title").text()));
//   var sm_price=$("._price").val();
//   var sm_postage=$("._postage").text();
//   var sm_number=$(".number").val();
//   var sm_sub=encodeURI(encodeURI($(".xiao_sub").text()));
//   var sm_yf=$(".cost_money").text();
//   var sm_goodsId=$(".xiao_goodId").text();
//   var sm_type=$(".xiao_type").text();
//   var url = 'buy.html?img='+sm_img+'&title='+sm_title+'&price='+sm_price+'&postage='+sm_postage+
//                      '&number='+sm_number+'&sub='+sm_sub+'&yf='+sm_yf+'&goodId='+sm_goodsId+'&type='+sm_type;
//   window.location.href = url;
//}

/*---------------------------------------------------*/
//var v_img=getQueryString("img");
//var v_title=decodeURI(getQueryString("title"));
//var v_price=getQueryString("price");
//var v_postage=getQueryString("postage");
//var v_number=getQueryString("number");
//var v_sub=decodeURI(getQueryString("sub"));
//var v_yf=getQueryString("yf");
//var v_type=getQueryString("type");
//var v_goodsId=getQueryString("goodId");
//console.log(v_title);

//var goods={"title":data.description,"price_p":data.price,"price_h":data.memberPrice,"cost_money":data.memberFreight,"buy_times":data.salesNum,
//"surplus":data.surplusNum,"logistics":data.logistics,"detail-container":data.mediaText,"xiao_img":data.thumbnail,"xiao_sub":data.subjectName,"xiao_type":data.type,"xiao_goodId":getQueryString("goodsId")};
var v_img=JSON.parse(localStorage.getItem("good")).xiao_img; 
var v_title=JSON.parse(localStorage.getItem("good")).title;
var v_price=JSON.parse(localStorage.getItem("good")).price_p;
var v_postage=JSON.parse(localStorage.getItem("good")).price_h;
var v_number=JSON.parse(localStorage.getItem("good")).buy_times;
var v_sub=JSON.parse(localStorage.getItem("good")).title;
var v_yf=JSON.parse(localStorage.getItem("good")).cost_money;
var v_type=JSON.parse(localStorage.getItem("good")).xiao_type;
var v_goodsId=JSON.parse(localStorage.getItem("good")).xiao_goodId;
var v_number = localStorage.number;

function gotoAddressPage(){
    var addressUuid = $("#address-handle").find(".uuid").text().trim();
    var url = 'my_address.html?addressId='+addressUuid;
    var data='{"header":"00000511","userId":"'+userId+'","token":"'+token+'","data":"{}"}';

    /*dataH5*/
    dataH5 ='{"h5Id":"01400","data":"'+data+'"}';
    setBehaviourAction(dataH5);

    dataH5 ='{"h5Id":"01303","data":"'+data+'"}';
    setBehaviourAction(dataH5);
    location.href = url;
}

/*-------------获取url参数--------------*/
function getQueryString(name){
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}

/*---------------------------确认付款-----------------------------*/
function confirmPayGoods(){

	var pay_reasonType ="1";
	var pay_goodsNum=$(".num").text();
	pay_goodsNum = pay_goodsNum.substring(1);
	var pay_receiver=$(".consignee2").text();
	var pay_phone=$(".phone").text();
	var pay_address=$(".address2").text();
	var pay_subject="购买商品";
	var pay_body="购买商品";
	var pay_payType = $('input[name="gender"]:checked ').val();
    var payEntrance="0";
    var goodsList = [];
    goodsList.push('{\\"goodsId\\":\\"'+goodsId+'\\",\\"goodsNum\\":\\"'+pay_goodsNum+'\\"}');
    var data = '{"header":"00100009","userId":"'+userId+'","token":"'+token+'",'+
        '"data": "{\\"reasonType\\":\\"'+pay_reasonType+'\\",\\"goods\\":['+goodsList.join(',')+'],\\"receiver\\":\\"'+pay_receiver+'\\",\\"phone\\":\\"'+pay_phone+'\\",\\"address\\":\\"'+pay_address+'\\",\\"payType\\":\\"'+pay_payType+'\\",\\"subject\\":\\"'+pay_subject+'\\",\\"body\\":\\"'+pay_body+'\\",\\"payEntrance\\":\\"'+payEntrance+'\\"}"}';
    /*dataH5*/
    dataH5 ='{"h5Id":"01305","data":"'+data+'"}';
	setBehaviourAction(dataH5);
	$.ajax({
		contentType: "application/json", //必须有
		dataType:'json',
		async: false,
		url:urlW,
		data:data,
		type:"post",
		success:function(data) {
            //alert(data.msg);
            // alert("请求成功");
			//history.go(-2);
		    if("0000" == data.code){
                payAction(data,checkPayInfo);
            }
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
            setShowMsgAction("付款请求数据失败");
            //alert("付款请求数据失败");
        }
	});
}

function checkPayInfo(info){
    alert("info:"+info);
    /*付款成功*/
    if (info==0){
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            shadeClose: true,
            skin: 'yourclass',
            area: ['250px','230px'],
            content:$('.layer-warp')
        });
        $("#down_close").click(function(){
            layer.closeAll();
        });

        $(".pafor_go").click(function(){
            location.href='more.html';
        })

        $(".pafor_list").click(function(){
            orderAction();
        })
    } else if (info==-1){
        //未支付成功
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            shadeClose: true,
            skin: 'yourclass',
            area: ['250px','230px'],
            content:$('.layer-warp_no')
        });

        $("#down_close_no").click(function(){
            layer.closeAll();
        });

        $(".pafor_go_no").click(function(){
            location.href='more.html';
        })

        $(".pafor_list_no").click(function(){
            orderAction();
            setUserInfoAction();
        });
    } else  if (info == -2){
        //支付失败
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            shadeClose: true,
            skin: 'yourclass',
            area: ['250px','230px'],
            content:$('.layer-warp_fail')
        });

        $("#down_close_fail").click(function(){
            layer.closeAll();
        });

        $(".pafor_go_fail").click(function(){
            location.href='more.html';
        });

        $(".pafor_list").click(function(){
            location.href='more.html';
        });
    }
}

//从购买页面返回商品详情页面 goodsId
function goodsback(){	
    var url =localStorage.getItem("refer");
	location.href = url;
	/*dataH5*/
	dataH5 ='{"h5Id":"01306"}';
	setBehaviourAction(dataH5);
};

/*--------------------------添加新地址-------------------*/
function dressNew(){
	$(".ui-navbar-right").click(function(){
		var consignee = $("#_user").val();
		var phone = $("#_phone").val();
		if(consignee==""){
	        // alert("收货人不能为空");
            // 弹框 showMsgAction
            setShowMsgAction("收货人不能为空");
			return false;
		} else if (phone==""){
	        // alert("手机号码不能为空");
	        // 弹框 showMsgAction
            setShowMsgAction("手机号码不能为空");
			return false;
		} else if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){
			//alert("手机号码不正确!");
			// 弹框 showMsgAction
            setShowMsgAction("手机号码不正确");
			return false;
		}
		var address = getAddress();
		if(!address){
			alert("请！");
			return;
		}
		var isDefault = document.getElementById("isDefault").checked?1:0;
		data='{"header":"00100127","userId":"'+userId+'","token":"'+token+'","data": "{\\"consignee\\":\\"'+consignee+'\\",\\\"phone\\":\\"'+phone+'\\",\\"address\\":\\"'+address+'\\",\\"isDefault\\":'+isDefault+'}"}';  

        dataH5 = '{"h5Id":"03902","data":"'+data+'"}';
        setBehaviourAction(dataH5);

		dataH5 = '{"h5Id":"03901","data":"'+data+'"}';
        setBehaviourAction(dataH5);

		dataH5 = '{"h5Id":"03900","data":"'+data+'"}';
        setBehaviourAction(dataH5);
		
		$.ajax({
			contentType: "application/json", //必须有
			dataType:'json',
			url:urlW,
			data:data,
			type:"post",
			success:function(data) {
			    console.log("data:"+data);
	            //location.href= genUrl('my_address.html');
			    var url = 'my_address.html';
	  		    location.href = url;
			}
		});
	});
}

/*--------------------------收货地址列表-------------------*/
function dressList(){
    var warp_address="";
    var addressId = getQueryString("addressId");
    var data='{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}';
    dataH5 = '{"h5Id":"01400","data":"'+data+'"}';
    setBehaviourAction(dataH5);
    $.ajax({
	    contentType: "application/json", //必须有
		dataType:'json',
		url:urlR,
		async: false,
        data:'{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}',
		type:"post",
		success:function(data) {
            /*-----解析购物车------*/
		    $.each(data.deliveryAddressList, function(n,value) {
			    if (value.isDefault==1) {
					warp_address+='<div class="my_address">'
			            +'<div class="address-get">'
				        +'<span class="">收货人:<em class="consignee">'+value.consignee+'</em></span>'
				        +'<span class="add_peo">'+value.phone+'</span>'
                        +'<div class="clear"></div>'
                        +'<div class="address_detail"><em class="address_yes">[默认地址]</em>收货地址:<span class="add">'+value.address+'</span>'
                        +'<span style="display:none;" class="uuid">'+value.uuid+'</span>'
                        +'</div>'
                        +'</div>'
                        +'<div class="address-get_btn">';
		            if(!addressId || value.uuid==addressId){
				        warp_address += '<span class="get_btn_red"><i></i></span>';
				    } else {
				    	warp_address += '<span class="get_btn"></span>';
				    }
		            warp_address += '</div>' +'</div>';
			    } else{
					warp_address+='<div class="my_address">'
                        +'<div class="address-get">'
                        +'<span class="">收货人:<em class="consignee">'+value.consignee+'</em></span>'
                        +'<span class="add_peo">'+value.phone+'</span>'
                        +'<div class="clear"></div>'
                        +'<div class="address_detail">收货地址:<span class="add">'+value.address+'</span>'
                        +'<span style="display:none;" class="uuid">'+value.uuid+'</span>'
                        +'</div>'
                        +'</div>'
                        +'<div class="address-get_btn">';
                    if(value.uuid==addressId){
                        warp_address += '<span class="get_btn_red"><i></i></span>';
                    }else{
                        warp_address += '<span class="get_btn"></span>';
                    }
                    warp_address += '</div>' +'</div>';
			    }
		    });
			$(".my_addr_warp").append(warp_address);
            $(".my_address").on("click",function(){
                /*dataH5*/
                dataH5 ='{"h5Id":"01401"}';
                setBehaviourAction(dataH5);
			
                $(".get_btn_red i").remove();
                $(".get_btn_red").removeClass("get_btn_red").addClass("get_btn");
                $(this).find(".get_btn").removeClass("get_btn").addClass("get_btn_red").append('<i></i>');
                $(this).find(".address_yes").html("");
                var addressId = $(this).find(".uuid").text();
                //localStorage.setItem("addressId",$(this).find(".uuid").text());

                var uuid = $(this).find(".uuid").text();
                var consignee = $(this).find(".consignee").text();
                var phone = $(this).find(".add_peo").text();
                var address = $(this).find(".add").text();
                localStorage.setItem("addressUuid",uuid);
                localStorage.setItem("consignee",consignee);
                localStorage.setItem("phone",phone);
                localStorage.setItem("address",address);
                var url = 'buy.html';
                location.href = url;
		    });
		},
		error: function(){
			alert("收货地址列表请求数据失败");
        }
	});
}

function toAddAddress(){
    var hasAddress = false;

    var length = $(".my_addr_warp").children().length;
    if(length<=0){
        hasAddress=false;
    } else {
        hasAddress=true;
    }

    setAddAddressAction(hasAddress);
//    var url = 'add_address.html';
//    location.href = url;
    /*dataH5*/
    dataH5 ='{"h5Id":"01402"}';
    setBehaviourAction(dataH5);
}

/*标记返回*/
/*商品实物*/
$(".fack_back").click(function(){
    var url =localStorage.getItem("refer");
    location.href = url;
    /*dataH5*/
    dataH5 ='{"h5Id":"01206"}';
    setBehaviourAction(dataH5);
})

$(".back_adress").click(function(){
    /*dataH5*/
    dataH5 ='{"h5Id":"01403"}';
    setBehaviourAction(dataH5);
})

//点击跳转
$('.newAdress').click(function() {
    dataH5 = '{"h5Id":"03903"}';
    setBehaviourAction(dataH5);
});

//点击跳转购物车
$('.shopping').click(function() {
    dataH5 = '{"h5Id":"01204"}';
    setBehaviourAction(dataH5);
    var href = 'cart.html?type=detail';
    window.location.href = href;
});

//选择支付方式
$('.pay_wei_btn').click(function() {
    dataH5 = '{"h5Id":"01303"}';
    setBehaviourAction(dataH5);
});

// 添加购物车
//$(".addToCart").click(function(){
function addToCart(){
    if(userId == null || userId == ''){
        //如果未登录，进入登录页面
        // 跳转登录页面
        setLoginAction();
    } else {
        data = '{"header":"00100304","userId":"'+userId+'","token":"'+token+'",'+
               '"data": "{\\"d02Id\\":\\"'+goodsId+'\\",\\"count\\":\\"'+"1"+'\\"}"}';
        // 已经登录，调用添加购物车接口，添加购物车
        dataH5 = '{"h5Id":"01204","data":"'+data+'"}';
        setBehaviourAction(dataH5);

        $.ajax({
            contentType: "application/json", //必须有
            dataType:'json',
            url:urlW,
            data:data  ,
            type:"post",
            async: false,
            success:function(data) {
                if("0002"==data.code){
                    // 跳转登录页面
                    setLoginAction("账户信息已过期，请重新登录");
                } else if("0000"==data.code){
                     $(".gdd_priceSum").html(data.totalAmount);
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
}//});