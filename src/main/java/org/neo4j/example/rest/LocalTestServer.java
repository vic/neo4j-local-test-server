/*
 * Shamefully copied from https://raw.githubusercontent.com/neo4j-contrib/cypher-http-examples/master/src/test/java/org/neo4j/example/rest/LocalTestServer.java
 */
package org.neo4j.example.rest;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.WrappingNeoServer;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.configuration.Configurator;
import org.neo4j.server.configuration.ServerConfigurator;
import org.neo4j.server.preflight.PreFlightTasks;
import org.neo4j.test.ImpermanentGraphDatabase;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.Collections;
import java.util.Map;

public class LocalTestServer {
    public static final int PORT = 7777;
    private final GraphDatabaseService graphDatabase;
    private WrappingNeoServerBootstrapper neoServer;

    public LocalTestServer() {
        graphDatabase = new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

    public void start() {
        final GraphDatabaseAPI api = (GraphDatabaseAPI)graphDatabase;

        final ServerConfigurator c = new ServerConfigurator(api);
        c.configuration().addProperty(
                Configurator.WEBSERVER_ADDRESS_PROPERTY_KEY, "127.0.0.1");
        c.configuration().addProperty(
                Configurator.WEBSERVER_PORT_PROPERTY_KEY, PORT);
        c.configuration().addProperty(
                "dbms.security.auth_enabled", false);


        neoServer = new WrappingNeoServerBootstrapper(api, c);
        neoServer.start();
    }

    public void stop() {
        try {
            neoServer.stop();
        } catch(Exception e) {
            System.err.println("Error stopping server: "+e.getMessage());
        }
        neoServer=null;
    }


    public GraphDatabaseService getGraphDatabase() {
        return graphDatabase;
    }

    public String getBaseUrl() {
        return "http://localhost:"+PORT;
    }

    public static void main(String[] args) {
        final LocalTestServer server = new LocalTestServer();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override public void run() {
                server.stop();
            }
        });
        server.start();
        System.out.println("Started at "+server.getBaseUrl());
    }

}