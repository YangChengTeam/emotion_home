package com.yc.emotion.home.mine.domain.bean;

/**
 * Created by suns  on 2020/8/26 10:16.
 */
public class RewardInfo {


    /**
     * money : null
     * has_permission : 0
     * count : 0
     * code :
     */

    private float money;
    private int has_permission;
    private int count;
    private String code;
    private String cover;
    private int has_bind;

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getHas_permission() {
        return has_permission;
    }

    public void setHas_permission(int has_permission) {
        this.has_permission = has_permission;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getHas_bind() {
        return has_bind;
    }

    public void setHas_bind(int has_bind) {
        this.has_bind = has_bind;
    }
}
