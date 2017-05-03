package com.htao.downloadmanager.downloader;

import java.util.Observable;

/**
 * Created by 10607 on 2017/4/30.
 */

public class DownloadChanger extends Observable{

    private static DownloadChanger instance ;

    private DownloadChanger(){

    }

    public static DownloadChanger getInstance(){
        if (instance == null) {
            synchronized (DownloadChanger.class){
                if (instance == null) {
                    instance = new DownloadChanger() ;
                }
            }
        }
        return instance ;
    }

    public void postChange(DownloadEntry downloadEntry){
        setChanged();
        notifyObservers(downloadEntry);
    }

}
