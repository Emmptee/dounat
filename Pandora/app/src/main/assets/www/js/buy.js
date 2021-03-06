
var goodsId;
var userId;
var token;
var vipFlg;
var dataH5;
var needMember = JSON.parse(localStorage.getItem("good")).needMember;
var v_img=JSON.parse(localStorage.getItem("good")).xiao_img;
var v_title=JSON.parse(localStorage.getItem("good")).title;
var v_price_p=JSON.parse(localStorage.getItem("good")).price_p;
var v_price_h=JSON.parse(localStorage.getItem("good")).price_h;
var v_number=JSON.parse(localStorage.getItem("good")).buy_times;
var v_sub=JSON.parse(localStorage.getItem("good")).title;
var v_yfvip=JSON.parse(localStorage.getItem("good")).cost_vip;
var v_postageStatus=JSON.parse(localStorage.getItem("good")).postageStatus;
var v_yf=JSON.parse(localStorage.getItem("good")).cost_money;
var v_type=JSON.parse(localStorage.getItem("good")).xiao_type;
var v_goodsId=JSON.parse(localStorage.getItem("good")).xiao_goodId;
var v_number = localStorage.number;
var areaName=JSON.parse(localStorage.getItem("good")).xiao_sub;
var  v_zhek=JSON.parse(localStorage.getItem("good")).zhek;

function onReadyGoodsInfo() {
    document.addEventListener("resume", onResume, false);
    goodInfo();
}

function onResume() {
    $(".goods-order").empty();
    $(".buy_address").empty();
    goodInfo();
}

function goodInfo(){

    goodsId = localStorage.getItem("goodsId");
    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");

    if(userId == null || userId == ""){
        history.go(-1);
        return;
    }

    var addressUuidDefault = null;
    var consignee = null;
    var phone = null;
    var address = null;

    location.href.substring(location.href.indexOf("img="));
    location.href.substring(location.href.indexOf("img="));
    var h_price;

    if((vipFlg==1||needMember==1)&&v_price_h!=null&&(v_price_h!=""||!v_price_h)){
        h_price='<span class="vprice">会员价：'
                + '¥'+v_price_h
                + '</span>';
    }else{
        h_price='<span class="vprice">'
                + '¥'+v_price_p
                + '</span>';
    }
    var sm_swarp="";
    sm_swarp+='<div class="goods_title_t"><i class="v_type"></i><em style="padding:0 3px">/</em>'
    if (areaName==null) {
        sm_swarp+='<span>商品专区</span>'
    } else{
        sm_swarp+='<span>'+areaName+'</span>'
    }
    sm_swarp += '</div>'
                +' <ul class="block-list-cart">'
    $.each(v_zhek, function(n,value) {
		if (value.type==0) {
			sm_swarp+='<div class="zhe_pink"><span class="zhe_li"><i class="zhek">折扣</i><em>'+value.rules+'</em></span></div>'
		} 
    });

    if (v_zhek!=null&&v_zhek.length>0) {
          sm_swarp+=' <li class="block-item block-item-pink" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+v_img+'" alt="" draggable="false" />' + '<div class="detail" >';
    } else{
          sm_swarp+=' <li class="block-item block-item-cart" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+v_img+'" alt="" draggable="false" />' + '<div class="detail" >';
    }

    sm_swarp+= '<h3 class="goods-title ellipsis">'+v_title+'</h3>';

    sm_swarp += '<div class="price-num ellipsis">'+h_price+'<span class="num">×'+v_number+'</span></div>'
               + ' </div>'
               +'</li>'
               +'</ul>';

    if(v_type==0){
        if ((vipFlg==1||needMember==1)&&String(v_yfvip)!=null&&String(v_yfvip)!="") {
            sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">快递</span>'+(v_yfvip==0?(v_postageStatus==1?"邮费到付":"免邮"):v_yfvip+"元")+'</span></div>';
        } else{
            sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">快递</span>'+(v_yf==0?(v_postageStatus==1?"邮费到付":"免邮"):v_yf+"元")+'</span></div>';
        }
    }else if(v_type==1||v_type==2){
        sm_swarp+='<div class="freight">在线观看<span class="price"><span style="padding-right:5px;">快递</span>'+(v_yf==0?(v_postageStatus==1?"邮费到付":"免邮"):v_yf+"元")+'</span></div>';
    }

    $(".goods-order").append(sm_swarp);
    if (vipFlg==1) {
        $(".has_yf").html(v_yfvip);
    } else{
        $(".has_yf").html(v_yf);
    }

    // dressList();

    // 取得默认地址
    var addressUuid = localStorage.getItem("addressUuid");
    if(addressUuid == null || addressUuid == ''){
        var warp_address="";
        var addressId = getQueryString("addressId");
        var data='{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}';

        $("#loadingDiv").show();
        $.ajax({
            contentType: "application/json", //必须有
            timeout: 10000,
            dataType:'json',
            url:urlR,
            async: false,
            data:'{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}',
            type:"post",
            success:function(data) {

                if("0000" == data.code){
                    $.each(data.deliveryAddressList, function(n,value) {
                        if (value.isDefault==1) {
                           addressUuidDefault = value.uuid;
                           consignee = value.consignee;
                           phone = value.phone;
                           address = value.address;
                           addressUuid = addressUuidDefault;
                        }
                   });
                } else if("0002" == data.code) {
                    setLoginAction("0002");
                } else {
                    setShowMsgAction(data.msg);
                }
                $("#loadingDiv").hide();
            },error: function(){
                $("#loadingDiv").hide();
                $("#errorDiv").show();
                // alert("收货地址列表请求数据失败");
            }
        });
    }
    // 取得默认地址结束

    if(v_type == 0){
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
                    //alert(data.addressId);
                    $("#loadingDiv").hide();
                    if("0000" == data.code){
                        if (data.addressId== null || data.addressId=='') {
                            layer.open({
                                content: '你还没有设置收货地址，请点击确定去设置！',
                                btn: ['确定', '取消'],
                                yes: function(index){
                                    layer.closeAll();
									setAddAddressAction(false);
									setBehaviourAction("01301");
                                },
                                no: function(index) {
                                    layer.closeAll();
                                    history.go(-1);
                                    setBehaviourAction("01302");
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
                    } else if("0002" == data.code) {
                        setLoginAction("0002");
                    } else {
                        setShowMsgAction(data.msg);
                    }
                },
                error: function(){
                    $("#loadingDiv").hide();
                    $("#errorDiv").show();
                }

            });
        }

        $(document).on("click","#address-handle",function(){
            gotoAddressPage();
        });
    }
    /*--------------判断实物、片花、数据---------*/
    if (v_type==0) {
        $(".v_type").html("实物商品");
    } else if(v_type==1) {
        $(".v_type").html("视频商品");
    } else {
        $(".v_type").html("文档商品");
    }

    getPriceInfo();

    setBehaviourAction("01300", localStorage.getItem("good"));
}

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
                if(data.price <= 0){
                    $(".pay_for").hide();
                    $("#total_pay").hide();
                    $("#total_free").show();
                } else {
                    $("#total_pay").show();
                    $("#total_free").hide();
                }
            }
            $("#loadingDiv").hide();
        },
        error:function(e){
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });
}

