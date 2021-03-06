package com.donut.app.http.message;

import java.util.List;

public class MyOrderResponse extends BaseResponse
{
    private List<MyOrder> orderList;

	public List<MyOrder> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<MyOrder> orderList) {
		this.orderList = orderList;
	}

	public class MyOrder
    {
        private String subjectId;

		private String orderId;

		private String expressOrder;

		private String goodsId;

        private String subjectName;

        private String goodName;

		private String staticUrl;

		private Integer type;

		private Float price;

		private Float freight;

		private Integer num;

        private String thumbnailUrl;

        private String createTime;
        
        private float payAmount;

        /**
		 * 0：未支付；1：已支付(未发货)；2：已退款；3：已提现；4已取消'; 5已发货
		 */
        private Integer payStatus;
		/**
         * '0：未发货；1：已发货；2：确认收货';
		 */
		private Integer expressStatus;

		public String getSubjectId() {
			return subjectId;
		}

		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}

		public String getSubjectName() {
			return subjectName;
		}

		public void setSubjectName(String subjectName) {
			this.subjectName = subjectName;
		}

		public String getGoodsId() {
			return goodsId;
		}

		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}

		public String getGoodName() {
			return goodName;
		}

		public void setGoodName(String goodName) {
			this.goodName = goodName;
		}

		public String getStaticUrl() {
			return staticUrl;
		}

		public void setStaticUrl(String staticUrl) {
			this.staticUrl = staticUrl;
		}

		public String getThumbnailUrl() {
			return thumbnailUrl;
		}

		public void setThumbnailUrl(String thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public float getPayAmount() {
			return payAmount;
		}

		public void setPayAmount(float payAmount) {
			this.payAmount = payAmount;
		}

		public Integer getPayStatus() {
			return payStatus;
		}

		public void setPayStatus(Integer payStatus) {
			this.payStatus = payStatus;
		}

		public String getExpressOrder() {
			return expressOrder;
		}

		public void setExpressOrder(String expressOrder) {
			this.expressOrder = expressOrder;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public Float getPrice() {
			return price;
		}

		public void setPrice(Float price) {
			this.price = price;
		}

		public Float getFreight() {
			return freight;
		}

		public void setFreight(Float freight) {
			this.freight = freight;
		}

		public Integer getNum() {
			return num;
		}

		public void setNum(Integer num) {
			this.num = num;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public Integer getExpressStatus() {
			return expressStatus;
		}

		public void setExpressStatus(Integer expressStatus) {
			this.expressStatus = expressStatus;
		}
	}
}
