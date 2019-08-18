package com.team3;

import javafx.util.Pair;

import java.util.List;

public class ServerConfig {
    public ServerConfig(List<Pair<String, Integer>> loadBalancerList, List<Pair<String, Integer>> appServerList, List<Pair<String, Integer>> fileServerList, Pair<String, Integer> defaultLeader) {
        this.loadBalancerList = loadBalancerList;
        this.appServerList = appServerList;
        this.fileServerList = fileServerList;
        this.defaultLeader = defaultLeader;
    }

    public List<Pair<String, Integer>> getLoadBalancerList() {
        return loadBalancerList;
    }

    public void setLoadBalancerList(List<Pair<String, Integer>> loadBalancerList) {
        this.loadBalancerList = loadBalancerList;
    }

    public List<Pair<String, Integer>> getAppServerList() {
        return appServerList;
    }

    public void setAppServerList(List<Pair<String, Integer>> appServerList) {
        this.appServerList = appServerList;
    }

    public List<Pair<String, Integer>> getFileServerList() {
        return fileServerList;
    }

    public void setFileServerList(List<Pair<String, Integer>> fileServerList) {
        this.fileServerList = fileServerList;
    }

    public Pair<String, Integer> getDefaultLeader() {
        return defaultLeader;
    }

    public void setDefaultLeader(Pair<String, Integer> defaultLeader) {
        this.defaultLeader = defaultLeader;
    }

    public List<Pair<String, Integer>> loadBalancerList;
    public List<Pair<String, Integer>> appServerList;
    public List<Pair<String, Integer>> fileServerList;
    public Pair<String, Integer> defaultLeader;
}