package com.codepig.common.bean;

import java.util.List;

public class Upload_files {
    /**
     * row : 1
     * Upload_files : [{"image_id":"20bffd66eee9cb88445f7254a14b61d7","video_id":"20bffd66eee9cb88445f7254a14b61d7"},{"image_id":"20bffd66eee9cb88445f7254a14b61d7","video_id":""}]
     */

    private String row;
    private List<UploadFilesBean> upload_files;

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public List<UploadFilesBean> getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(List<UploadFilesBean> upload_files) {
        this.upload_files = upload_files;
    }

    public static class UploadFilesBean {
        /**
         * image_id : 20bffd66eee9cb88445f7254a14b61d7
         * video_id : 20bffd66eee9cb88445f7254a14b61d7
         */

        private String image_id;
        private String video_id;

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
