var labelArr = [['实物','明星商品'],['数据','文档']];
var pageIndexArr = [0,0];
var divIdArr = ['spsw','sjbg'];
var timer;
var type = -1, goodsSelectType = -1;
var userId = "";
var searchInput = -1;
var fkB01;

var vSwiper = new Array();

document.addEventListener("deviceready", getInitData, false);
document.addEventListener("backbutton", back, false);
function back() {
    setBehaviourAction("01105");
    setBackAction();
}

function getInitData() {
    // 获取登录信息
	goodsListAction(goodsListActionCalBack);

    //获取专题Id,若获取不到,则为会员专享.
}

function goodsListActionCalBack(info)
{
	fkB01 = info;

	if(fkB01 != null && fkB01 != "") {
		searchInput = -1;
	} else {
		searchInput = -2;
		$(".ui-navbar-text").text("会员专享");
	}

    setUserInfoAction(initUserId);
}

function initUserId()
{
	userId = localStorage.getItem("userId");
	getInfo();
}

function getInfo(){

	//下拉刷新
	loadSpswData(true);

	wrapperSwipe();
}

var pageId=0;//加载更多
var nav;
function wrapperSwipe(){

   	//Init Navigation
   	nav = $('.swiper-nav').swiper({
   		slidesPerView: 'auto',
   		freeMode:true,
   		freeModeFluid:true,
   		onSlideClick: function(nav){
   			pages.swipeTo( nav.clickedSlideIndex );
   			$(".tabs .active").removeClass('active');
            $('.tabs .swiper-slide').eq(nav.clickedSlideIndex).addClass('active');
   		}
   	})

   	$(window).on('resize',function(){
   		fixPagesHeight();
   	})
   	fixPagesHeight();

   	//Init Pages
   	var pages = $('.swiper-pages').swiper(
   	{
   		onSlideChangeStart: function(){
     		$(".tabs .active").removeClass('active');
     		$(".tabs a").eq(pages.activeIndex).addClass('active');
     		//加载中提示。。
		   console.log("=====================start======================");
     	},
     	onSlideChangeEnd: function(swiper){
           type = swiper.activeIndex;
           if(type == 0){
               goodsSelectType = 0;
           } else if(type ==1){
               goodsSelectType = 2;
           }
           loadSpswData(true);
		   console.log("======================end=========================");
        }
   	});

   	//Scroll Containers
	$('.scroll-container').each(function(index){
		vSwiper[index] = $(this).swiper({
   			mode:'vertical',
   			scrollContainer: true,
   			mousewheelControl: true,
   			scrollbar: {
   				container:$(this).find('.swiper-scrollbar')[0]
   			},
   			onTouchMove:function(swiper){
				var scrollTop = swiper.positions.current;
				var winHeight = vSwiper[type].height;
				var totalHeight = $('.content-slide').eq(type).height();
				var liSize = $(".content-slide").eq(type).find("ul li").size();
				if(scrollTop>0){
					$(".load").show();
					$(".load .tip").html("下拉刷新");
					if(scrollTop>60)
					{
						$(".load .tip").html("松开开始刷新");
					}
				}else if(liSize>9&&(winHeight<totalHeight?(Math.abs(scrollTop)+winHeight>totalHeight):(Math.abs(scrollTop)>60)))
				{
					$(".more").show();
					$(".more .tip").html("上拉加载更多");
					if((Math.abs(scrollTop)+winHeight-totalHeight)>60)
					{
						$(".more .tip").html("松开开始加载");
					}
				}

			},
			onTouchEnd:function(swiper){
					var scrollTop = swiper.positions.current;
					var winHeight = vSwiper[type].height;
					var totalHeight = $('.content-slide').eq(type).height();

					var liSize = $(".content-slide").eq(type).find("ul li").size();
					if(scrollTop>60){
						$(".load .tip").html("正在刷新...");
						$(".load").css({marginTop:0});
						loadSpswData();
					}else if(liSize>9&&(winHeight<totalHeight?(Math.abs(scrollTop)+winHeight>totalHeight):(Math.abs(scrollTop)>60)))
					{
						if($(".content-slide").eq(type).find("ul li").size()==0){
							loadSpswData();
							return;
						}
						$(".more .tip").html("正在加载");
						getData(type,pageIndexArr[type]);
					}
			}
   		});
	});
}

