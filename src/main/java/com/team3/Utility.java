package com.team3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.ServerConfig;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    private static final Logger logger = LogManager.getLogger("Utility");
    private static final String LOAD_BALANCER_CONFIG = "load_balancer";
    private static final String APP_SERVER_CONFIG = "app_server";
    private static final String FILE_SERVER_CONFIG = "file_server";
    private static final String ALL_SERVER_CONFIG = "node";
    private static final String LEADER_CONFIG = "default_leader";
    private static final String ADDRESS = "address";
    private static final String PORT = "port";

    /* check if port is valid integer and between 0 - 2^16 */
    public static Pair<Boolean, Integer> isPortValid(String portString) {
        int port;
        try {
            port = Integer.parseInt(portString);
            if (port > 65535 || port < 0) {
                return new Pair<Boolean, Integer>(false, 0);
            }
        } catch (Exception e) {
            return new Pair<Boolean, Integer>(false, 0);
        }
        return new Pair<Boolean, Integer>(true, port);
    }

    private static List<Pair<String, Integer>> createNodeList(JsonNode node) {
        if (node.isArray()) {
            List<Pair<String, Integer>> addrList = new ArrayList<Pair<String, Integer>>();
            for (JsonNode elementNode: node) {
                addrList.add(
                        new Pair<String, Integer>(
                                elementNode.get(ADDRESS).asText(),
                                elementNode.get(PORT).asInt()));
            }
            return addrList;
        }
        return null;
    }

    public static ServerConfig readConfig(String configPath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String configStr = new String(Files.readAllBytes(Paths.get(configPath)));
            JsonNode configNode = mapper.readTree(configStr);
            JsonNode loadBalancerNode = configNode.get(LOAD_BALANCER_CONFIG);
            JsonNode appServerNode = configNode.get(APP_SERVER_CONFIG);
            JsonNode fileServerNode = configNode.get(FILE_SERVER_CONFIG);
            JsonNode allServerNode = fileServerNode.get(ALL_SERVER_CONFIG);
            JsonNode defaultLeaderNode = fileServerNode.get(LEADER_CONFIG);

            List<Pair<String, Integer>> loadBalancerList = createNodeList(loadBalancerNode);
            List<Pair<String, Integer>> appServerList = createNodeList(appServerNode);
            List<Pair<String, Integer>> fileServerList = createNodeList(allServerNode);
            Pair<String, Integer> defaultLeader = new Pair<String, Integer>(
                    defaultLeaderNode.get(ADDRESS).asText(),
                    defaultLeaderNode.get(PORT).asInt());

            return new ServerConfig(loadBalancerList, appServerList, fileServerList, defaultLeader);

        } catch (Exception e) {
            logger.error("Cannot parse config file" +
                    configPath + ": " + e.getMessage());
        }
        return null;
    }
}