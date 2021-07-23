package com.example.securingweb.controllers;

import java.util.List;

import com.example.securingweb.entity.LogInfo;
import com.example.securingweb.services.LogInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private LogInfoService logInfoService;

    @PostMapping("/jdbctest")
    public String jdbctest() {
        // LogInfo newInfo = new LogInfo("2021/7/21 11:30:00","127.0.0.1","test1","insert from controller","insert successfully");
        // logInfoService.insertLogInfo(newInfo);

        List<LogInfo> infoList = logInfoService.getInfoByUser("test1");
        String infoString = ""; 

        for (LogInfo info : infoList) {
            infoString += info.toString();
            infoString += '\n';
        }

        return infoString;
    }

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("text", "Text from controller");
        return "home";
    }

}
