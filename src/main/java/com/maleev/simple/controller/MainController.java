package com.maleev.simple.controller;

import com.maleev.simple.model.entity.Message;
import com.maleev.simple.model.entity.User;
import com.maleev.simple.repository.MessageRepository;
import com.maleev.simple.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

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
                       @Valid Message message,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam("file") MultipartFile file) throws IOException {
        message.setUser(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                Path dirPath = Paths.get(uploadPath);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }
                final String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                file.transferTo(Paths.get(uploadPath + "/" + fileName));
                message.setFilename(fileName);
            }
            model.addAttribute("message", null);
            messageRepository.save(message);
        }
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("tagFilter", "");
        return "redirect:/main";
    }
}