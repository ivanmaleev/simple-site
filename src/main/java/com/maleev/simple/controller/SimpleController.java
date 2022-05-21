package com.maleev.simple.controller;

import com.maleev.simple.entity.Message;
import com.maleev.simple.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class SimpleController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/simple")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Map<String, Object> model) {
        model.put("name", name);
        return "simple";
    }

    @GetMapping
    public String main(Map<String, Object> model) {
        model.put("messages", messageRepository.findAll());
        return "main";
    }

    @PostMapping
    public String save(@RequestParam(name = "text", required = true, defaultValue = "") String text,
                       @RequestParam(name = "tag", required = true, defaultValue = "") String tag,
                       Map<String, Object> model) {
        Message message = new Message(text, tag);
        messageRepository.save(message);
        model.put("messages", messageRepository.findAll());
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam(name = "tag", required = true, defaultValue = "") String tag,
                         Map<String, Object> model) {
        List<Message> messages;
        if ("".equals(tag)) {
            messages = messageRepository.findAll();
        } else {
            messages = messageRepository.findAllByTag(tag);
        }
        model.put("messages", messages);
        return "main";
    }
}