//Function to Fix Pages Height
function fixPagesHeight() {
    $('.swiper-pages').css({
        height: $(window).height() - $(".ui-page-navbar").height()
    });
}

function showDetail(goodsKind,type,goodsId){
	if(goodsKind=="1"){
		setBehaviourAction("01106");
		localStorage.setItem("goodsId",goodsId);
	    window.location.href='goods-details_auction.html?goodsId='+goodsId+'&fromHtml=true';
	}else if(goodsKind == "0"){
		setBehaviourAction("01104");
		if(type=="0"){
			localStorage.setItem("goodsId",goodsId);
		    window.location.href='goods-details.html?goodsId='+goodsId+'&fromHtml=true';
		} else if(type=="1"){
			localStorage.setItem("goodsId",goodsId);
		    window.location.href='goods_details_videos.html?goodsId='+goodsId+'&fromHtml=true';
		} else if(type=="2"){
			localStorage.setItem("goodsId",goodsId);
		    window.location.href='goods_details_documents.html?goodsId='+goodsId+'&fromHtml=true';
		}
	}
}

function buildGoodsItems(item)
{
	var  box = '<li>';
	if(item.goodsKind == 1){
        box += '<em class="auction_icon"><img src="images/auction_good.png"></em>';
    }else{ 
       if(item.promotionType == 0){
        box += '<em class="discount"><img src="images/discount.png"><i>折扣</i></em>';
		}else if(item.promotionType!=0){
			box += '<em class="discount"></em>';
		}
    }
    
    box += '<div class="cont_box"><h4><span>'
            + labelArr[type][0]
            + '</span><i>/</i><em>'
            + labelArr[type][1]
            + '</em></h4>';
    if(item.needMember == 1){
        box += '<div class="content-slide_img page_f">';
    } else {
        box += '<div class="content-slide_img">';
    }
    box += '<a href="javascript:void(0)" onclick="showDetail('+item.goodsKind+',' + goodsSelectType + ',\'' + item.goodsId + '\')">'
            + '<img src="' + item.thumbnail + '" class="lazyload"/></a>';
    if(item.needMember==1){
        box += '<i class="more_v"><img src="images/more_v.png"></i></div>';
    } else {
        box += '</div>';
    }
    box += ' <span class="content-slide_mon">';
    if(item.goodsKind==0){
        if(item.needMember == 1){
            box += ' <i>¥</i> '+ item.memberPrice ;
        } else {
            box += ' <i>¥</i> '+ item.price ;
        }
    }else if(item.goodsKind==1)
    {
        box += ' <i style="color:#FEAF68">¥'+ item.auctionCurrentPrice+'</i> ';
    }
    box += '</span><p> '
            + item.description
            + ' </p></div>';
    if(item.goodsKind == 0){
        if(item.surplusNum==0){
            box += '<div class="front_bg" onclick="showDetail('+item.goodsKind+',' 
                    +goodsSelectType+',\''+item.goodsId+'\')"><span><i>暂时无货</i></span></div>';
        }
    }else if(item.goodsKind == 1){
        if(item.auctionStatus>0){
            box += '<div class="front_bg" onclick="showDetail('+item.goodsKind+',' 
                    +goodsSelectType+',\''+item.goodsId+'\')"><span><i>竞拍结束</i></span></div>';
        }else if(item.auctionStatus<0){
            box += '<div class="front_bg" onclick="showDetail('+item.goodsKind+',' 
                    +goodsSelectType+',\''+item.goodsId+'\')"><span><i>即将开始</i></span></div>';
        }
    }
    box+= '</li>';
    return box;
}

