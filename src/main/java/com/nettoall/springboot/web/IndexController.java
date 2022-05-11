package com.nettoall.springboot.web;

import com.nettoall.springboot.config.auth.LoginUser;
import com.nettoall.springboot.config.auth.dto.SessionUser;
import com.nettoall.springboot.service.PostsService;
import com.nettoall.springboot.web.dto.PostsResponseDto;
import com.nettoall.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    /**
     * SessionUser 를 직접 호출해서 가져오는 방식
     * @return
     */
//    @GetMapping("/")
//    public String index(Model model) {
//        model.addAttribute("posts",postsService.findAllDesc());
//
//        SessionUser user = (SessionUser)  httpSession.getAttribute("user");
//
//        if(user != null) {
//            model.addAttribute("userName", user.getName());
//        }
//        return "index";
//    }

    /**
     * LoginUser 어노테이션을 생성하여 로그인 정보를 처리
     * @return
     */
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());

        if(user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }


    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post",dto);
        return "posts-update";
    }
}
