package com.chenjay.blog.service.impl;

import com.chenjay.blog.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author janti
 * @date 2018/5/3 22:07
 */
@Component
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender mailSender;


    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailFrom;

    /**
     * 发送简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendSimpleEmail(String to,String subject,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * 发送html邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendHtmlMail(String to, String subject, String template, Context context) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            String content = templateEngine.process(template, context);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);



        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送带附件的邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param filepath
     */
    @Override
    public void sendFileMail(String to, String subject, String content, String filepath) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);

            FileSystemResource file = new FileSystemResource(new File(filepath));
            String fileName = filepath.substring(filepath.lastIndexOf(File.separator));
            helper.addAttachment(fileName,file);

            mailSender.send(mimeMessage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
