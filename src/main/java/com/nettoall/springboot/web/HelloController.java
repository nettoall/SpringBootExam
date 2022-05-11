package com.nettoall.springboot.web;

import com.nettoall.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// JSON을 반환하는 컨트롤러로 만듬 . @ResponseBody를 method마다 선언했던 것을 controller에 선언함으로 한번에 사용하도록 함
@RestController
public class HelloController {
// Http Get 요청을 받을 수 있는 API 선언
    //@ RequestMapping(Method=RequestMethod.GET 발전된 선언
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }
}
