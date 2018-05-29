/*------------------------------------------------------------*/
var userId;
var token;
var vipFlg;
var data = null;
var dataH5 = null;
var vSwiper = null;
var intime = 50;

function onDeviceReady() {
    document.addEventListener("resume", onResume, false);
    setUserInfoAction(cartFunc);
}

function onResume() {
    pageId = 0;
    getData(0, 0);
}

function cartFunc() {

    wrapperSwipe();

    getData(0, 0);

    $(".cart_bj").click(function() {
        classComplete();
        $(".cart_bj").hide();
        $(".cart_complete").show();
        $(".footerTable1").hide();
        $(".footerTable2").show();

        setBehaviourAction("01501");
    });

    $(".cart_complete").click(function() {
        classEdit();
        topRightStateEdit();
        complete();
    });
}

function topRightStateEdit(){
    $(".cart_complete").hide();
    $(".cart_bj").show();
    $(".footerTable1").show();
    $(".footerTable2").hide();
}

function classEdit() {
    $(".edit").each(function() {
        $(this).parent().siblings("dd").find(".delBtn").hide();
        $(this).html("编辑");
        var goodsType = $(this).parent().siblings("dd").find(".goodsType").val();
        if(goodsType==0)
        {
            $(this).parent().siblings("dd").find(".numberWidget").hide();
            $(this).parent().siblings("dd").find(".title_cart").show();
        }
    });
}

function classComplete() {
    $(".edit").each(function() {
        $(this).parent().siblings("dd").find(".delBtn").show();
        $(this).html("完成");
        var goodsType = $(this).parent().siblings("dd").find(".goodsType").val();
        if(goodsType==0)
        {
            $(this).parent().siblings("dd").find(".numberWidget").show();
            $(this).parent().siblings("dd").find(".title_cart").hide();
        }
    });
}

var pageId = 0;
var loadedArr = [false, false, false];

function wrapperSwipe() {

    $(window).on('resize', function() {
        fixPagesHeight();
    });

    fixPagesHeight();

    //Init Pages
    var pages = $('.swiper-pages').swiper({
        onSlideChangeStart: function() {
        },
        onSlideChangeEnd: function(swiper) {
        }
    });

    //Scroll Containers
    $('.scroll-container').each(function(index) {
        vSwiper = $(this).swiper({
            mode: 'vertical',
            scrollContainer: true,
            mousewheelControl: true,
            scrollbar: {
                container: $(this).find('.swiper-scrollbar')[0]
            },
            onTouchMove: function(swiper) {
                var scrollTop = swiper.positions.current;
                var winHeight = vSwiper.height;
                var totalHeight = $('.content-slide').height();
                if (scrollTop > 0) {
                    $(".load").show();
                    $(".load .tip").html("下拉刷新");
                    if (scrollTop > 60) {
                        $(".load .tip").html("松开开始刷新");
                    }
                } else if ($(".cart").length>9&&(winHeight < totalHeight ? (Math.abs(scrollTop) + winHeight > totalHeight) : (Math.abs(scrollTop) > 60))) {
                    $(".more").show();
                    $(".more .tip").html("上拉加载更多");
                    if ((Math.abs(scrollTop) + winHeight - totalHeight) > 60) {
                        $(".more .tip").html("松开开始加载");
                    }
                }
            },
            onTouchEnd: function(swiper) {
                var scrollTop = swiper.positions.current;
                var winHeight = vSwiper.height;
                var totalHeight = $('.content-slide').height();
                if (scrollTop > 60) {
                    clearTimeout(0);
                    $(".load .tip").html("正在刷新...");
                    $(".load").css({
                        marginTop: 0
                    });
                    pageId = 0;
                    getData(1, 0);
                } else if ($(".cart").length>9&&(winHeight < totalHeight ? (Math.abs(scrollTop) + winHeight > totalHeight) : (Math.abs(scrollTop) > 60))) {
                    if ($(".content-slide").find("dl dd").size() == 0) {
                        pageId = 0;
                        getData(1, 0);
                        return;
                    }
                    $(".more .tip").html("正在加载...");
                    //	window.scrollBy(0,40);
                    getData(1, pageId);
                }
            }
        });
    });
}

