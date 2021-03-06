package com.donut.app.http.message;

import java.util.List;

public class RewardIncomeListResponse extends BaseResponse
{
    private List<RewardIncomeListDetail> rewardIncomeList;

    private float totalIncome;

    private float totalPresentNum;

    private float balance;

    /**
     * @return rewardIncomeList
     */
    public List<RewardIncomeListDetail> getRewardIncomeList()
    {
        return rewardIncomeList;
    }

    /**
     * @param rewardIncomeList
     *            the rewardIncomeList to set
     */
    public void setRewardIncomeList(
            List<RewardIncomeListDetail> rewardIncomeList)
    {
        this.rewardIncomeList = rewardIncomeList;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }

    public float getTotalPresentNum() {
        return totalPresentNum;
    }

    public void setTotalPresentNum(float totalPresentNum) {
        this.totalPresentNum = totalPresentNum;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
