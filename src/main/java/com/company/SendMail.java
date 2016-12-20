package com.company;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SendMail {

    public Multipart addAttachments(List<String> files){
        Multipart multipart = new MimeMultipart();
        for(String file : files){
            try{
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file);
                multipart.addBodyPart(messageBodyPart);
            }
            catch (MessagingException e){
                System.out.println("Problem z wyslaniem emaila");
            }

        }

        return multipart;
    }

    public List<String> prepareListToSend(String pathFile, String pathDirectory) throws Exception{
        File file = new File(pathFile);
        if(!file.exists())
            file.createNewFile();
        List<String> fileList = new ArrayList<>();
        Scanner input = new Scanner(file);
        while(input.hasNextLine()){
            fileList.add(input.nextLine());
        }



        File folder = new File(pathDirectory);
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".pdf");
            }
        });


        List<String> pdfList = new ArrayList<>();
        for(File f : listOfFiles){
            pdfList.add(f.getPath());
        }

        for(String f : fileList){
            for(int i = 0; i < pdfList.size(); i++){
                if(f.equals(pdfList.get(i))){
                    pdfList.remove(i);
                    break;
                }
            }
        }

        Writer output = new BufferedWriter(new FileWriter(pathFile, true));
        for(String f : pdfList){
            output.write(f);
            output.write("\r\n");
        }
        output.close();


        return pdfList;
    }
}


