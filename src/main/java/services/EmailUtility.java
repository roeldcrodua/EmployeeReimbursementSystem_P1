package services;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailUtility {
    // https://www.journaldev.com/2532/javamail-example-send-mail-in-java-smtp

    public static void sendConfirmationEmail(String toEmail, String userName, String userPassword){
        try
        {
            final String fromEmail = "noreply.ers.rdc@gmail.com"; //requires valid gmail id
            final String password = "P4ssw0rd!"; // correct password for gmail id
            final String link = "http://localhost:9000/";

            String subject = "ERS - User Registration Confirmation";
            String body = "Welcome to ERS! \n\nThis is to confirm that your email address was added to the system. \n\n" +
                    "Please login to the link attached using the provided username and password \n\n" +
                    "USERNAME: " + userName + " \n " +
                    "PASSWORD: " + userPassword + " \n" +
                    "LINK: " + link;

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            Transport.send(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendResetPasswordEmail(String toEmail, String userName){
        try
        {
            final String fromEmail = "noreply.ers.rdc@gmail.com"; //requires valid gmail id
            final String password = "P4ssw0rd!"; // correct password for gmail id
            final String link = "http://localhost:9000/";

            String subject = "ERS - Reset Password Confirmation";
            String body = "Hi " + userName + ",\n\n This is to confirm that you have successfully changed your password. \n\n" +
                    "LOGIN LINK: " + link;

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };

            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            Transport.send(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String generatePassword(){
        // https://stackoverflow.com/questions/19743124/java-password-generator#:~:text=The%20one%20line%20random%20password%20generator%20by%20standard,%28%29.ints%20%2810%2C%2033%2C%20122%29.collect%20%28StringBuilder%3A%3Anew%2C%20StringBuilder%3A%3AappendCodePoint%2C%20StringBuilder%3A%3Aappend%29.toString%20%28%29%3B

        return new Random()
                .ints(10, 33, 122)
                .mapToObj(i -> String.valueOf((char)i))
                .collect(Collectors.joining());
    }
}
