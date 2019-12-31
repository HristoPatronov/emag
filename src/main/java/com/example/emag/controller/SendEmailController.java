package com.example.emag.controller;

//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//
public class SendEmailController {
//
//    //send email if discount or quantity update are made
//    private static final String SENDER = "no-reply@ittalents.bg";
//
//    public static void sendMail(String to, String subject, String body){
//        final String username = SENDER;//TODO set your email - a valid gmail account
//        final String password = "password";//TODO set your email pass
//
//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true"); //TLS
//
//        Session session = Session.getInstance(prop,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//
//        try {
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(SENDER));
//            message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse(to)
//            );
//            message.setSubject(subject);
//            message.setText(body);
//
//            Transport.send(message);
//
//            System.out.println("Done");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
}
