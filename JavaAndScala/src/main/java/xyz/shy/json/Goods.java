package xyz.shy.json;

/**
 * Created by Shy on 2018/5/28
 */

public class Goods {
    private Integer goodsId;
    private String data;
    //商品是否可用标志，true可用，false不可用
    private Boolean status;
    //数据插入或者更新时间
    private String ctime;
    //商品版本号
    private Long version;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Goods() {
    }

    public Goods(Integer goodsId, String data, Boolean status, String ctime, Long version) {
        this.goodsId = goodsId;
        this.data = data;
        this.status = status;
        this.ctime = ctime;
        this.version = version;
    }
}
