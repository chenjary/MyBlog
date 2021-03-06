package com.chenjay.blog.scheduletask;

import com.chenjay.blog.modal.Vo.LogVo;
import com.chenjay.blog.service.ILogService;
import com.chenjay.blog.service.IMailService;
import com.chenjay.blog.utils.DateKit;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PrimitiveIterator;

@Component
public class ScheduleTask {

    @Resource
    ILogService logService;

    @Resource
    IMailService mailService;

    private static final String MAIL_TO = "chenjie_task@163.com";

    @Scheduled(fixedRate = 86400000)
    public void process(){
        StringBuffer result = new StringBuffer();
        long totalMemory = Runtime.getRuntime().totalMemory();

        String ram = totalMemory / (1024 * 1024) + "MB";
        List<LogVo> logVoList = logService.getLogs(0,10);
        for (LogVo logVo:logVoList){
            result.append(" 时间: ").append(DateKit.formatDateByUnixTime(logVo.getCreated(),"yyyy-MM-dd HH:mm:ss"));
            result.append(" 操作: ").append(logVo.getAction());
            result.append(" IP： ").append(logVo.getIp()).append("\n");
        }

        /*List<LogVo> logs = new ArrayList<>();
        for (LogVo log : logVoList) {

        }*/
        String template = "mail/InternalServerErrorTemplate";
        Context context = new Context();


        context.setVariable("ram", ram);
        context.setVariable("percent", getMemory());
        context.setVariable("logs", logVoList);
        context.setVariable("occurredTime", DateKit.dateFormat(new Date()));

        mailService.sendHtmlMail(MAIL_TO,"博客系统运行情况",template,context);



    }

    public static String getMemory() {

        OperatingSystemMXBean osmxb = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long totalvirtualMemory = osmxb.getTotalSwapSpaceSize(); // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();
        Double compare = (Double) (1 - freePhysicalMemorySize * 1.0 / totalvirtualMemory) * 100;
        String str = compare.intValue() + "%";
        return str;

    }
}
