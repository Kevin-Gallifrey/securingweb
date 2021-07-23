package com.example.securingweb.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import com.example.securingweb.entity.LogInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class LogInfoDao extends JdbcDaoSupport{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        super.setJdbcTemplate(jdbcTemplate);
    }

    public void insertLogInfo(LogInfo info) {
        // 传入SQL，SQL参数，返回更新的行数:
        if (1 != jdbcTemplate.update("INSERT INTO logInfo(time, ipaddr, user, event, status) VALUES(?,?,?,?,?)", 
            info.getTime(), info.getIpaddr(), info.getUser(), info.getEvent(), info.getStatus())) {
            throw new RuntimeException("Insert failed.");
        }
    }

    public List<LogInfo> getInfoByUser(String user) {
        return jdbcTemplate.query("SELECT * FROM logInfo WHERE user = ?",
            new BeanPropertyRowMapper<>(LogInfo.class), new Object[] { user });
        //return jdbcTemplate.queryForObject("SELECT * FROM logInfo WHERE user = ?",
        //       LogInfo.class, new Object[] { user });
    }

}
