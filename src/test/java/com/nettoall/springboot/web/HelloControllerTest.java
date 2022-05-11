package com.nettoall.springboot.web;

import com.nettoall.springboot.config.auth.SecurityConfig;
import org.junit.Test;
// junit5
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@RunWith 테스트 진행시 JUnit에 내장된 실행자 외에 다른 실행자를 실행
//SpringRunner 실행자를 사용 - 스프링 부트 테스트와 JUnit사이에 연결자 역할
//@WebMvcTest 여러 스프링 테스트 중 Web에 해당됨
// 선언시 @Controller @ControllerAdvice 사용
// @Service, @Component @ Repository 사용 불가

//junit4
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
public class HelloControllerTest {
    // 스프링이 관리하는 Bean 주입
    @Autowired
    private MockMvc mvc; // 웹 API 테스트시 사용. 스프링 MVC테스트 시작점.

    @WithMockUser(roles="USER")
    @Test
    public void hello_return() throws Exception {
        String hello = "hello";

        //MockMVC를 통해 /hello 주소로 http get 요청
        // 체이닝 지원으로 다음 and 검증 기능 이어서 선언 가능
        // andExpect(status().isOk()) mvc.perform 결과를 검증 http header status 검증
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello)); // 응답본문의 내용을 검증
    }

    @WithMockUser(roles="USER")
    @Test
    public void helloDto_return() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(get("/hello/dto")
                .param("name",name)
                .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }
}