function gotoAddressPage(){
    var addressUuid = $("#address-handle").find(".uuid").text().trim();
    var url = 'my_addressAll.html?addressId='+addressUuid;
    location.href = url;
    /*dataH5*/
    setBehaviourAction("01303");
}

/*---------------------------确认付款-----------------------------*/
function confirmPayGoods(payFlg){

	var pay_reasonType ="1";
	var pay_goodsNum=$(".num").text();
	pay_goodsNum = pay_goodsNum.substring(1);
	var pay_receiver=$(".consignee2").text();
	var pay_phone=$(".phone").text();
	var pay_address=$(".address2").text();
	var pay_subject="甜麦圈-商品购买";
	var pay_body="甜麦圈-商品购买";
	var pay_payType = $('input[name="gender"]:checked ').val();
    var payEntrance="0";
    var goodsList = [];
    goodsList.push('{\\"goodsId\\":\\"'+goodsId+'\\",\\"goodsNum\\":\\"'+pay_goodsNum+'\\"}');
    var data = '{"header":"00100009","userId":"'+userId+'","token":"'+token+'",'+
        '"data": "{\\"reasonType\\":\\"'+pay_reasonType+'\\",\\"goods\\":['+goodsList.join(',')+'],\\"receiver\\":\\"'+pay_receiver+'\\",\\"phone\\":\\"'+pay_phone+'\\",\\"address\\":\\"'+pay_address+'\\",\\"payType\\":\\"'+pay_payType+'\\",\\"subject\\":\\"'+pay_subject+'\\",\\"body\\":\\"'+pay_body+'\\",\\"payEntrance\\":\\"'+payEntrance+'\\"}"}';

    $("#loadingDiv").show();
	$.ajax({
		contentType: "application/json", //必须有
		timeout: 10000,
		dataType:'json',
		async: false,
		url:urlW,
		data:data,
		type:"post",
		success:function(data) {
            $("#loadingDiv").hide();
            //alert(data.msg);
            // alert("请求成功");
			//history.go(-2);
		    if("0000" == data.code){
                payAction(data,checkPayInfo);
                history.go(-2);
            } else if("0002" == data.code) {
                setLoginAction("0002");
            } else {
                if(payFlg == 2 && "9999" == data.code) {
                    orderAction();
                    history.go(-2);
                }
                setShowMsgAction(data.msg);
            }
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
            setShowMsgAction("付款请求数据失败");
            //alert("付款请求数据失败");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
	});

	/*dataH5*/
    setBehaviourAction("01305", data);
}

function checkPayInfo(info){
    // alert("info:"+info);
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
    // var url =localStorage.getItem("refer");
    // location.href = url;
    history.go(-1);
	/*dataH5*/
	setBehaviourAction("01310");
};

function onBackKeyDown() {
    goodsback();
}
