package com.myapp.todo.controller;

import com.myapp.todo.dto.UserDTO;
import com.myapp.todo.entity.user.UserEntity;
import com.myapp.todo.oauth.LoginUser;
import com.myapp.todo.repository.UserRepository;
import com.myapp.todo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/todo/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            Map<String, Object> kakao = (Map<String, Object>) principal.getAttributes().get("kakao_account");
            model.addAttribute("name", (String) ((Map<String, Object>) kakao.get("profile")).get("nickname"));
        }
        return "main";
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserDTO> getUserInfo(@LoginUser UserDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/logout")
    public String logout() {

        return "redirect:/";
    }

    @PatchMapping("/name/{userId}")
    public ResponseEntity<UserDTO> updateName(@PathVariable("userId") Long userId, @RequestBody String newName) {
        UserEntity updatedUser = userService.updateName(userId, newName);
        return ResponseEntity.ok(new UserDTO(updatedUser));
    }

}
