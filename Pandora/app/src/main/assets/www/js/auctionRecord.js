var nav;
var timer;
var userId = "";
var token = "";
var vipFlg = "";
var vSwiper;
var pageNum = 0;
var rows = 10;
document.addEventListener("deviceready", initData, false);
document.addEventListener("backbutton", onBackKeyDown, false);
function initData()
{
    userId = localStorage.getItem("userId");
    token = localStorage.getItem("token");
    vipFlg = localStorage.getItem("vipFlg");
	pageNum = 0;
	setBehaviourAction("08400",'{"userId":"'+userId+'","header":"00010316","data":"{goodsId:'+getQueryString("goodsId")+',page:'+pageNum+',rows:'+rows+'}"}');
	getData(pageNum);
	wrapperSwipe();
}

function wrapperSwipe(){

   	//Init Navigation
   	nav = $('.swiper-nav').swiper({
   		slidesPerView: 'auto',
   		freeMode:true,
   		freeModeFluid:true,
   		onSlideClick: function(nav){
   		}
   	})

   	$(window).on('resize',function(){
   		fixPagesHeight();
   	})
   	fixPagesHeight();
    resizeContentHeight();

   	//Init Pages
   	var pages = $('.swiper-pages').swiper(
   	{
   		onSlideChangeStart: function(){
     		$(".tabs .active").removeClass('active');
     		$(".tabs a").eq(pages.activeIndex).addClass('active');
     		//加载中提示。。
     	},
     	onSlideChangeEnd: function(swiper){

        }
   	});

   	//Scroll Containers
	$('.scroll-container').each(function(index){
		vSwiper = $(this).swiper({
   			mode:'vertical',
   			scrollContainer: true,
   			mousewheelControl: true,
   			scrollbar: {
   				container:$(this).find('.swiper-scrollbar')[0]
   			},
   			onTouchMove:function(swiper){
				var scrollTop = swiper.positions.current;
				var winHeight = vSwiper.height;
				var totalHeight = $('.content-slide').height();
				var liSize = $(".content-slide").find("ul li").size();
				if(scrollTop>0){
					$(".load").show();
					$(".load .tip").html("下拉刷新");
					if(scrollTop>60)
					{
						$(".load .tip").html("松开开始刷新");
					}
				}else if(liSize>(rows-1)&&(winHeight<totalHeight?(Math.abs(scrollTop)+winHeight>totalHeight):(Math.abs(scrollTop)>60)))
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
					var winHeight = vSwiper.height;
					var totalHeight = $('.content-slide').height();

					var liSize = $(".content-slide").find("ul li").size();
					if(scrollTop>60){
						getData(0);
						$(".load .tip").html("正在刷新...");
						$(".load").css({marginTop:0});
					}else if(liSize>(rows-1)&&(winHeight<totalHeight?(Math.abs(scrollTop)+winHeight>totalHeight):(Math.abs(scrollTop)>60))){
						getData(pageNum+1);
						$(".more .tip").html("正在加载");
					}
			}
   		});
	});
}


function getData(requestNum){
//	console.log(321);
	$.ajax({
		contentType: "application/json", //必须有
		timeout: 10000,
		url:urlR,
		data:'{"userId":"'+userId+'","header":"00010316","data":"{goodsId:'+getQueryString("goodsId")+',page:'+requestNum+',rows:'+rows+'}"}',
		type:"post",
		success:function(data) {
			box = "";
			if(data.auctionLogsList!=null&&data.auctionLogsList.length>0)
			{
				pageNum = requestNum;
	            $.each(data.auctionLogsList,function(index,item){
	            	var headPic = "";
	            	if(item.headPic!=null&&item.headPic!="")
	            	{
	            		headPic="<img src=\""+item.headPic+"\"></img>";
	            	}else
	            	{
	            		headPic="<img src=\"images/default_head.png\"></img>";
	            	}
	            	
	            	var nickName = "小麦穗";
	            	if(item.nickName!=null&&item.nickName!="")
	            	{
	            		nickName=item.nickName;
	            	}
	                box += '<li>';
	                box += '<span class="ar_img">';
	                box += '<i class="arImg">'+headPic+'</i>';
	    			box += '</span>';
	    			box += '<div class="ar_cont">';
	    			box += '<span class="arCont_title">'
	                		+ nickName +'</span>';
	                box += '<span class="arCont_time">'
	                		+ item.createTime +'</span>';
	    			box += '</div>';
	    			box += '<span class="ar_money">¥<i>'
	                		+ item.auctionResultPrice +'</i></span>';
	    			box += '</li>';
	     		});
	     		
     		}
			$('.content-slide').css({
				height: "auto"
			});
 			if(requestNum==0){
				$("#ar_box").empty().append(box);
				setTimeout(function(){
						$(".load").animate({marginTop:-40},200,function(){
						$(".load").hide();
						vSwiper.reInit();
					});
	            },500);
				resizeContentHeight();
				vSwiper.reInit();
				dealMoreTip(data.auctionLogsList.length);
			}else{
				setTimeout(function(){
					$("#ar_box").append(box);
					vSwiper.reInit();
					dealMoreTip(data.auctionLogsList.length)
		        },300);
			}
	    }
	});
}

function dealMoreTip(length)
{
	var fullHeight = $(window).height() - $(".ui-page-navbar").height()
	var currentHeight = $('.content-slide').height();
	if(currentHeight>fullHeight)
	{
		if(length<rows){
			$(".more .tip").html("没有更多");
		}else
		{
			$(".more .tip").html("上拉加载更多");
		}
	}else
	{
		$(".more .tip").html("");
	}
}
//Function to Fix Pages Height
function fixPagesHeight() {
    $('.swiper-wrapper').css({
       height: $(window).height() - $(".ui-page-navbar").height()
    });
}
function resizeContentHeight()
{
	var fullHeight = $(window).height() - $(".ui-page-navbar").height()
	var currentHeight = $('.content-slide').height();
	if(fullHeight>currentHeight)
	{
		$('.content-slide').css({
			height: fullHeight
		});
	}
}


function onBackKeyDown() {
   setBehaviourAction("08401");
   history.go(-1);
}