var baseUrl="http://www.sweetdonut.cn/";
//var baseUrl = "http://test.sweetdonut.cn/Donut/";
//var baseUrl = "http://10.10.10.188:8080/Donut/";
var urlW = baseUrl + "restW/";
var urlR = baseUrl + "restR/";
//插件对象
function Plugin(){
	this.contentType = "application/json"; 
	this.dataType = "json";
	this.rootUrl = urlR;
}

/***
 * 接口编码响应
 * @param {Object} code
 */
Plugin.prototype.resMessage = function( res ){

	if(!res || typeof res == 'undefined') return;
	 
	if('0000' == res.code){ //请求成功 
		 return 'ok';
	}

	if('0002' == res.code) {//未登录
		//此处调用android跳转到登陆页面
	    console.log("用户未登录，调用android登陆页面")
	    setLoginAction("");
		return;
	}
	console.log(res.msg)
	// alert(res.msg); //其他的错误提示
}

/***
 * ajax post 请求
 * @param {Object} code
 */

Plugin.prototype.AjaxPost = function( url , data , callback){
	$.ajax({  
		contentType: this.contentType, //必须有
	    dataType:    this.dataType,
	    url:	     this.rootUrl + url,
	    data:	     data,
	    type: 		 "POST",  
	    success : function(res) { 
	    	    if(this.resMessage(res) == 'ok'){
	    	    	callback && callback(res);
	    	    } 
	    	    return;
	    },
 	    error :   function(e){
 	    	console.log( e );
 	    }
	});
}


/***
 * ajax get 请求
 * @param {Object} code
 */

Plugin.prototype.AjaxGet = function(){
	$.ajax({  
		contentType: this.contentType, //必须有
	    dataType:    this.dataType,
	    url:	     url, 
	    type: 		 "GET",  
	    success : function(res) { 
	    		callback && callback(res);
	    },
 	    error :   function(e){
 	    	console.log( e );
 	    }
	});
}

/***
 * 数据缓存设置
 * @param {Object} name
 * @param {Object} value
 */
Plugin.prototype.setCache = function( name , value){
	localStorage.setItem(name , value);
}

/***
 * 获取数据缓存值,返回对象
 * @param {Object} name
 * @param {Object} 
 */
Plugin.prototype.getCache = function( name ){ 
	return eval("( "+localStorage.getItem(name) +" )");
}


/***
 * 获取数据缓存值,返回字符串
 * @param {Object} name
 * @param {Object} 
 */
Plugin.prototype.getCacheStr = function( name ){ 
	return localStorage.getItem(name);
}

/***
 * 清除对象缓存
 * @param {Object} name
 * @param {Object} 
 */
Plugin.prototype.clearCache = function(name){
	localStorage.removeItem(name);
}


var plugin = new Plugin();

/*安卓的物理的返回键*/
function onLoad() {          
    document.addEventListener("deviceready", onDeviceReady, false);
    document.addEventListener("backbutton", onBackKeyDown, false);
}
// device APIs are available
function onDeviceReady() {

}
// Handle the back button
function onBackKeyDown() {
  	history.go(-1);
}


function getQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

function setBackAction() {
    cordova.exec(
        function() {console.log("Success");},
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "backAction",
        []);
}

function readDocAction(docUrl)
{
    cordova.exec(
            function() {console.log("Success");},
            function(e) {console.log("Error: "+e);},
            "DonutPlugin",
            "readDocAction",
            [docUrl]);
}

function goodsListAction(goodsListActionCalBack)
{
    cordova.exec(
        function(info) {
            goodsListActionCalBack(info);
        },
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "goodsListAction",
        []);
}

function payAction(data,checkPayInfo)
{
    cordova.exec(
        function(info){ checkPayInfo(info); },
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "payAction",
        [data]
    );
}

function goodsAction(goodsActionCalBack)
{
    cordova.exec(
        function(info) {
            localStorage.setItem("goodsId", info);
            goodsActionCalBack(info);
        },
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "goodsAction",
        []);
}

function orderAction()
{
    cordova.exec(
        function() {console.log("Success");},
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "orderAction",
        []);
}

function setBehaviourAction(h5Id, requestData) {

    var json = {};
    json.h5Id = h5Id;
    json.data = requestData;

try{
    cordova.exec(
        function() {console.log("Success");},
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "behaviourAction",
        [json]);
        }catch(e){
        console.log(e);
        }
}

function setLoginAction(msg) {
    if(msg == null || msg == '') {
        msg = "未登录，请先登录";
    } else {
        msg = "0002";
    }
    cordova.exec(
        function(userinfo) {
            var info = userinfo.split(",");
            if(info!=null&&info.length==3)
            {
                userId = info[0];
                token = info[1];
                vipFlg = info[2];
                localStorage.setItem("userId",userId);
                localStorage.setItem("token",token);
                localStorage.setItem("vipFlg",vipFlg);
            }else
            {
                localStorage.setItem("userId","");
                localStorage.setItem("token","");
                localStorage.setItem("vipFlg","");
            }
        },
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "loginAction",
        [msg]);
}

function setUserInfoAction(goonFun) {
    cordova.exec(
        function(userinfo) {
            var info = userinfo.split(",");
            if(info!=null&&info.length==3)
            {
                userId = info[0];
                token = info[1];
                vipFlg = info[2];
                localStorage.setItem("userId",userId);
                localStorage.setItem("token",token);
                localStorage.setItem("vipFlg",vipFlg);
            }else
            {
                localStorage.setItem("userId","");
                localStorage.setItem("token","");
                localStorage.setItem("vipFlg","");
            }
            if(goonFun!=null){
                goonFun();
            }
        },
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "userInfoAction",
        []);
}

function setShowMsgAction(msg) {
    cordova.exec(
        function() {console.log("Success");},
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "showMsgAction",
        [msg]);
}

function setAddAddressAction(hasAddress) {
    cordova.exec(
        function() {console.log("Success");},
        function(e) {console.log("Error: "+e);},
        "DonutPlugin",
        "addAddressAction",
        ['{"hasAddress": '+hasAddress+'}']);
}

function checkMember(){
    var checkFlg = localStorage.getItem("vipFlg");
    if(checkFlg != "1")
    {
        layer.open({
            content: '对不起，您还不是会员，不能购买此商品',
            btn: ['去开通', '取消'],
            yes: function(index){
				layer.closeAll();
                cordova.exec(
                    function(info) {
						if (info == "1"){
							localStorage.setItem("vipFlg",info);
						}

                    },
                    function(e) {console.log("Error: "+e);},
                    "DonutPlugin",
                    "vipAction",
                    []);
            },
            no: function(index) {
              layer.closeAll();
            }
         });
        return false;
    }
    return true;
}

function checkLogin()
{
   var checkUserId = localStorage.getItem("userId");
   if(checkUserId == null || checkUserId == ''){
        setLoginAction();
        return false;
   }
   return true;
}

function bindingPhone()
{
	layer.open({
            content: '为保证竞拍的真实性，绑定手机才可参与',
            btn: ['去绑定', '取消'],
            yes: function(index){

                layer.closeAll();
                cordova.exec(
                    function(info) {
                    },
                    function(e) {console.log("Error: "+e);},
                    "DonutPlugin",
                    "bindingPhone",
                    []);
            },
            no: function(index) {
              layer.closeAll();
            }
         });
}

function getAuctionRecordId(callBack)
{
    cordova.exec(
	    function(info) {
			localStorage.setItem("d10Id",info);
			callBack();
	    },
	    function(e) {console.log("Error: "+e);},
	    "DonutPlugin",
	    "AuctionRecordAction",[]
	);
}