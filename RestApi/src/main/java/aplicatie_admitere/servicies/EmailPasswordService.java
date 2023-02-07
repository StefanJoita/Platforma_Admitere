package aplicatie_admitere.servicies;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.logging.Logger;

import static org.springframework.beans.MethodInvocationException.ERROR_CODE;

@Service
public class EmailPasswordService {
    private JavaMailSender mailSender;
    @Autowired
    public EmailPasswordService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }
    public String generatePassword() {

        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10,lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }
    public String generateOTP()
    {
        Random r = new Random( System.currentTimeMillis() );
        String otp=Integer.toString((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
        return otp;
    }
    public void sendPassword(String to,String Password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("aplicatie.admitere@gmail.com");
        message.setTo(to);
        message.setSubject("Contul tau a fost creat cu succes!Ti-am trimis aici parola!");
        message.setText("Parola dvs este { "+Password+"}");
        mailSender.send(message);
    }
    public void sendOtp(String to, String otp)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("aplicatie.admitere@gmail.com");
        message.setTo(to);
        message.setSubject("Ai incercat sa te conectezi pe contul de pe platforma de admitere");
        message.setText("Codul unic de logare este: "+otp+"}. Daca nu ai incercat tu sa te loghezi inseamna ca altcineva a facut-o. Ai grija!");
        mailSender.send(message);
    }
}
