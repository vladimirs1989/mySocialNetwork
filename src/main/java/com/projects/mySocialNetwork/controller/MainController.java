package com.projects.mySocialNetwork.controller;

import com.projects.mySocialNetwork.entity.Message;
import com.projects.mySocialNetwork.entity.User;
import com.projects.mySocialNetwork.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageRepository messageRepository;

    @Value("${upload.path}")
    private  String uploadPath;

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
                      Map<String, Object> model,
                      @RequestParam("file")MultipartFile file) throws IOException {
        Message message = new Message(text, tag, user);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resaltFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resaltFilename));

            message.setFilename(resaltFilename);
        }
        messageRepository.save(message);
        Iterable<Message> messages = messageRepository.findAll();
        model.put("messages", messages);

        return "main";
    }


}
