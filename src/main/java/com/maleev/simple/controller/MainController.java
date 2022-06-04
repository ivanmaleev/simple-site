package com.maleev.simple.controller;

import com.maleev.simple.model.entity.Message;
import com.maleev.simple.model.entity.User;
import com.maleev.simple.repository.MessageRepository;
import com.maleev.simple.utils.ControllerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.Set;
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
    public String main(@RequestParam(required = false, defaultValue = "") String tagFilter,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Message> page;
        if ("".equals(tagFilter)) {
            page = messageRepository.findAll(pageable);
        } else {
            page = messageRepository.findAllByTag(tagFilter, pageable);
        }
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
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
            saveFile(message, file);
            model.addAttribute("message", null);
            messageRepository.save(message);
        }
        model.addAttribute("messages", messageRepository.findAll());
        model.addAttribute("tagFilter", "");
        return "redirect:/main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message) {
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", user.getMessages());
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (message.getUser().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            saveFile(message, file);
            messageRepository.save(message);
        }
        return "redirect:/user-messages/" + user;
    }

    private void saveFile(@Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            Path dirPath = Paths.get(uploadPath);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            final String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            file.transferTo(Paths.get(uploadPath + "/" + fileName));
            message.setFilename(fileName);
        }
    }
}