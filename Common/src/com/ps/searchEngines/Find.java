package com.ps.searchEngines;

import com.ps.Page.UrlInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;


public interface Find {
    ArrayList<UrlInfo> findUrls(String requestName, InetSocketAddress inetSocketAddress) throws Exception;
    ArrayList<Picture> findPicture(String requestName, int numberPicture, InetSocketAddress inetSocketAddress) throws IOException;
    ArrayList<String> findYouTube(String requestName, int numberAds, InetSocketAddress inetSocketAddress) throws Exception;
}
