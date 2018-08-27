package com.ps.searchEngines;

import com.ps.Page.UrlInfo;

import java.net.InetSocketAddress;
import java.util.ArrayList;


public interface Find {
    ArrayList<UrlInfo> findUrl(String requestName, InetSocketAddress inetSocketAddress) throws Exception;
    ArrayList<Picture> findPicture(String requestName, int numberPicture, InetSocketAddress inetSocketAddress);
    ArrayList<String> findYouTube(String requestName, int numberAds, InetSocketAddress inetSocketAddress) throws Exception;
}