function loadSpswData(flag){
	var successFn =
	    function(data) {
	    	// console.log(data);
	        var box = "";
	    	$.each(data.goodsList,function(index,item){
				vSwiper[type].reInit();
				box += buildGoodsItems(item);
		    });

			$('.content-slide').css({
				height: "auto"
			});
			if(data.goodsList.length>0){
				pageIndexArr[type] = 1;
				$("#"+divIdArr[type]).empty().append(box);
			}
			setTimeout(function(){
					$(".load").animate({marginTop:-40},200,function(){
					$(".load").hide();
				});
            },500);
			resizeContentHeight();
			vSwiper[type].reInit();
			$("#loadingDiv").hide();
			if(data.goodsList == null || data.goodsList.length <= 0) {
			    $("#emptyDiv").show();
			}
	    };

	var h5Code = "01100";
    switch(type){
        case -1:
            type = 0;
            break;
        case 0:
            h5Code = "01102";
            break;
        case 1:
            h5Code = "01101";
            break;
        case 2:
            h5Code = "01103";
            break;
    }

    if(type == 0){
        goodsSelectType = 0;
    } else if(type ==1){
        goodsSelectType = 2;
    }

    var data = '{"userId":"'+userId+'","header":"00010301","data":"{type:'+goodsSelectType+',requestType:'+1+',searchInput:'+searchInput+',fkB01:\\"'+fkB01+'\\"'+',page:0'+',rows:10}"}';

	ajax(urlR, data, successFn, flag);

	setBehaviourAction(h5Code, data);
}

function resizeContentHeight()
{
	var fullHeight = $(window).height() - $(".ui-page-navbar").height()
	var currentHeight = $('.content-slide').eq(type).height();
	if(fullHeight>currentHeight)
	{
		$('.content-slide').eq(type).css({
			height: fullHeight
		});
	}
}

function ajax(urlR,data,successFn, flag){
    if(flag) {
        $("#loadingDiv").show();
    }
    $("#emptyDiv").hide();
	$.ajax({
	    contentType: "application/json", //必须有
	    timeout: 10000,
		url:urlR,
		data:data  ,
		type:"post",
		success:successFn,
		error:function(){
            $("#loadingDiv").hide();
            $("#errorDiv").show();
            //alert("请求数据失败");
        }
	});
}

function getData(type,pageNum){
//	console.log(321);
    if(type == 0){
        goodsSelectType = 0;
    } else if(type ==1){
        goodsSelectType = 2;
    }
	$.ajax({
		contentType: "application/json", //必须有
		timeout: 10000,
		url:urlR,
		data:'{"userId":"'+userId+'","header":"00010301","data":"{type:'+goodsSelectType+',requestType:'+1+',searchInput:'+searchInput+',fkB01:\\"'+fkB01+'\\"'+',page:'+pageNum+',rows:10}"}'  ,
		type:"post",
		success:function(data) {
	        var box = "";
	    	$.each(data.goodsList, function(index,item) {
				box += buildGoodsItems(item);
		    });

			$('.content-slide').css({
				height: "auto"
			});
			setTimeout(function(){
				if(pageNum==0){
					$("#"+divIdArr[type]).empty().append(box);
				} else {
					$("#"+divIdArr[type]).append(box);
				}
				resizeContentHeight();
				var fullHeight = $(window).height() - $(".ui-page-navbar").height()
				var currentHeight = $('.content-slide').eq(type).height();
				if(currentHeight>fullHeight)
				{
					if(data.goodsList.length<10){
						$(".more .tip").html("没有更多");
					}else
					{
						$(".more .tip").html("上拉加载更多");
					}
					vSwiper[type].reInit();
				} else {
					$(".more .tip").html("没有更多");
					setTimeout(function(){
						$(".more").hide();
						vSwiper[type].reInit();
					},400);
				}
            },300);
			if(data.goodsList.length>0){
				pageIndexArr[type]++;
			}
	    }
	});
}
