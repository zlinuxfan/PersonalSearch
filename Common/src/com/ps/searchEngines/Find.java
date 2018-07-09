package com.ps.searchEngines;

import com.UrlInfo;

import java.net.InetSocketAddress;
import java.util.ArrayList;


public interface Find {
    ArrayList<UrlInfo> find(String requestName, InetSocketAddress inetSocketAddress) throws Exception;
    ArrayList<Picture> findPicture(String requestName, int numberPicture, InetSocketAddress inetSocketAddress);
    ArrayList<String> findYouTube(String requestName, int numberAds, InetSocketAddress inetSocketAddress) throws Exception;
}
