var goodsId;
var userId = localStorage.getItem("userId");
var token = localStorage.getItem("token");
var vipFlg = localStorage.getItem("vipFlg");
var data;
var goodsType;
var dataH5;
// 购物车中  多个商品结算

document.addEventListener("deviceready", onDeviceReady, false);
document.addEventListener("backbutton", backAction, false);
function onResume()
{
    $(".goods-order").empty();
    $(".sum_money").empty();
    $(".has_yf").empty();//运费
    $(".buy_address").empty();

	getAuctionRecordId(initLoad);
}

function onDeviceReady()
{
	getAuctionRecordId(initLoad);
	document.addEventListener("resume", onResume, false);
}

function initLoad(){

    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");

    if(userId == null || userId == ""){
        backAction();
        return;
    }
    var d10Id = getQueryString("d10Id");
    if(d10Id==null||d10Id=='')
    {
    	d10Id = localStorage.getItem("d10Id");
    }

    var entityGoodFlg = 0;
	data = '{"header":"00000320","userId":"'+userId+'","token":"'+token+'",'+
                   '"data":"{\\"d10Id\\":'+d10Id+'}"}';

    setBehaviourAction("08300",data);
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
            	goodsId = data.d02Id;
                var freight = null;
                var freightAmount = null;
                var goodType = null;
                var h_price=data.auctionCurrentPrice;
                if(vipFlg==0){
                    freight =data.freight;
                    freightAmount = data.freight;
                    if(freight == '0'){
                        if(data.postageStatus==1){
                            freight = "邮费到付"
                        }else{
                            freight = "免邮"
                        }
                    }else
                    {
                        freight+="元";
                    }
                }else if(vipFlg==1){
                    freight =data.memberFreight;
                    freightAmount = data.memberFreight;
                    if(freight == '0'){
                        if(data.postageStatus==1){
                            freight = "邮费到付"
                        }else{
                            freight = "免邮"
                        }
                    }else
                    {
                         freight+="元";
                    }
                }

                if (data.type==0) {
                    goodType = "实物商品";
                    entityGoodFlg = 1;
                }else if(data.type==1){
                    goodType = "视频商品";
                }else{
                    goodType = "文档商品";
                }

                var sm_swarp="";
                sm_swarp+='<li style = "border-bottom:1px #d2d2d2 dotted;margin-top: 10px">';
                sm_swarp+='<div class="goods_title_t"><i class="v_type">'+goodType+'</i>';
                if(data.subjectName!=null&&data.subjectName!='')
                {
               		sm_swarp+='<em style="padding:0 3px">/</em><span>'+data.subjectName+'</span>';
                }
                sm_swarp+='</div>';
               
                sm_swarp += ' <ul class="block-list-cart">'
                            +' <li class="block-item block-item-cart" style="margin:0px;"> <img style="left: 10px;" class="shop-goods-thumb" src="'+data.thumbnail+'" alt="" draggable="false" />'
                            + '<div class="detail" >';
                sm_swarp+= '<h3 class="goods-title ellipsis">'+data.description+'</h3>';
                sm_swarp+= '<div class="price-num ellipsis"><span class="vprice">'
                            + '¥'+h_price
                            + '</span></div>'
		                    + ' </div>'
		                    +'</li>';
                if(data.type==0){
                    sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">快递</span>'+freight+'</span></div>';
                }else if(data.type==1||data.type==2){
                    sm_swarp+='<div class="freight">配送方式<span class="price"><span style="padding-right:5px;">在线观看</span></span></div>';
                }
                sm_swarp+="</ul></li>";

                $(".goods-order").append(sm_swarp);
                /*--------------判断实物、片花、数据---------*/
                var totalAmount = h_price+freightAmount;
                $(".sum_money").html(totalAmount);
                $(".has_yf").html(freightAmount);//运费

                $("#total_pay").show();
                
                if(entityGoodFlg == 1){
				    var addressUuid = localStorage.getItem("addressUuid");
				    var consignee = null;
				    var phone = null;
				    var address = null;
				    if(addressUuid!=null&&addressUuid!='')
				    {
					    if(localStorage.getItem("consignee") != null && localStorage.getItem("consignee") !='')
	                 		consignee = localStorage.getItem("consignee");
			            if(localStorage.getItem("phone") != null && localStorage.getItem("phone") !='')
			                phone = localStorage.getItem("phone");
			            if(localStorage.getItem("address") != null && localStorage.getItem("address") !='')
			                address = localStorage.getItem("address");
			            localStorage.removeItem("addressUuid");
			            localStorage.removeItem("consignee");
			            localStorage.removeItem("phone");
			            localStorage.removeItem("address");
				    }else if(data.consignee!=null&&data.consignee!='')
				    {
				    	consignee=data.consignee;
				    	phone = data.phone;
				    	address = data.address;
				    }else
				    { 
				    	layer.open({
                            content: '你还没有设置收货地址！',
                            btn: ['去设置', '取消'],
                            yes: function(index){
                            	setBehaviourAction("08301");
                                setAddAddressAction(false);
                                layer.closeAll();
                            },
                            no: function(index) {
                                backAction();
                            }
                        });
                        return ;
				    }
				    var my_dress =' <section class="address-handle section serv-btn" id="address-handle">'+
                    '<span class="uuid" style="display:none;">'+addressUuid+'</span>'+
                    '<a href="javascript:void(0);" onclick="gotoAddressPage()"><span class="arrow-left" style="top:44%"></span></a>'+
                    '<div class="wuliu-div">收货人:<span class="consignee2">'+consignee+'</span></div>'+
                    '<div class="phone">'+phone+'</div><div class="clear"></div>'+
                    '<div class="address_detail"><div class="wuliu-dive textOverflow2"><span class="address2">'+address+'</span></div></div></section>'+
                    '<div class="add_bg"></div>'+
                    '<div class="borderD"></div>';
           			 $(".buy_address").append(my_dress);
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
    setBehaviourAction("08302");
    var url = 'my_addressAll.html?addressId='+addressUuid;
    location.href = url;
    /*dataH5*/
}

function backAction(){
    setBehaviourAction("08304");
    var d10Id = getQueryString("d10Id");
    if(d10Id==null||d10Id=='')
    {
    	setBackAction();
    }
    else
    {
  		history.go(-1);
    }
 }

/*---------------------------确认付款-----------------------------*/
function confirmPay(){

    // 参数取得
    var reasonType="1";
    var receiver=$(".consignee2").text();
    var phone=$(".phone").text();
    var address=$(".address2").text();
    var payType=$('input[name="gender"]:checked ').val();
    var subject="甜麦圈-商品购买";
    var body="甜麦圈-商品购买";

    var payEntrance="0";
    data = '{"header":"00100009","userId":"'+userId+'","token":"'+token+'",'+
        '"data": "{\\"reasonType\\":\\"'+reasonType+'\\",\\"goods\\":[{\\"goodsId\\":\\"'+goodsId+'\\",\\"goodsNum\\":\\"1\\"}],'+
        '\\"receiver\\":\\"'+receiver+'\\",\\"phone\\":\\"'+phone+'\\",\\"address\\":\\"'+address+'\\",\\"payType\\":\\"'+payType+'\\",'+
        '\\"subject\\":\\"'+subject+'\\",\\"body\\":\\"'+body+'\\",\\"payEntrance\\":\\"'+payEntrance+'\\"}"}';

    setBehaviourAction("08303",data);
    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json", //必须有
        timeout: 5000,
        dataType:'json',
        url:urlW,
        data:data,
        type:"post",
        success:function(data) {
            $("#loadingDiv").hide();
            setShowMsgAction(data.msg);
            if("0000" == data.code){
                payAction(data,checkPayInfo);
                backAction();
            } else if("0002" == data.code){
                setLoginAction("0002");
            } else {
                if(payFlg == 2 && "9999" == data.code) {
                    orderAction();
                	backAction();
                }
            }
        },error: function(XMLHttpRequest, textStatus, errorThrown){
            setShowMsgAction("付款请求数据失败");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });

    /*dataH5*/

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
