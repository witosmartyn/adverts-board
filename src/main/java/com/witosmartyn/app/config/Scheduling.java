package com.witosmartyn.app.config;

import com.witosmartyn.app.config.components.StatisticsBean;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * vitali
 */
@org.springframework.context.annotation.Configuration
@EnableScheduling
@Log4j()
public class Scheduling extends WebMvcConfigurerAdapter {


    @Autowired
    private StatisticsBean statisticsBean;

    @Scheduled(fixedDelayString = "${app.config.statistic.update-interval}")
    private void statisticUpdate() {
        statisticsBean.updateAdvertsOnSite();
        statisticsBean.updateRegisteredUsers();
        statisticsBean.updateCountItemByCategory();

    }




}
