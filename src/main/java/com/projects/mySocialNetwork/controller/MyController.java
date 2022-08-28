package com.projects.mySocialNetwork.controller;

import com.projects.mySocialNetwork.entity.Message;
import com.projects.mySocialNetwork.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MyController {

    @Autowired
    private MessageRepository messageRepository;
    
    @GetMapping("/network")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name,
                           Map<String,Object> model) {
        model.put("name", name);
        return "network";
    }

    @GetMapping
    public String main(Map<String,Object> model) {
        Iterable<Message> messages = messageRepository.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("/")
    public  String add(@RequestParam String text, @RequestParam String tag,
                       Map<String,Object> model){
        Message message = new Message(text, tag);
        messageRepository.save(message);
        Iterable<Message> messages = messageRepository.findAll();
        model.put("messages", messages);

        return "main";
    }

    @PostMapping("/filter")
    public  String filter(@RequestParam String filter, @RequestParam String chose, Map<String,Object> model){
        Iterable<Message> messagesByTag;
        System.out.println(chose);
        if(chose.equals("tag")) {
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
