package com.finder.LnF.mailService;

import com.finder.LnF.contact.ContactRepository;
import com.finder.LnF.utils.ResponseEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final ContactRepository contactRepository;
    private static final String SUBJECT = "LOST AND FOUND!";

    public ResponseEntity<?> sendFoundEmail(String emailAddress) throws MessagingException {

        String userName;
        try {
            userName = getPrincipal();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }

        String messageBody = "<html>" + "<body>"
                + "Dear " + userName + "," + "<br>"
                + "<br>"
                + "<p>" + "Your requested lost document has been found."
                + "<br>" + " Please login with your credentials to view it."
                + "</p>" + "<br>"
                + "<br><br>" +
                "<p>Best Regards,</p>" +
                "<p>The LnF Team</p>"+
                "</body>" + "</html>";

        var contact = contactRepository.findByUsername(userName).orElse(null);
        if(contact != null){

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailAddress);
            helper.setSubject(SUBJECT);
            helper.setText(messageBody, true);
            javaMailSender.send(message);

            return ResponseEntity.builder()
                    .message("Email Sent Successfully")
                    .statusCode(HttpStatus.OK.value())
                    .build();

        } else {
            return ResponseEntity.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Please set Contact Details First")
                    .build();
        }
    }

    private String getPrincipal() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Kindly login first");
        }
        return authentication.getName();
    }
}