//Function to Fix Pages Height
function fixPagesHeight() {
    $('.swiper-pages').css({
        height: $(window).height() - $(".cart_footer").height()-$("header").height()
    });
}

function resizeContentHeight()
{
	var fullHeight = $(window).height() - 110;
	var currentHeight = $('.content-slide').height();
	if(fullHeight>currentHeight)
	{
		$('.content-slide').css({
			height: fullHeight
		});
	}
}


function getData(flg, pageId) {

    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");

    if(userId == null || userId == ""){
        onBackKeyDown();
        return;
    }

    data = '{"header":"00000307","userId":"' + userId + '","token":"' + token + '",'
            + '"data": "{\\"page\\":\\"' + pageId + '\\",\\"rows\\":\\"10\\"}"}';

    if (flg == 0) {
        setBehaviourAction("01500", data);
        $("#loadingDiv").show();
    }
    $.ajax({
        contentType: "application/json",
        timeout: 10000,
        dataType: 'json',
        url: urlR,
        data: data,
        type: "post",
        success: function(data) {
            if ("0002" == data.code) {
                // 跳转登录页面
                setLoginAction(data.msg);
            } else if ("0000" == data.code) {
                showView(data);
            } else {
                // 弹框 showMsgAction
                setShowMsgAction(data.msg);
            }
            $("#loadingDiv").hide();
        },
        error: function() {
            // 弹框 showMsgAction
            setShowMsgAction("获取信息失败");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });
}

function showView(data) {

    $(".txtred").html($(".txtred").html());
    if(data.goodsList.length > 0) {
        $(".swiper-slide").css("padding-bottom","10px");
    }
    var box = "";
    var index = 0;
    $.each(data.goodsList, function(index, item) {

        var residueNum = item.residueNum;
        if (item.type != 0) residueNum = 1;

        box += '<dl class="cart swiper-slide" style="margin-top: 0px;'
            +(index==data.goodsList.length-1?"padding-bottom:0px;":"")+'"> <dt> <label>';
        if (residueNum == 0 || item.status == 0 || (item.needMember == 1 && vipFlg == 0)) {
            //此处删除按钮;
        } else {
            box += '<span class="cart_icon cart_F" onclick="cartFB(this);"></span>'
        }
        var goodsType;
        switch (item.type) {
        case 0:
            goodsType = "商品实物/";
            break;
        case 1:
            goodsType = "视频花絮/";
            break;
        case 2:
            goodsType = "报告文档/";
            break;
        }
        box += '<input type="hidden"/><i class="cart_item_title">'
                + goodsType + (item.areaName == null ? "商品专区": item.areaName)
                + '</i></label> <a class="edit" onclick="edit(this);">编辑</a> </dt> ';
        var boxflag = false;
        $.each(item.promotions, function(n, value) {
            if (value.type == 0) {
                box += '<div class="zhe_pink"><li class="zhe_li"><i class="zhek">折扣</i><em>'
                        + value.rules + '</em></li></div>';
                boxflag = true;
            }
        });
        if (boxflag) {
            box += '<dd class="cart_k">';
        } else {
            box += '<dd class="cart_s">'
        }

        box += '<a class="goodsPic" onclick="goodsDetails(\'' + item.goodsId + '\',\''
                + item.type + '\');"><img src="' + item.thumbnail + '"/></a>';

        box += '<div class="goodsInfor"> <h2>';

        box += "<div class='title_cart' onclick=\"goodsDetails(\'" + item.goodsId + "\',\'"
                + item.type + "\');\">" + item.description + "</div>";

        box += '<div class="numberWidget"> <input type="hidden" class="uuid" name="uuid" value="'
                + item.uuid + '"/> <input type="hidden" class="goodsType" name="goodsType" value="' + item.type + '"/>'
                + '<input type="hidden" class="residueNum" name="residueNum" value="' + item.residueNum + '"/>';
        if (item.type == 0) {
            box += '<input type="button" value="-" class="minus" onclick="minusB(this);"/> <input type="text" value="'
                    + item.num + '" readonly  class="number"/> <input type="button" value="+"  class="plus" onclick="plusB(this);"/> ';
        }
        box += '</div> <span class="cart_num">' + item.num + '</span> </h2> <div class="priceArea">';

        if(item.needMember==1) {
            box +='<span>会员价:</span><span class="price">'+item.memberPrice+'</span>'
        } else {
            if(vipFlg==1&&item.memberPrice!=null&&item.price!=item.memberPrice)
            {
                box +='<span>会员价:</span><span class="price">'+item.memberPrice+'</span>'
                    +'<del class="originalPrice">'+item.price+'</del>';
            }else
            {
                box +='<span class="price">'+item.price+'</span>'
            }
        }

        box += '</div>' + '</div>' + '<a class="delBtn" onclick="delBtn(this)">删除</a>';
        if (item.needMember == 1 && vipFlg == 0) {
            box += "<div class='front_bg' onclick=\"goodsDetails(\'" + item.goodsId + "\',\'"
                    + item.type + "\');\"><span><i>会员专享</i></span></div>"
        } else if (residueNum == 0) {
            box += "<div class='front_bg' onclick=\"goodsDetails(\'" + item.goodsId + "\',\'"
                    + item.type + "\');\"><span><i>暂时无货</i></span></div>"
        } else if (item.status == 0) {
            box += '<div class="front_bg"><span><i>商品下架</i></span></div>'
        }

        box += '</dd> </dl>';
        index++;

    });
    resizeContentHeight();
    if (pageId == 0) {
        $(".cart_swarp").empty().append(box);
        $(".txtred").html("0");
        $(".cart_all").addClass("cart_icon_select_all").removeClass("cart_icon_red");
        topRightStateEdit();
        setTimeout(function(){
                $(".load").animate({marginTop:-40},200,function(){
                    $(".load").hide();
                    vSwiper.reInit();
            });
        },400);
        if (data.goodsList.length < 10) {
            $(".more").hide();
        } else {
            $(".more .tip").html("上拉加载更多");
        }
    } else {

        classEdit();
        topRightStateEdit();

        setTimeout(function(){
            $(".cart_swarp").append(box);
            vSwiper.reInit();
            if (data.goodsList.length < 10) {
                $(".more .tip").html("没有更多");
            } else {
                $(".more .tip").html("上拉加载更多");
            }
        },300);
    }

    if(data.goodsList.length != 0) {
        pageId ++;
    }


    nullTips();
}

function nullTips() {
    if ($(".cart").length == 0) {
        $("#emptyDiv").show();
    }
}

function deleteAll() {

    var cartFList = $(".cart");
    var dataUuid = "";
    var uuid = null;

    for (var i = 0; i < cartFList.length; i++) {
        if ($(cartFList[i]).find(".cart_F").hasClass("cart_icon_red")) {
            uuid = $(cartFList[i]).find(".uuid").val();
            dataUuid += '\\"' + uuid + '\\",';
        }
    }

    if (dataUuid == null || dataUuid == '') {
        // 弹框 showMsgAction
        setShowMsgAction("请选中至少一件宝贝");
        //alert("请选中至少一件宝贝");
    } else {

        layer.open({
            content: '确定要删除选中的商品吗？',
            btn: ['确定', '取消'],
            yes: function(index) {
                deleteAllConfirm(cartFList, dataUuid);
                layer.closeAll();
            }
        });
    }
}

function deleteAllConfirm(cartFList, dataUuid) {
    var dataUuidList = dataUuid.substring(0, dataUuid.length - 1);

    var uuid = $(this).parent().find(".uuid").val();
    data = '{"header":"00100306","userId":"' + userId + '","token":"' + token + '",'
            + '"data":"{\\"uuidList\\":[' + dataUuidList + ']}"}';

    setBehaviourAction("01509", data);

    // var thisObj =$(this).parent().parent();
    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json",
        timeout: 10000,
        dataType: 'json',
        url: urlW,
        data: data,
        type: "post",
        success: function(data) {
            if ("0002" == data.code) {
                // 跳转登录页面
                setLoginAction(data.msg);
                $("#loadingDiv").hide();
            } else if ("0000" == data.code) {
                pageId = 0;
                getData(0, 0);
                // nullTips();
            } else {
                // 弹框 showMsgAction
                setShowMsgAction(data.msg);
                $("#loadingDiv").hide();
            }
        },
        error: function() {
            // 弹框 showMsgAction
            setShowMsgAction("删除异常");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });
}

