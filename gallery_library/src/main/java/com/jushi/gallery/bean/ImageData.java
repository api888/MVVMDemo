package com.jushi.gallery.bean;

import java.io.Serializable;

/**
 * @author huscarter.
 * @title 图片实体类
 * @description
 * @date 9/17/2015.
 */
public class ImageData implements Serializable {
    private String path;
    private String videoPath;
    private boolean selected = false;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
