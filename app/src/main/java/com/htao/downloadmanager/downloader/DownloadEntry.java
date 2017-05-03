package com.htao.downloadmanager.downloader;

import java.io.Serializable;

/**
 * Created by 10607 on 2017/4/30.
 */

public class DownloadEntry implements Serializable {
    public String id;
    public String downloadUrl;
    public String name;

    public DownloadEntry(){

    }

    public DownloadEntry(String url) {
        this.downloadUrl = url;
        this.id = url;
        this.name = url.substring(url.lastIndexOf("/") + 1);
    }

    public enum DownloadStatus {
        idle, waiting,connecting, downloading, paused, resumed, cancelled, completed,error
    }

    public DownloadStatus mDownloadStatus=DownloadStatus.idle;

    public int curLength;
    public int totalLength;

    @Override
    public String toString() {
        return "DownloadEntry: " + downloadUrl + " is " + mDownloadStatus.name() + " with " + curLength + "/" + totalLength;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode()==this.hashCode();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
