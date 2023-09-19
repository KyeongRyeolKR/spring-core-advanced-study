package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 빈 후처리기(bean post processor)
 * 빈을 생성하고 난 후 등록하기 직전에 객체를 조작하고 싶다면 빈 후처리기를 사용하면 된다.
 * - 객체를 조작할 수도 있고, 완전히 다른 객체로 변경할 수도 있다.
 * - 컴포넌트 스캔의 대상이 되는 빈들마저도 빈 후처리기를 사용하면 프록시로 교체가 가능하다!
 */
public class BeanPostProcessorTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // "beanA" 이름으로 B 객체가 빈으로 등록된다.
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();

        // A는 빈으로 등록되지 않는다.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean
        public AToBPostProcessor helloPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    /**
     * 빈 후처리기 클래스
     */
    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        /**
         * bean 객체를 생성하고 초기화까지 진행한 후 조작
         */
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName = {}, bean = {}", beanName, bean);
            // 만약 A 객체가 들어오면 B 객체로 변경해서 반환한다.
            if(bean instanceof A) {
                return new B();
            }
            // 아니라면 그대로 반환한다.
            return bean;
        }
    }
}
