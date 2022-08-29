package com.projects.mySocialNetwork.controller;

import com.projects.mySocialNetwork.entity.Message;
import com.projects.mySocialNetwork.entity.User;
import com.projects.mySocialNetwork.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
    public String main(Map<String, Object> model) {
        Iterable<Message> messages = messageRepository.findAll();
        model.put("messages", messages);
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

    @PostMapping("/filter")
    public String filter(@RequestParam String filter, @RequestParam String chose, Map<String, Object> model) {
        Iterable<Message> messagesByTag;
        System.out.println(chose);
        if (chose.equals("tag")) {
            if (filter != null && !filter.isEmpty()) {
                messagesByTag = messageRepository.findByTag(filter);
            } else {
                messagesByTag = messageRepository.findAll();
            }
        } else {
            if (filter != null && !filter.isEmpty()) {
                messagesByTag = messageRepository.findByText(filter);
            } else {
                messagesByTag = messageRepository.findAll();
            }
        }
        model.put("messages", messagesByTag);
        return "main";
    }

}
