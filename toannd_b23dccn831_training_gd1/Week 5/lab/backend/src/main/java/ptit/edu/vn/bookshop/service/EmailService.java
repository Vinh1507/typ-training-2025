package ptit.edu.vn.bookshop.service;

import java.util.Map;

public interface EmailService {
    void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml);
    void sendEmailFromTemplateSync(String to, String subject, String templateName, Map<String, Object> variables);
}
