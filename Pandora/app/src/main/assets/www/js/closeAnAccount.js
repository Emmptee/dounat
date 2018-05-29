var goodsId = localStorage.getItem("goodsId");
var userId = localStorage.getItem("userId");
var token = localStorage.getItem("token");
var vipFlg = localStorage.getItem("vipFlg");
var data;
var dataUuidList=localStorage.getItem("dataUuidList");
var goodsType;
var dataH5;
// 购物车中  多个商品结算

function onResume()
{
    $(".goods-order").empty();
    $(".sum_money").empty();
    $(".has_yf").empty();//运费
    $(".buy_address").empty();

    cartAll();
}

function cartAll(){

    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");

    if(userId == null || userId == ""){
        history.go(-1);
        return;
    }

    var entityGoodFlg = 0;
	data = '{"header":"00000309","userId":"'+userId+'","token":"'+token+'",'+
                   '"data":"{\\"uuidList\\":['+dataUuidList+']}"}';

    /*dataH5*/
	setBehaviourAction("01300", data);
    $("#loadingDiv").show();
	$.ajax({
        contentType: "application/json", //必须有
        timeout: 10000,
        dataType:'json',
        url:urlR,
        data:data,
        async:false,
        type:"post",
        success:function(data) {
            if("0002"==data.code){
                // 跳转登录页面
                setLoginAction(data.msg);
            } else if("0000"==data.code) {
                if(data.goodsList==null||data.goodsList.length==0)
                {
                    history.go(-1);
                }
                var h_price;
                var freight = null;
                var goodType = null;

                //循环开始
                $.each(data.goodsList,function(index,item){

                    if(item.num > item.residueNum && item.type==0) {
                        var message = "对不起，您选择的" + item.description + "商品库存已不足，请重新选择。"
                        setShowMsgAction(message);
                        history.go(-1);
                        return false;
                    }

                    var freight = null;
                    if(vipFlg==0){
                        // h_price='<span class="vprice">￥'+item.price+'</span>';
                        h_price=+item.price;
                        freight =item.freight;
                        if(freight == '0'){
                            if(item.postageStatus==1){
                                freight = "邮费到付"
                            }else{
                                freight = "免邮"
                            }
                        }else
                        {
                            freight+="元";
                        }
                    }else if(vipFlg==1){
                        // h_price='<span class="vprice">￥'+item.memberPrice+'</span>';
                        h_price=+item.memberPrice;
                        freight =item.memberFreight;
                        if(freight == '0'){
                            if(item.postageStatus==1){
                                freight = "邮费到付"
                            }else{
                                freight = "免邮"
                            }
                        }else
                        {
                             freight+="元";
                        }
                    }

                    if (item.type==0) {
                        goodType = "实物商品";
                        entityGoodFlg = 1;
                    }else if(item.type==1){
                        goodType = "视频商品";
                    }else{
                        goodType = "文档商品";
                    }

                    var sm_swarp="";
                    sm_swarp+='<li style = "border-bottom:1px #d2d2d2 dotted;margin-top: 10px">';
                    sm_swarp+='<div class="goods_title_t"><i class="v_type">'+goodType+'</i><em style="padding:0 3px">/</em><span>'+item.areaName+'</span></div>';

                    $.each(item.promotions, function(n,value) {
                        if (value.type==0) {
                            sm_swarp+='<div class="zhe_pink"><span class="zhe_li"><i class="zhek">折扣</i><em>'+value.rules+'</em></span></div>'
                        }
                    });

                    if (item.promotions != null && item.promotions.length > 0) {
                        sm_swarp += ' <ul class="block-list-cart">'
                                    +' <li class="block-item block-item-pink" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+item.thumbnail+'" alt="" draggable="false" />'
                                    + '<div class="detail" >';
                    } else {
                        sm_swarp += ' <ul class="block-list-cart">'
                                    +' <li class="block-item block-item-cart" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+item.thumbnail+'" alt="" draggable="false" />'
                                    + '<div class="detail" >';
                    }

                    if(vipFlg==1){
                        sm_swarp+= '<h3 class="goods-title ellipsis">'+item.description+'</h3>';
                    }else{
                        sm_swarp+= '<h3 class="goods-title ellipsis">'+item.description+'</h3>';
                    }

                    sm_swarp+= '<div class="price-num ellipsis"><span class="vprice">'
                                + ((vipFlg == 1) ? "会员价：" : "")
                                + '¥'+h_price
                                + '</span><span class="num">x'+item.num+'</span>'
                    sm_swarp+= '<input type="hidden" class="residueNum" name="residueNum" value="'+item.residueNum+'"/></div>'
                        + ' </div>'
                        +'</li>'
                        +'</ul>';
                    if(item.type==0){
                        sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">快递</span>'+freight+'</span></div>';
                    }else if(item.type==1||item.type==2){
                        //sm_swarp+='<div class="freight">在线观看<span class="price"></div>';
                        sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">在线观看</span></span></div>';
                    }
                    sm_swarp+="</li>";

                    $(".goods-order").append(sm_swarp);
                    /*--------------判断实物、片花、数据---------*/

                });
                //$(".goods-order").append(sm_swarp);
                /*循环结束*/
                $(".sum_money").html(data.totalAmount);
                $(".has_yf").html(data.totalFreight);//运费

                if(data.totalAmount <= 0) {
                    $(".pay_for").hide();
                    $("#total_pay").hide();
                    $("#total_free").show();
                } else {
                    $("#total_pay").show();
                    $("#total_free").hide();
                }
            } else{
                // 弹框 showMsgAction
               setShowMsgAction(data.msg);
            }
            $("#loadingDiv").hide();
        },error:function(){
           //  alert("结算异常");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });


    $(document).on("click","#address-handle",function(){
        gotoAddressPage();
    });

    var addressUuid = localStorage.getItem("addressUuid");
	var addressUuidDefault = null;
    var consignee = null;
    var phone = null;
    var address = null;
    if(addressUuid == null || addressUuid == ''){
        // 取得默认地址
        var warp_address="";
        var addressId = getQueryString("addressId");
        var data = '{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}';
        $("#loadingDiv").show();
        $.ajax({
            contentType: "application/json", //必须有
            timeout: 10000,
            dataType:'json',
            url:urlR,
            data: data,
            type:"post",
            success:function(data) {

        		$.each(data.deliveryAddressList, function(n,value) {
        			if (value.isDefault==1) {
                         addressUuidDefault = value.uuid;
                         consignee = value.consignee;
                         phone = value.phone;
                         address = value.address;
                         addressUuid = addressUuidDefault;
        			}

        		});
        		$("#loadingDiv").hide();
            },error: function(){
                // alert("收货地址列表请求数据失败");
                $("#loadingDiv").hide();
                $("#errorDiv").show();
            }
        });
    }

    if(entityGoodFlg == 1){
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
            $("#loadingDiv").show();
            $.ajax({
                contentType: "application/json", //必须有
                timeout: 10000,
                dataType:'json',
                url:urlR,
                data:'{"header":"00000511","userId":"'+userId+'","token":"'+token+'","data":"{}"}'  ,
                type:"post",
                success:function(data) {
                    $("#loadingDiv").hide();
                    if (data.addressId== null|| data.addressId=='') {
                        layer.open({
                            content: '你还没有设置收货地址，请点击确定去设置！',
                            btn: ['确定', '取消'],
                            yes: function(index){
                                setAddAddressAction(false);
                                layer.closeAll();
                                setBehaviourAction("01301");
                            },
                            no: function(index) {
                                history.go(-1);
                                setBehaviourAction("01302");
                            }
                        });
                    } else{
                        var my_dress="";
                        my_dress +=' <section class="address-handle section serv-btn" id="address-handle" data-link="">'+
                                '<span class="uuid" style="display:none;">'+data.addressId+'</span>'+
                                '<a href="javascript:void(0);" onclick="gotoAddressPage()"><span class="arrow-left" style="top:44%"></span></a>'+
                                '<div class="wuliu-div">收货人:<span class="consignee2">'+data.consignee+'</span></div>'+
                                '<div class="phone">'+data.phone+'</div><div class="clear"></div>'+
                                '<div class="address_detail"><div class="wuliu-dive textOverflow2"><span class="address2">'+data.address+'</span></div></div></section>'+
                                '<div class="add_bg"></div><div class="borderD"></div>';
                        $(".buy_address").append(my_dress);
                    }
                },
                error: function(){
                    $("#loadingDiv").hide();
                    $("#errorDiv").show();
                }
            });
        }
    }
}

// 单个商品结算开始
function singleGoods(){
    var singlgGoogsInfo=JSON.parse(localStorage.getItem("singlgGoogsInfo"));
    //var entityGoodFlg = 0;

    var areaName=singlgGoogsInfo.areaName;
    var goodType=singlgGoogsInfo.goodType;
    goodsType = goodType;
    var thumbnail=singlgGoogsInfo.thumbnail;
    var description=singlgGoogsInfo.description;
    var price=singlgGoogsInfo.price;
    var residueNum=singlgGoogsInfo.residueNum;
    var  v_zhek=singlgGoogsInfo.zhek;
    var num = 1;

    var freight = null;

    if (goodType==1) {
        goodType = "视频商品";
    }else if(goodType==2){
        goodType = "文档商品";
    }

    var sm_swarp="";
    sm_swarp += "<li>";
    sm_swarp += '<div class="goods_title_t"><i class="v_type">'+goodType+'</i><em style="padding:0 3px">/</em>'
    if (areaName==null) {
        sm_swarp+='<span>商品专区</span>'
    } else{
        sm_swarp+='<span>'+areaName+'</span>'
    }
    sm_swarp += ' </div>'
                +' <ul class="block-list-cart">'
    $.each(v_zhek, function(n,value) {
		if (value.type==0) {
			sm_swarp+='<div class="zhe_pink"><li class="zhe_li"><i class="zhek">折扣</i><em>'+value.rules+'</em></li></div>'
		} 
    });
    if (v_zhek!=null&&v_zhek.length>0) {
		sm_swarp += ' <li class="block-item block-item-pink" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+thumbnail+'" alt="" draggable="false" />'
                    + '<div class="detail" >';
	} else{
		sm_swarp += ' <li class="block-item block-item-cart" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+thumbnail+'" alt="" draggable="false" />'
                    + '<div class="detail" >';
	}
            
    sm_swarp += '<h3 class="goods-title ellipsis">'+description+'</h3>';
    sm_swarp += '<div class="price-num ellipsis"><span class="vprice">'
                + ((vipFlg == 1) ? "会员价：" : "")
                + '¥'+price+'</span><span class="num">x'+num+'</span>';
    sm_swarp += '<input type="hidden" class="residueNum" name="residueNum" value="'+residueNum+'"/></div>'
                + ' </div>'
                +'</li>'
                +'</ul>';
    //sm_swarp+='<div class="freight">在线观看<span class="price"></div>';
    sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">在线观看</span></span></div>';
    sm_swarp+="</li>";

    $(".goods-order").append(sm_swarp);

    getPriceInfo();

    setBehaviourAction("01300", singlgGoogsInfo);
}
// 单个商品结算结束

function getPriceInfo() {
    var pay_reasonType ="1";
	var pay_goodsNum=$(".num").text().substring(1);
    var payEntrance="3";
    var goodsList = [];
    goodsList.push('{\\"goodsId\\":\\"'+goodsId+'\\",\\"goodsNum\\":\\"'+pay_goodsNum+'\\"}');

    var data = '{"header":"00100009","userId":"'+userId+'","token":"'+token+'",'+
        '"data": "{\\"reasonType\\":\\"'+pay_reasonType+'\\",\\"goods\\":['+goodsList.join(',')
        +'],\\"payEntrance\\":\\"'+payEntrance+'\\"}"}';

    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json",
        timeout: 10000,
        dataType:'json',
        url:urlW,
        data:data,
        type:"post",
        success:function(data) {
            if(data.code == "0000") {
                $(".sum_money").html(parseFloat(data.price).toFixed(2));
            }else if(data.code == "0002"){
                setShowMsgAction(data.msg);
                history.go(-1);
                setLoginAction("0002");
            } else if("0005" == data.code){
                setShowMsgAction("系统内部错误");
            } else {
                setShowMsgAction(data.msg);
            }
            $("#loadingDiv").hide();
        },
        error:function(e){
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });
}

/*-------------获取url参数--------------*/
function getQueryString(name){
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}

function gotoAddressPage(){
    var addressUuid = $("#address-handle").find(".uuid").text().trim();
    var url = 'my_addressAll.html?addressId='+addressUuid;
    location.href = url;
    /*dataH5*/
    setBehaviourAction("01303");
}

function backB(){
    setBehaviourAction("01310");
 /* var urlBack ="cart.html";
  location.href = urlBack;*/
  history.go(-1);
 }

function backBSingle(){
    setBehaviourAction("01310");

//    var urlBack ="";
//    if(goodsType == 1)
//        urlBack = "goods_details_videos.html?fromHtml=true";
//    else if(goodsType == 2)
//        urlBack = "goods_details_documents.html?fromHtml=true";
//
//    document.getElementById("backBSingle").href=urlBack;
    //location.href = urlBack;
    history.go(-1);
}

/*---------------------------确认付款-----------------------------*/
function confirmPay(payFlg){

    // 参数取得
    var reasonType="1";
    var receiver=$(".consignee2").text();
    var phone=$(".phone").text();
    var address=$(".address2").text();
    var payType=$('input[name="gender"]:checked ').val();
    var subject="甜麦圈-商品购买";
    var body="甜麦圈-商品购买";

    if(payFlg == 0) {//直接支付
        var payEntrance="0";
        data = '{"header":"00100009","userId":"'+userId+'","token":"'+token+'",'+
            '"data": "{\\"reasonType\\":\\"'+reasonType+'\\",\\"goods\\":[{\\"goodsId\\":\\"'+goodsId+'\\",\\"goodsNum\\":\\"1\\"}],'+
            '\\"receiver\\":\\"'+receiver+'\\",\\"phone\\":\\"'+phone+'\\",\\"address\\":\\"'+address+'\\",\\"payType\\":\\"'+payType+'\\",'+
            '\\"subject\\":\\"'+subject+'\\",\\"body\\":\\"'+body+'\\",\\"payEntrance\\":\\"'+payEntrance+'\\"}"}';
    } else if(payFlg == 1 || payFlg == 2){
        var payEntrance="1";

        data = '{"header":"00100009","userId":"'+userId+'","token":"'+token+'",'+
            '"data": "{\\"reasonType\\":\\"'+reasonType+'\\",\\"d01Ids\\":['+dataUuidList+'],'+
            '\\"receiver\\":\\"'+receiver+'\\",\\"phone\\":\\"'+phone+'\\",\\"address\\":\\"'+address+'\\",\\"payType\\":\\"'+payType+'\\",'+
            '\\"subject\\":\\"'+subject+'\\",\\"body\\":\\"'+body+'\\",\\"payEntrance\\":\\"'+payEntrance+'\\"}"}';
    }

    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json", //必须有
        timeout: 10000,
        dataType:'json',
        url:urlW,
        data:data,
        type:"post",
        success:function(data) {
            if("0000" == data.code){
                payAction(data,checkPayInfo);
                history.go(-2);
            } else if("0002" == data.code){
                setLoginAction("0002");
            } else {
                if(payFlg == 2 && "9999" == data.code) {
                    orderAction();
                    history.go(-2);
                }
                setShowMsgAction(data.msg);
            }
            $("#loadingDiv").hide();
        },error: function(XMLHttpRequest, textStatus, errorThrown){
            setShowMsgAction("付款请求数据失败");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });

    /*dataH5*/
    setBehaviourAction("01305", data);

}

function checkPayInfo(info){
    if (info==0){
        /*付款成功*/
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
        })

        $(".pafor_go").click(function(){
            location.href='more.html';
        })

        $(".pafor_list").click(function(){
            orderAction();
        //进入订单列表
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
        })
        $(".pafor_list").click();
        $(".pafor_go_no").click(function(){
            location.href='more.html';
        })

        $(".pafor_list_no").click(function(){
            orderAction();
            //继续支付
        })
    }else if (info==-2){
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
        })
        $(".pafor_list").click()
        $(".pafor_go_fail").click(function(){
            location.href='more.html';
        })

        $(".pafor_list").click(function(){
            location.href='more.html';
            //继续支付
        })
    }

}
