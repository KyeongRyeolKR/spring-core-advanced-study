package hello.proxy.config.v1_proxy;

import hello.advanced.trace.logtrace.LogTrace;
import hello.proxy.app.v1.*;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(logTrace));

        return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceImpl serviceImpl = new OrderServiceImpl(orderRepository(logTrace));

        return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryImpl repositoryImpl = new OrderRepositoryImpl();

        return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
    }

//    @Bean
//    public OrderServiceV2 orderServiceV2() {
//        return new OrderServiceV2(orderRepositoryV2());
//    }
//
//    @Bean
//    public OrderRepositoryV2 orderRepositoryV2() {
//        return new OrderRepositoryV2();
//    }
}
