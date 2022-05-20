package com.example.usercrud.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class UIController {
    @GetMapping("/createuser")
    public String addUser() {
        return "create-user";
    }

    @GetMapping("/updateuser")
    public String updateUser() {
        return "update-user";
    }
}
