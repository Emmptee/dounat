package com.donut.app.http.message;

import java.util.List;

public class InitOpenMemberResponse extends BaseResponse {
    private String headPic;

    private String nickName;

    private Integer memberStatus;

    private float balance;

    private String expireTime;

    private List<MemberDetail> memberList;

    public class MemberDetail {
        private String uuid;

        private String name;

        private Float price;

        private Integer lastDays;

        private String description;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Integer getLastDays() {
            return lastDays;
        }

        public void setLastDays(Integer lastDays) {
            this.lastDays = lastDays;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public List<MemberDetail> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberDetail> memberList) {
        this.memberList = memberList;
    }

}
