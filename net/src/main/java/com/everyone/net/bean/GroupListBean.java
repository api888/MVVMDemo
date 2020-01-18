package com.everyone.net.bean;

/**
 * 社群列表项信息
 */
public class GroupListBean {
    /**
     * id : 1
     * name : 大家社群
     * logo : https://everyone-1256261891.cos.ap-beijing.myqcloud.com/20191102/A68D42FBFC68E879B2661BB7A18E824D.jpg
     * cover : https://everyone-1256261891.cos.ap-beijing.myqcloud.com/20191102/F8650859CAFF3C92460B0A1E1487E7AA.jpg
     * desc : 大家开发群
     * topic_item : {"topic_id":1,"topic_title":"第一次发帖试试看","topic_type":2,"topic_cover":""}
     * mall_item : null
     */

    private int id;
    private String name;
    private String logo;
    private String cover;
    private String desc;
    private TopicInfoBean topic_item;
    private MallInfoBean mall_item;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TopicInfoBean getTopic_item() {
        return topic_item;
    }

    public void setTopic_item(TopicInfoBean topic_item) {
        this.topic_item = topic_item;
    }

    public MallInfoBean getMall_item() {
        return mall_item;
    }

    public void setMall_item(MallInfoBean mall_item) {
        this.mall_item = mall_item;
    }
}
