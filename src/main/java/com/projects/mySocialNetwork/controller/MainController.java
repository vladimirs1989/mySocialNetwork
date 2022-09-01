package com.projects.mySocialNetwork.controller;

import com.projects.mySocialNetwork.entity.Message;
import com.projects.mySocialNetwork.entity.User;
import com.projects.mySocialNetwork.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/")
    public String start(Map<String, Object> model) {
        return "network";
    }

    @GetMapping("/main")
    public String main(@RequestParam (required = false, defaultValue = "") String filter,
                       @RequestParam   (required = false, defaultValue = "text") String chose,
                       Model model) {
        Iterable<Message> messages = messageRepository.findAll();

       if (chose.equals("tag")) {
            if (filter != null && !filter.isEmpty()) {
                messages = messageRepository.findByTag(filter);
            } else {
                messages = messageRepository.findAll();
            }
        } else {
            if (filter != null && !filter.isEmpty()) {
                messages= messageRepository.findByText(filter);
            } else {
                messages = messageRepository.findAll();
            }
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam String tag,
                      Map<String, Object> model) {
        Message message = new Message(text, tag, user);
        messageRepository.save(message);
        Iterable<Message> messages = messageRepository.findAll();
        model.put("messages", messages);

        return "main";
    }


}
