package com.codepig.common.bean;

import java.io.Serializable;

/**
* @description 发帖内容
* @author chenbin
* @date 2018/11/22 16:24
* @version v3.2.0
*/
public class PostContent {
    private String note;        //内容

    private String[] imgs;      //图片ID格式

    private  VideoInfo videoinfo;

    public VideoInfo getVideoinfo() {
        return videoinfo;
    }

    public void setVideoinfo(VideoInfo videoinfo) {
        this.videoinfo = videoinfo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }

    public static class VideoInfo implements Serializable {
        private String position;
        private String image_id;
        private String video_id;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getImage_id() {
            return image_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }
    }
}
