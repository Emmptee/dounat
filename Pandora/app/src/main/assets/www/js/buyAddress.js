
var userId = localStorage.getItem("userId");
var token = localStorage.getItem("token");
var dataH5;

function onReadyAddress(){
    document.addEventListener("resume", onResume, false);
    dressList();
}

function onResume() {
    $(".my_addr_warp").children().remove();
    dressList();
}

/*--------------------------收货地址列表-------------------*/
function dressList(){

    var warp_address="";
    var addressId = getQueryString("addressId");
    var datadress='{"header":"00000124","userId":"'+userId+'","token":"'+token+'","data":"{page:0,rows:10}"}';

    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json", //必须有
        timeout: 10000,
        dataType:'json',
        url:urlR,
        data: datadress,
        type:"post",
        success:function(data) {

            $.each(data.deliveryAddressList, function(n,value) {
                if (value.isDefault==1) {
                    warp_address+='<div class="my_address">'
                        +'<div class="address-get">'
                        +'收货人:<span class="receiver"><em class="consignee">'+value.consignee+'</em></span>'
                        +'<span class="add_peo">'+value.phone+'</span>'
                        +'<div class="clear"></div>'
                        +'<div class="address_detail"><em class="address_yes">[默认地址]</em>收货地址:<span class="add">'+value.address+'</span>'
                        +'<span style="display:none;" class="uuid">'+value.uuid+'</span>'
                        +'</div>'
                        +'</div>'
                        +'<div class="address-get_btn">';
                    if(!addressId || value.uuid==addressId){
                        warp_address += '<span class="get_btn_red"><i></i></span>';
                    }else{
                        warp_address += '<span class="get_btn"></span>';
                    }
                    warp_address += '</div>' +'</div>';
                } else {
                    warp_address+='<div class="my_address">'
                        +'<div class="address-get">'
                        +'收货人:<span class="receiver"><em class="consignee">'+value.consignee+'</em></span>'
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
                history.go(-1);

                /*dataH5*/
                setBehaviourAction("01401");
            });
            $("#loadingDiv").hide();
        },
        error: function(){
            // alert("收货地址列表请求数据失败");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });

    setBehaviourAction("01400", datadress);
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
//	var url = 'add_addressAll.html';
//  location.href = url;
    /*dataH5*/
    setBehaviourAction("01402");
}


function back_adress(){
    /*dataH5*/
    history.go(-1);
    setBehaviourAction("01403");
}