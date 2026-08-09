package com.wordbridge.project.gigorder;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GigOrderScheduler {

    private final GigOrderService gigOrderService;


    //Every hour
    @Scheduled(cron = "0 0 * * * ?")
    public void processExpiredDisputes() {
        gigOrderService.processExpiredDisputes();



    }


}
