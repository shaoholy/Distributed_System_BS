package com.team3;

import com.team3.Utility;
import com.team3.grpc.ClientRequest;
import com.team3.grpc.LoadServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Client {
    private final ManagedChannel channel;
    private final LoadServiceGrpc.LoadServiceBlockingStub blockingStub;
    private final int STUB_TIMEOUT = 60;
    private String connectedPort;
    private static final Logger logger = LogManager.getLogger("Client");

    public Client(String address, int port) {
        this(ManagedChannelBuilder.forAddress(address, port)
                .usePlaintext().build());
        connectedPort = Integer.toString(port);
    }

    Client(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = LoadServiceGrpc.newBlockingStub(channel);
    }

    public void sendOperation() {
        try {
            ClientRequest request = ClientRequest
                    .newBuilder()
                    .setAddress(InetAddress.getLocalHost().getHostAddress().toString())
                    .setRequestId(UUID.randomUUID().toString())
                    .build();
            blockingStub.withDeadlineAfter(STUB_TIMEOUT, TimeUnit.SECONDS)
            .balance(request);
        } catch (Exception e) {
            logger.error("Cannot clear all : " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        int port;
        String address;
        Pair<Boolean, Integer> portPair;

        /* set log configuration file path */
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        String propLocation = "src/main/resources/log4j2.xml";
        File file = new File(propLocation);

        context.setConfigLocation(file.toURI());

        logger.info("Log properties file location: " + propLocation);

        /* ask user for address and port */
        if (args.length == 2 && (portPair = Utility.isPortValid(args[1])).getKey()) {
            address = args[0];
            port = portPair.getValue();
        } else {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                logger.info("Please give two arguments, " +
                        "address and port, separate with space");
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] line_arg = line.trim().split("\\s+");
                    if (line_arg.length == 2 && (portPair = Utility.isPortValid(line_arg[1])).getKey()) {
                        address = line_arg[0];
                        port = portPair.getValue();
                        break;
                    }
                }
            }
        }

        Client client = new Client(address, port);
        client.sendOperation();

    }
}