package ptit.edu.vn.bookshop.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ptit.edu.vn.bookshop.service.EmailService;

import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
                    isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            System.out.println("ERROR SEND EMAIL: " + e.getMessage());
        }
    }

    @Async
    public void sendEmailFromTemplateSync(String to, String subject, String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        String content = this.templateEngine.process(templateName, context); // convert tu html sang String
        this.sendEmailSync(to, subject, content, false, true);
    }
}
