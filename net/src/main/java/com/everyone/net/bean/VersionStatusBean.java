package com.everyone.net.bean;

public class VersionStatusBean {
    /**
     * version_id : 1
     * version : 1.0.100.10
     * download_url : https://everyone-1256261891.cos.ap-beijing.myqcloud.com/20191102/everyone.apk
     * mobile_os : android
     * examine : false
     * create_time : 1574816498812
     */

    private int version_id;
    private String version;
    private String download_url;
    private String mobile_os;
    private boolean examine;
    private long create_time;

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getMobile_os() {
        return mobile_os;
    }

    public void setMobile_os(String mobile_os) {
        this.mobile_os = mobile_os;
    }

    public boolean isExamine() {
        return examine;
    }

    public void setExamine(boolean examine) {
        this.examine = examine;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
