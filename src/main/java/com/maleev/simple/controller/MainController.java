package com.maleev.simple.controller;

import com.maleev.simple.model.entity.Message;
import com.maleev.simple.model.entity.User;
import com.maleev.simple.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private final MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "simple";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String tagFilter, Model model) {
        List<Message> messages;
        if ("".equals(tagFilter)) {
            messages = messageRepository.findAll();
        } else {
            messages = messageRepository.findAllByTag(tagFilter);
        }
        model.addAttribute("messages", messages);
        model.addAttribute("tagFilter", tagFilter);
        return "main";
    }

    @PostMapping("/main")
    public String save(@AuthenticationPrincipal User user,
                       @RequestParam String text,
                       @RequestParam String tag, Model model) {
        Message message = new Message(user, text, tag);
        messageRepository.save(message);
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("tagFilter", "");
        return "main";
    }
}