package com.parasoft.mcpserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class UseClientToTestServer {
    @Test
    public void stateful() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        McpClientTransport mcpClientTransport = HttpClientStreamableHttpTransport.builder("http://localhost:8080")
                .endpoint("stateful/mcp")
                .build();

        McpSyncClient client = McpClient.sync(mcpClientTransport)
                .requestTimeout(Duration.ofSeconds(60))
                .capabilities(McpSchema.ClientCapabilities.builder()
                        .roots(true)
                        .sampling()
                        .build())
                .build();

        client.initialize();
        client.ping();
        McpSchema.ListToolsResult toolsResult = client.listTools();
        System.out.println(objectMapper.writeValueAsString(toolsResult));
        McpSchema.CallToolRequest callToolRequest = McpSchema.CallToolRequest.builder()
                .name("get_my_name")
                .build();
        McpSchema.CallToolResult callToolResult = client.callTool(callToolRequest);
        System.out.println(objectMapper.writeValueAsString(callToolResult));
        while (true){
            // keep the client be alive, you need to stop the test to shut down the client
        }
    }

    @Test
    public void stateless() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        McpClientTransport mcpClientTransport = HttpClientStreamableHttpTransport.builder("http://localhost:8080")
                .endpoint("stateless/mcp")
                .build();

        McpSyncClient client = McpClient.sync(mcpClientTransport)
                .requestTimeout(Duration.ofSeconds(60))
                .capabilities(McpSchema.ClientCapabilities.builder()
                        .roots(true)
                        .sampling()
                        .build())
                .build();

        client.initialize();
        client.ping();
        McpSchema.ListToolsResult toolsResult = client.listTools();
        System.out.println(objectMapper.writeValueAsString(toolsResult));
        McpSchema.CallToolRequest callToolRequest = McpSchema.CallToolRequest.builder()
                .name("get_my_name")
                .build();
        McpSchema.CallToolResult callToolResult = client.callTool(callToolRequest);
        System.out.println(objectMapper.writeValueAsString(callToolResult));
        while (true){
            // keep the client be alive, you need to stop the test to shut down the client
        }
    }

    @Test
    public void sse() throws JsonProcessingException {

        McpClientTransport mcpClientTransport = HttpClientSseClientTransport
                                                    .builder("http://non-existent-server:8080")
                                                    .sseEndpoint("/sse")
                                                    .objectMapper(new ObjectMapper())
                                                    .build();

        McpSyncClient client = McpClient.sync(mcpClientTransport)
                .requestTimeout(Duration.ofSeconds(60))
                .initializationTimeout(Duration.ofSeconds(60))
                .capabilities(McpSchema.ClientCapabilities.builder().roots(true).sampling().build())
                .build();

        client.initialize();
        client.ping();


        McpSchema.ListToolsResult toolsResult = client.listTools();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(objectMapper.writeValueAsString(toolsResult));
        McpSchema.CallToolRequest callToolRequest = McpSchema.CallToolRequest.builder()
                .name("get_my_name")
                .build();
        McpSchema.CallToolResult callToolResult = client.callTool(callToolRequest);
        System.out.println(objectMapper.writeValueAsString(callToolResult));
        while (true){
            // keep the client be alive, you need to stop the test to shut down the client
        }
    }
}
