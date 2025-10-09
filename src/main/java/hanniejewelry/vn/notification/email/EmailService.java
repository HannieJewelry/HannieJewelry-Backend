package hanniejewelry.vn.notification.email;

import hanniejewelry.vn.user.enums.UserType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    private static final Map<UserType, String> INVITE_SUBJECT = Map.of(
            UserType.EMPLOYEE, "Employee invitation to join the system",
            UserType.USER, "Customer invitation to join the system"
    );

    @Async
    public void sendInvite(String email, String name, String inviteUrl, UserType type) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(email);

            String subject = INVITE_SUBJECT.getOrDefault(type, "Invitation to join the system");
            helper.setSubject(subject);
            helper.setText(
                    String.format(
                            "Hello %s,\nPlease click the following link to confirm your %s account: %s",
                            name, type.getValue(), inviteUrl
                    ),
                    false
            );
            helper.setFrom("nguyenhauweb@gmail.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}