package com.everyone.net.bean;

public class TopicInfoBean {
    /**
     * topic_id : 1
     * topic_title : 第一次发帖试试看
     * topic_type : 2
     * topic_cover : https://everyone-1256261891.cos.ap-beijing.myqcloud.com/20191102/F8650859CAFF3C92460B0A1E1487E7AA.jpg
     * topic_content : https://everyone-1256261891.cos.ap-beijing.myqcloud.com/20191102/B7D114251F0A49DA44CD6AA6D7C11C4B.jpg
     * topic_outside :
     * topic_create_time : 1574607377
     * is_elite : true
     * total_read : 2
     * total_comment : 2
     */

    private int topic_id;
    private String topic_title;
    private int topic_type;
    private String topic_cover;
    private String topic_content;
    private String topic_outside;
    private String author_name;
    private String author_avatars;
    private String topic_desc;
    private long topic_create_time;
    private long create_time;
    private boolean is_elite;
    private boolean is_thumb;
    private int total_read;
    private int total_comment;
    private int thumb_count;

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public int getThumb_count() {
        return thumb_count;
    }

    public void setThumb_count(int thumb_count) {
        this.thumb_count = thumb_count;
    }

    public String getTopic_desc() {
        return topic_desc;
    }

    public void setTopic_desc(String topic_desc) {
        this.topic_desc = topic_desc;
    }

    public String getAuthor_avatars() {
        return author_avatars;
    }

    public void setAuthor_avatars(String author_avatars) {
        this.author_avatars = author_avatars;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public boolean isIs_thumb() {
        return is_thumb;
    }

    public void setIs_thumb(boolean is_thumb) {
        this.is_thumb = is_thumb;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public int getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(int topic_type) {
        this.topic_type = topic_type;
    }

    public String getTopic_cover() {
        return topic_cover;
    }

    public void setTopic_cover(String topic_cover) {
        this.topic_cover = topic_cover;
    }

    public String getTopic_content() {
        return topic_content;
    }

    public void setTopic_content(String topic_content) {
        this.topic_content = topic_content;
    }

    public String getTopic_outside() {
        return topic_outside;
    }

    public void setTopic_outside(String topic_outside) {
        this.topic_outside = topic_outside;
    }

    public long getTopic_create_time() {
        return topic_create_time;
    }

    public void setTopic_create_time(long topic_create_time) {
        this.topic_create_time = topic_create_time;
    }

    public boolean isIs_elite() {
        return is_elite;
    }

    public void setIs_elite(boolean is_elite) {
        this.is_elite = is_elite;
    }

    public int getTotal_read() {
        return total_read;
    }

    public void setTotal_read(int total_read) {
        this.total_read = total_read;
    }

    public int getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(int total_comment) {
        this.total_comment = total_comment;
    }
}
