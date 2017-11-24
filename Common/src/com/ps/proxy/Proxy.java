package com.ps.proxy;

public class Proxy {
    private String proxyAdress;
    private int port;

    public Proxy(String proxyAdress, int port) {
        this.proxyAdress = proxyAdress;
        this.port = port;
    }

    public String getProxyAdress() {
        return proxyAdress;
    }

    public int getPort() {
        return port;
    }
}
