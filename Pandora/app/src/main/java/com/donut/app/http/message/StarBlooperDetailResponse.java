package com.donut.app.http.message;

import java.util.List;

public class StarBlooperDetailResponse extends BaseResponse {

    private String trailersPic;

    private String starName;

    private List<BlooperItem> starTidbitVideos;

    public String getTrailersPic() {
        return trailersPic;
    }

    public void setTrailersPic(String trailersPic) {
        this.trailersPic = trailersPic;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public List<BlooperItem> getStarTidbitVideos() {
        return starTidbitVideos;
    }

    public void setStarTidbitVideos(List<BlooperItem> starTidbitVideos) {
        this.starTidbitVideos = starTidbitVideos;
    }

    public class BlooperItem {
        private String b02Id;

        private String headPic;

        private String title;

        private String playUrl;

        private int browseTimes;

        private String b02Thumbnail;

        public String getB02Id() {
            return b02Id;
        }

        public void setB02Id(String b02Id) {
            this.b02Id = b02Id;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public int getBrowseTimes() {
            return browseTimes;
        }

        public void setBrowseTimes(int browseTimes) {
            this.browseTimes = browseTimes;
        }

        public String getB02Thumbnail() {
            return b02Thumbnail;
        }

        public void setB02Thumbnail(String b02Thumbnail) {
            this.b02Thumbnail = b02Thumbnail;
        }
    }
}
