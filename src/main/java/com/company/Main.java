package com.company;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by KUBA on 2016-12-19.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.println("Podaj swojego emaila:");
        String username = input.nextLine();
        System.out.println("Podaj haslo:");
        String password = input.nextLine();
        System.out.println("Podaj emaila na ktory chcesz wyslac wiadomosc: ");
        String email = input.nextLine();
        System.out.println("Podaj sciezke do folderu z ktorego zamierzasz wyslac pliki pdf: ");
        String path = input.nextLine();
        System.out.println("Podaj sciezke do pliku ze sciezkami plikow ktorych nie zamierzasz wysylac: ");
        String filePaths = input.nextLine();
        System.out.println("Trwa wysylanie emaila...");
        SendMail sendMail = new SendMail();

        List<String> files = sendMail.prepareListToSend(filePaths, path);

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Testing Subject");


            Multipart multipart = sendMail.addAttachments(files);
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Wyslano emaila");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
