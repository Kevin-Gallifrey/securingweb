package com.example.securingweb.services;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.example.securingweb.dao.LogInfoDao;
import com.example.securingweb.entity.LogInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class LogInfoService {

    @Autowired
    private LogInfoDao logInfoDao;

    public List<LogInfo> getInfoByUser(String user) {
        return logInfoDao.getInfoByUser(user);
    }

    public void insertLogInfo(LogInfo info) {
        logInfoDao.insertLogInfo(info);
    }

    public LogInfo createLogIn(HttpServletRequest request, String user, String status) {
        
        // 获得用户的IP地址
        String ipaddr = request.getRemoteAddr();

        // 获得请求行信息
        String event = request.getMethod();
        if (request.getRequestURI() != null) {
            event += ' ' + request.getRequestURI();
        }
        if (request.getQueryString() != null) {
            event += ' ' + request.getQueryString();
        }
        event += ' ' + request.getProtocol();

        // 获得请求参数
        Map<String, String[]> paraMap = request.getParameterMap();
        String paraString = "";
        for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
            paraString += entry.getKey() + ":" + Arrays.toString(entry.getValue()) + "; ";
        }
        paraString = '[' + paraString.substring(0,paraString.length()-2) + ']';
        event += ' ' + paraString;

        // 获得系统当前时间
        ZonedDateTime zdt = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ZZZZ");
        String time = formatter.format(zdt);

        return new LogInfo(time, ipaddr, user, event, status);
    }
    
}
