package io.modelcontextprotocol.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.server.transport.HttpServletStatelessServerTransport;
import io.modelcontextprotocol.server.transport.HttpServletStreamableServerTransportProvider;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Duration;

@Configuration
@EnableWebMvc
public class McpServerConfig {
    @Bean(name = "httpServletStreamableServerTransportProvider")
    public HttpServletStreamableServerTransportProvider httpServletStreamableServerTransportProvider() {
        return HttpServletStreamableServerTransportProvider
                .builder()
                .mcpEndpoint("/stateful/mcp")
                .keepAliveInterval(Duration.ofSeconds(10L))
                .objectMapper(new ObjectMapper())
                .build();
    }

    @Bean(name = "httpServletStatelessServerTransport")
    public HttpServletStatelessServerTransport httpServletStatelessServerTransport() {
        return HttpServletStatelessServerTransport
                .builder()
                .messageEndpoint("/stateless/mcp")
                .objectMapper(new ObjectMapper())
                .build();
    }

    @Bean(name = "httpServletSseServerTransportProvider")
    public HttpServletSseServerTransportProvider httpServletSseServerTransportProvider() {
        return HttpServletSseServerTransportProvider.builder()
                .sseEndpoint("/sse")
                .messageEndpoint("/mcp/message")
                .keepAliveInterval(Duration.ofSeconds(10L))
                .objectMapper(new ObjectMapper())
                .build();
    }

    @Bean
    public ServletRegistrationBean statefulServletBean(HttpServletStreamableServerTransportProvider servlet) {
        return new ServletRegistrationBean(servlet, "/stateful/mcp");
    }

    @Bean
    public ServletRegistrationBean statelessServletBean(HttpServletStatelessServerTransport servlet) {
        return new ServletRegistrationBean(servlet, "/stateless/mcp");
    }

    @Bean
    public ServletRegistrationBean SSEServletBean(HttpServletSseServerTransportProvider servlet) {
        return new ServletRegistrationBean(servlet, "/sse", "/mcp/message");
    }
}
