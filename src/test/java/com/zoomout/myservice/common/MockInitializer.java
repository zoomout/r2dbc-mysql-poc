package com.zoomout.myservice.common;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.test.context.support.TestPropertySourceUtils;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class MockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String MOCK_SERVER_PORT_PROPERTY = "mock.server.port";

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        var clientAndServer = startClientAndServer();
        configurableApplicationContext.getBeanFactory().registerSingleton("clientAndServer", clientAndServer);
        configurableApplicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                clientAndServer.stop();
            }
        });
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext, MOCK_SERVER_PORT_PROPERTY + "=" + clientAndServer.getLocalPort());
    }
}
