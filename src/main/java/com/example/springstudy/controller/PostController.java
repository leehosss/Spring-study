package com.example.springstudy.controller;

import com.example.springstudy.dto.PostForm;
import com.example.springstudy.entity.Post;
import com.example.springstudy.entity.User;
import com.example.springstudy.service.PostService;
import com.example.springstudy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    // 게시글 목록 (페이징: 한 페이지 10개)
    @GetMapping
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Post> posts = postService.findAll(PageRequest.of(page, 10));
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        return "post/list";
    }

    // 게시글 상세
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.findByIdAndIncrementView(id);
        model.addAttribute("post", post);
        return "post/detail";
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("isEdit", false);
        return "post/form";
    }

    // 게시글 작성 처리
    @PostMapping("/new")
    public String create(@Valid @ModelAttribute PostForm postForm,
                         BindingResult bindingResult,
                         Model model,
                         Authentication auth) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "post/form";
        }

        User author = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("로그인 정보를 찾을 수 없습니다."));

        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setAuthor(author);

        Post saved = postService.save(post);
        return "redirect:/posts/" + saved.getId();
    }

    // 게시글 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Authentication auth) {
        Post post = postService.findById(id);

        // 작성자 본인만 수정 가능
        if (!post.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/posts/" + id;
        }

        PostForm postForm = new PostForm();
        postForm.setTitle(post.getTitle());
        postForm.setContent(post.getContent());

        model.addAttribute("postForm", postForm);
        model.addAttribute("postId", id);
        model.addAttribute("isEdit", true);
        return "post/form";
    }

    // 게시글 수정 처리
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute PostForm postForm,
                       BindingResult bindingResult,
                       Model model,
                       Authentication auth) {

        Post post = postService.findById(id);

        if (!post.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/posts/" + id;
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", id);
            model.addAttribute("isEdit", true);
            return "post/form";
        }

        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        postService.save(post);

        return "redirect:/posts/" + id;
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Authentication auth) {
        Post post = postService.findById(id);

        if (!post.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/posts/" + id;
        }

        postService.delete(id);
        return "redirect:/posts";
    }
}