function complete() {

    var cartFList = $(".cart");
    var dataUuid = "";
    var uuid = null;
    var currNum = null;
    for (var i = 0; i < cartFList.length; i++) {
        if ($(cartFList[i]).find(".cart_F").hasClass("cart_icon_red")) {
            uuid = $(cartFList[i]).find(".uuid").val();
            currNum = $(cartFList[i]).find(".number").val();
            dataUuid += '{\\"uuid\\":\\"' + uuid + '\\",\\"count\\":\\"' + (currNum == undefined ? 1 : currNum) + '\\"},';
        } else {
            $(cartFList[i]).find(".number").val($(cartFList[i]).find(".cart_num").text());
        }
    }
    var dataUuidList = dataUuid.substring(0, dataUuid.length - 1);

    data = '{"header":"00100305","userId":"' + userId + '","token":"' + token + '",'
            + '"data":"{\\"goodsList\\":[' + dataUuidList + ']}"}';

    setBehaviourAction("01511", data);

    // var thisObj =$(this).parent().parent();
    $.ajax({
        contentType: "application/json",
        timeout: 10000,
        dataType: 'json',
        url: urlW,
        data: data,
        type: "post",
        success: function(data) {
            if ("0002" == data.code) {
                // 跳转登录页面
                setLoginAction(data.msg);
            } else if ("0000" == data.code) {
                // $(".txtred").html(data.totalAmount);
                for (var i = 0; i < cartFList.length; i++) {
                    if ($(cartFList[i]).find(".cart_F").hasClass("cart_icon_red")) {
                        currNum = $(cartFList[i]).find(".number").val();
                        $(cartFList[i]).find(".cart_num").text(currNum);
                    } else {
                        $(cartFList[i]).find(".number").val($(cartFList[i]).find(".cart_num").text());
                    }
                }
            } else {
                // 弹框 showMsgAction
                setShowMsgAction(data.msg);
            }
        },
        error: function() {
            // 弹框 showMsgAction
            // setShowMsgAction("修改异常");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });
}

function edit(id) {

    var goodsType = $(id).parent().siblings("dd").find(".goodsType").val();
    if ($(id).html() == "完成") {

        $(id).parent().siblings("dd").find(".delBtn").hide();
        $(id).html("编辑");
        if(goodsType==0)
        {
            $(id).parent().siblings("dd").find(".numberWidget").hide();
            $(id).parent().siblings("dd").find(".title_cart").show();
        }
        var thisObj = $(id).parent().next();
        var currNum = thisObj.find(".number").val();
        var uuid = thisObj.find(".uuid").val();
        var num = thisObj.find(".cart_num").text();

        if (currNum != num && currNum != undefined) {

            if (thisObj.prev().find(".cart_F").hasClass("cart_icon_red")) {
                var price = thisObj.find(".price").text();
                var plusPrice = parseFloat((parseInt(currNum) - parseInt(num)) * parseFloat(price));
                var total = parseFloat($(".txtred").html());

                $(".txtred").html((total + plusPrice).toFixed(2));
            }
            thisObj.find(".cart_num").text(currNum);
        }

        setBehaviourAction("01510");
    } else {

        $(id).parent().siblings("dd").find(".delBtn").show();
        $(id).html("完成");
        if(goodsType==0)
        {
            $(id).parent().siblings("dd").find(".numberWidget").show();
            $(id).parent().siblings("dd").find(".title_cart").hide();
        }

        setBehaviourAction("01504");
    }
}

function updateGoodsNum(thisObj, uuid, currNum, num) {
    //  下面注释的代码，可以代替后面data拼接的部分
    /* var header = {
    "header":'00100305',
    "userId":userId,
    "token":token
    }
    var goodsList = {
    "uuid":uuid,
    "count":currNum
    };
    var data = {
    "goodsList":goodsList
    };
    var dataj = JSON.stringify(data);
    header.data=dataj;

    data = JSON.stringify(header);*/

    data = '{"header":"00100305","userId":"' + userId + '","token":"' + token + '",'
            + '"data":"{\\"goodsList\\":[{\\"uuid\\":\\"' + uuid + '\\",\\"count\\":\\"' + currNum + '\\"}]}"}';

    $.ajax({
        url: urlW,
        data: data,
        type: "post",
        contentType: "application/json",
        timeout: 10000,
        dataType: 'json',
        success: function(data) {
            if ("0002" == data.code) {
                // 跳转登录页面 showMsgAction
                setLoginAction(data.msg);
            } else if ("0000" == data.code) {
                if (thisObj.prev().find(".cart_F").hasClass("cart_icon_red")) {
                    var price = thisObj.find(".price").text();
                    var plusPrice = parseFloat((parseInt(currNum) - parseInt(num)) * parseFloat(price));
                    var total = parseFloat($(".txtred").html());

                    $(".txtred").html((total + plusPrice).toFixed(2));
                }
                // $(".txtred").html(data.totalAmount);
                thisObj.find(".cart_num").text(currNum);
            } else {
                // 弹框 showMsgAction
                setShowMsgAction(data.msg);
            }
        },
        error: function() {
            // 弹框 showMsgAction
            setShowMsgAction("编辑失败");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });

    dataH5 = '{"h5Id":"01510","data":"' + data + '"}';
    setBehaviourAction(dataH5);
}

//minus
function minusB(id) {

    var currNum = $(id).siblings(".number");
    if (parseInt(currNum.val()) <= 1) {
        // 弹框 showMsgAction
        setShowMsgAction("受不了了，宝贝不能再减少了哦");
        // alert("受不了了，宝贝不能再减少了哦");
        nullTips();
    } else {
        currNum.val(parseInt(currNum.val()) - 1);

        var thisObj = $(id).parent().parent().parent().parent();
        var currNum = thisObj.find(".number").val();
        var uuid = thisObj.find(".uuid").val();
        var num = thisObj.find(".cart_num").text();
        updateGoodsNum(thisObj, uuid, currNum, num);
        //changePriceCartFB(id.next());
    }

    setBehaviourAction("01506");
}

//plus
function plusB(id) {

    var currNum = $(id).siblings(".number");
    var residueNum = $(id).siblings(".residueNum");
    if (parseInt(currNum.val()) >= parseInt(residueNum.val())) {
        // 弹框 showMsgAction
        setShowMsgAction("宝贝达到库存量");
    } else {
        currNum.val(parseInt(currNum.val()) + 1);

        var thisObj = $(id).parent().parent().parent().parent();
        var currNum = thisObj.find(".number").val();
        var uuid = thisObj.find(".uuid").val();
        var num = thisObj.find(".cart_num").text();
        updateGoodsNum(thisObj, uuid, currNum, num);
    }

    setBehaviourAction("01505");
}

//delBtn
function delBtn(id) {
    layer.open({
        content: '确定要删除该商品吗？',
        btn: ['确定', '取消'],
        yes: function(index) {
            deleteGoods(id);
            layer.closeAll();
        }
    });
}

function deleteGoods(id) {
    var uuid = $(id).parent().find(".uuid").val();
    data = '{"header":"00100306","userId":"' + userId + '","token":"' + token + '",'
            + '"data":"{\\"uuidList\\":[\\"' + uuid + '\\"]}"}';

    setBehaviourAction("01507", data);

    var thisObj = $(id).parent().parent();
    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json",
        timeout: 10000,
        dataType: 'json',
        url: urlW,
        data: data,
        type: "post",
        success: function(data) {
            if ("0002" == data.code) {
                // 跳转登录页面
                setLoginAction(data.msg);
            } else if ("0000" == data.code) {
                // $(".txtred").html(data.totalAmount);
                var numSingle = $(id).parent().find(".cart_num").text();
                console.log(numSingle);

                var priceSingle = $(id).parent().find(".price").text();
                var tatalPrice = parseFloat(priceSingle) * parseFloat(numSingle);
                var tatalPriceOld = parseFloat($(".txtred").html());
                var num = tatalPriceOld - tatalPrice;
                $(".txtred").html((num <= 0 ? 0 : num).toFixed(2));

                thisObj.remove();
                if ($(".cart").length == 0) {
                    pageId = 0;
                    getData(0, 0);
                }
            } else {
                // 弹框 showMsgAction
                setShowMsgAction(data.msg);
            }
            $("#loadingDiv").hide();
        },
        error: function() {
            // 弹框 showMsgAction
            setShowMsgAction("删除异常");
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });
}

function onBackKeyDown() {
    backIcon();
}

function backIcon() {

    var type = getQueryString("type");
    if (type == null || type == '') {
        setBackAction();
    } else {
        history.go(-1);
    }
    setBehaviourAction("01512");
}

// 结算
function closeAnAccount() {

    var cartFList = $(".cart");
    var dataUuid = "";
    var uuid = null;
    var residueNum = null; //库存
    var number = null; // 买的数量
    var title_cart = null;
    var numFlg = 0;
    var goodsType = null;
    var message = null;
    for (var i = 0; i < cartFList.length; i++) {

        if ($(cartFList[i]).find(".cart_F").hasClass("cart_icon_red")) {
            uuid = $(cartFList[i]).find(".uuid").val();
            goodsType = $(cartFList[i]).find(".goodsType").val();
            residueNum = $(cartFList[i]).find(".residueNum").val();
            number = $(cartFList[i]).find(".number").val();
            title_cart = $(cartFList[i]).find(".title_cart")[0].innerText;

            if (goodsType == 0) {
                if (parseInt(number) > parseInt(residueNum)) {
                    message = "对不起，" + title_cart + "商品库存已不足，剩余(" + residueNum + ")。"
                    // 弹框 showMsgAction
                    setShowMsgAction(message);
                    // alert(message);
                    numFlg = 1;
                    break;
                }
            }
            dataUuid += '\\"' + uuid + '\\",';
        }
    }
    if (numFlg == 1) return false;
    if (dataUuid == '') {
        // 弹框 showMsgAction
        setShowMsgAction("请选中至少一件宝贝");
        return false;
    }

    var dataUuidList = dataUuid.substring(0, dataUuid.length - 1);

    data = '{"header":"00000309","userId":"' + userId + '","token":"' + token + '",'
            + '"data":"{\\"uuidList\\":[' + dataUuidList + ']}"}';

    if(checkGoodsInfo(data)){
        localStorage.setItem("dataUuidList", dataUuidList);
        location.href = 'closeAnAccount.html';
    }

    setBehaviourAction("01508", data);
}

function checkGoodsInfo(data){

    var flag = true;

    $("#loadingDiv").show();
    $.ajax({
        contentType: "application/json", //必须有
        timeout: 10000,
        dataType:'json',
        url:urlR,
        data: data,
        type:"post",
        async: false,
        success:function(data) {
            if("0002"==data.code){
                // 跳转登录页面
                setLoginAction(data.msg);
                flag = false;
            } else if("0000"==data.code) {
                if(data.goodsList==null||data.goodsList.length==0) {
                    setShowMsgAction("系统异常");
                    flag = false;
                } else {
                    $.each(data.goodsList,function(index,item){
                        if(item.status != 1){
                            setShowMsgAction("对不起，您选择的商品已下架,请重新选择");
                            pageId = 0;
                            getData(1, 0);
                            flag = false;
                        }
                        if(item.type == 0 && item.num > item.residueNum) {
                            setShowMsgAction("对不起，您选择的商品库存已不足,请重新选择");
                            pageId = 0;
                            getData(1, 0);
                            flag = false;
                        }
                    });
                }
            } else {
                flag = false;
            }
            $("#loadingDiv").hide();
        },
        error: function(e) {
            flag = false;
            $("#loadingDiv").hide();
            $("#errorDiv").show();
        }
    });

    return flag;
}

// 单个选中
function cartFB(id) {
    changePriceCartFB(id);

    setBehaviourAction("01502");
}

function changePriceCartFB(id) {
    if (!$(id).hasClass("cart_icon_red")) {
        $(id).addClass("cart_icon_red").append("<i></i>").removeClass("cart_icon");
        var numSingle = $(id).parent().parent().parent().find(".cart_num").text();
        var priceSingle = $(id).parent().parent().parent().find(".price").text();
        var tatalPrice = parseFloat(priceSingle) * parseFloat(numSingle);
        var tatalPriceOld = parseFloat($(".txtred").html());
        $(".txtred").html((tatalPriceOld + tatalPrice).toFixed(2));
    } else {
        $(id).empty().addClass("cart_icon").removeClass("cart_icon_red");
        var numSingle = $(id).parent().parent().parent().find(".cart_num").text();
        var priceSingle = $(id).parent().parent().parent().find(".price").text();
        var tatalPrice = parseFloat(priceSingle) * parseFloat(numSingle);
        var tatalPriceOld = parseFloat($(".txtred").html());
        var num = tatalPriceOld - tatalPrice;
        $(".txtred").html((num <= 0 ? 0 : num).toFixed(2));
    }
}

function cartAllB(id) {
    var cartFList = $(".cart_F");

    if (!$(id).hasClass("cart_icon_red")) {
        $(id).addClass("cart_icon_red").append("<i></i>").removeClass("cart_icon_select_all");
        for (var i = 0; i < cartFList.length; i++) {
            if (!$(cartFList[i]).hasClass("cart_icon_red")) {
                var numSingle = $(cartFList[i]).parent().parent().parent().find(".cart_num").text();
                var priceSingle = $(cartFList[i]).parent().parent().parent().find(".price").text();
                var tatalPrice = parseFloat(priceSingle) * parseFloat(numSingle);
                var tatalPriceOld = parseFloat($(".txtred").html());
                $(".txtred").html((tatalPriceOld + tatalPrice).toFixed(2));
                $(cartFList[i]).addClass("cart_icon_red").append("<i></i>").removeClass("cart_icon_select_all");
            }
        }
    } else {
        $(id).empty().addClass("cart_icon_select_all").removeClass("cart_icon_red");
        for (var i = 0; i < cartFList.length; i++) {
            if ($(cartFList[i]).hasClass("cart_icon_red")) {
                var numSingle = $(cartFList[i]).parent().parent().parent().find(".cart_num").text();
                var priceSingle = $(cartFList[i]).parent().parent().parent().find(".price").text();
                var tatalPrice = parseFloat(priceSingle) * parseFloat(numSingle);
                var tatalPriceOld = parseFloat($(".txtred").html());
                var num = tatalPriceOld - tatalPrice;
                $(".txtred").html((num <= 0 ? 0 : num).toFixed(2));
            }
            $(cartFList[i]).empty().addClass("cart_icon_select_all").removeClass("cart_icon_red");
        }
    }

    setBehaviourAction("01503");
}

function goodsDetails(goodsIdSingle, type) {
    localStorage.setItem("goodsId", goodsIdSingle);
    var goodsUrl = null;

    if (type == 0) goodsUrl = "goods-details.html?fromHtml=true";
    else if (type == 1) goodsUrl = "goods_details_videos.html?fromHtml=true";
    else if (type == 2) goodsUrl = "goods_details_documents.html?fromHtml=true";
    location.href = goodsUrl;
}