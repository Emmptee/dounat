var userId;
var token;
var vipFlg;
var dataUuidList;

var entityGoodFlg = 0;

function onDeviceReady() {
    document.addEventListener("resume", onResume, false);
    setUserInfoAction(getPromotionOrderAction);
}

function onResume(){
    if(entityGoodFlg == 1){
        setAddress();
    }
}

function getPromotionOrderAction() {

    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");

    if(userId == null || userId == ""){
        onBackKeyDown();
        return;
    }

    cordova.exec(
        function(orderIds) {
            dataUuidList = orderIds;
            goodsAll();
        },
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "promotionOrderAction",
        []);
}

function goodsAll(){

	data = '{"header":"00000309","userId":"'+userId+'","token":"'+token+'",'+
                   '"data":"{\\"uuidList\\":['+dataUuidList+']}"}';

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
                if(data.goodsList==null || data.goodsList.length==0)
                {
                   onBackKeyDown();
                   return;
                }
                var h_price;
                var freight = null;
                var goodType = null;

                //循环开始
                $.each(data.goodsList,function(index,item){

                    if(item.num > item.residueNum && item.type==0) {
                        var message = "对不起，您选择的" + item.description + "商品库存已不足，请重新选择。"
                        setShowMsgAction(message);
                        onBackKeyDown();
                        return false;
                    }

                    var freight = null;
                    if(vipFlg==0){
                        // h_price='<span class="vprice">￥'+item.price+'</span>';
                        h_price = item.price;
                        freight = item.freight;
                        if(freight == '0' || freight == null){
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
                        h_price = item.memberPrice;
                        freight = item.memberFreight;
                        if(freight == '0' || freight == null){
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

    setAddress();
}

function setAddress() {

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
            $(".buy_address").empty().append(my_dress);
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
                                onBackKeyDown();
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
                        $(".buy_address").empty().append(my_dress);
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

function gotoAddressPage(){
    var addressUuid = $("#address-handle").find(".uuid").text().trim();
    var url = 'my_addressAll.html?addressId='+addressUuid;
    location.href = url;
    /*dataH5*/
    setBehaviourAction("01303");
}

function onBackKeyDown(){
    setBehaviourAction("01310");
    setBackAction();
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

    if(payFlg == 1 || payFlg == 2){
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
                onBackKeyDown();
            } else if("0002" == data.code){
                setLoginAction("0002");
            } else {
                if(payFlg == 2 && "9999" == data.code) {
                    orderAction();
                    onBackKeyDown();
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

function checkPayInfo(info){ }
