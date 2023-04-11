package com.notch.heist.service.jobs;

import com.notch.heist.domain.enums.HeistStatus;
import com.notch.heist.service.HeistService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ChangeHeistStatusJob extends QuartzJobBean {

    private final HeistService heistService;

    public ChangeHeistStatusJob(HeistService heistService) {
        this.heistService = heistService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        heistService.changeHeistStatus((
                        Long) context.getJobDetail().getJobDataMap().get("heist_id"),
                (String) context.getJobDetail().getJobDataMap().get("heist_status")
        );
    }
}
