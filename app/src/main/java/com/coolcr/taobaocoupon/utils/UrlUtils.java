package com.coolcr.taobaocoupon.utils;

public class UrlUtils {
    public static String createHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String pict_url, int size) {
        return "https:" + pict_url + "_" + size + "x" + size + ".jpg";
    }

    public static String getCoverPath(String pict_url) {
        return "https:" + pict_url;
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        } else {
            return "https:" + url;
        }
    }

    /**
     * 拼接精选分类url
     *
     * @param favorites_id
     * @return
     */
    public static String getSelectedPageContentUrl(int favorites_id) {
        return "recommend/" + favorites_id;
    }
}
