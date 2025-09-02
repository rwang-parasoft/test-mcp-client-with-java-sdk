package io.modelcontextprotocol.test;

import io.modelcontextprotocol.server.*;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.server.transport.HttpServletStatelessServerTransport;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MCPServer {
    @Bean
    public McpSyncServer statefulSever(HttpServletStreamableServerTransportProvider transportProvider) {
        McpSyncServer syncServer = McpServer.sync(transportProvider)
                .serverInfo("Streamable MCP Server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(false, true)
                        .tools(true)
                        .prompts(false)
                        .build())
                .build();
        McpServerFeatures.SyncToolSpecification toolSpecification = McpServerFeatures.SyncToolSpecification.builder()
                .tool(getMyNameTool())
                .callHandler((exchange, request) -> myNameTool_CallToolResult())
                .build();
        syncServer.addTool(toolSpecification);

        return syncServer;
    }

    @Bean
    public McpStatelessSyncServer statelessSever(HttpServletStatelessServerTransport transportProvider) {
        McpStatelessSyncServer syncServer = McpServer.sync(transportProvider)
                .serverInfo("Stateless MCP Server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(false, true)
                        .tools(true)
                        .prompts(false)
                        .build())
                .build();
        McpStatelessServerFeatures.SyncToolSpecification toolSpecification = McpStatelessServerFeatures.SyncToolSpecification.builder()
                .tool(getMyNameTool())
                .callHandler((exchange, request) -> {
                    return myNameTool_CallToolResult();
                })
                .build();
        syncServer.addTool(toolSpecification);

        return syncServer;
    }

    @Bean
    public McpSyncServer SSESever(HttpServletSseServerTransportProvider transportProvider) {
        McpSyncServer syncServer = McpServer.sync(transportProvider)
                .serverInfo("Stateless MCP Server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(false, true)
                        .tools(true)
                        .prompts(false)
                        .build())
                .build();
        McpServerFeatures.SyncToolSpecification toolSpecification = McpServerFeatures.SyncToolSpecification.builder()
                .tool(getMyNameTool())
                .callHandler((exchange, request) -> myNameTool_CallToolResult())
                .build();
        syncServer.addTool(toolSpecification);

        return syncServer;
    }

    private McpSchema.Tool getMyNameTool() {
        return McpSchema.Tool.builder()
                .name("get_my_name")
                .description("Get my name.")
                .title("test")
                .build();
    }

    private McpSchema.CallToolResult myNameTool_CallToolResult() {
        return new McpSchema.CallToolResult("Hello, my name is <placeholder>.", false);
    }

}
