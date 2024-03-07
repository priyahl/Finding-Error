package com.findingError.service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.findingError.configuration.Config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Service
public class FileProcessingService1 {
	
	@Autowired
	   private JavaMailSender javaMailSender;
	@Autowired
	Config conf;
	
	 public void process(String directoryPath, String destinationDirectoryPath, String pattern, String date, String filename, String sendToEmail,List<String> ccEmails,String subject, String body) {
	        try {
	            Files.createDirectories(Paths.get(destinationDirectoryPath));

	            List<String> filesToProcess = findFilesToProcess(directoryPath, date, filename);

	            if (filesToProcess.isEmpty()) {
	                throw new IllegalArgumentException("No files found with the specified date and filename.");
	            }

	            processFiles(filesToProcess, destinationDirectoryPath, pattern);

	            sendEmailWithAttachments(sendToEmail,ccEmails,subject,body, destinationDirectoryPath);

	        } catch (IOException | MessagingException e) {
	            e.printStackTrace();
	        } catch (IllegalArgumentException e) {
	            System.err.println(e.getMessage());
	        }
	    }

	   public void processMultiple(String directoryPath, String destinationDirectoryPath, String pattern, List<String> dates, List<String> fileNames, String sendToEmail,List<String> ccEmails,String subject, String body) {
	        try {
	            Files.createDirectories(Paths.get(destinationDirectoryPath));

	            List<String> filesToProcess = findFilesToProcess(directoryPath, dates, fileNames);

	            if (filesToProcess.isEmpty()) {
	                throw new IllegalArgumentException("No files found with the specified dates and filenames.");
	            }

	            processFiles(filesToProcess, destinationDirectoryPath, pattern);

	            sendEmailWithAttachments(sendToEmail, ccEmails,subject,body, destinationDirectoryPath);

	        } catch (IOException | MessagingException e) {
	            e.printStackTrace();
	        } catch (IllegalArgumentException e) {
	            System.err.println(e.getMessage());
	        }
	    }

	   private List<String> findFilesToProcess(String directoryPath, String date, String filename) throws IOException {
		        LocalDate today = LocalDate.now();
		        String currentDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		        if (currentDate.equals(date)) {
		            return Files.list(Paths.get(directoryPath))
		                    .filter(Files::isRegularFile)
		                    .map(Path::toString)
		                    .filter(filePath -> filePath.contains(filename + ".log"))
		                    .collect(Collectors.toList());
		        } else {
		        	return Files.list(Paths.get(directoryPath))
		                    .filter(Files::isRegularFile)
		                    .map(Path::toString)
		                    .filter(filePath -> filePath.contains(filename) && filePath.contains(date))
		                    .collect(Collectors.toList());
		        }
		}
	
    private List<String> findFilesToProcess(String directoryPath, List<String> dates, List<String> fileNames) throws IOException {
	        List<String> filesToProcess = new ArrayList<>();

	        Iterator<String> fIterator = fileNames.iterator();
	        Iterator<String> dIterator = dates.iterator();
	        while (fIterator.hasNext()) {
	            String filename = fIterator.next();
	            String date = dIterator.next();
	            List<String> filesMatchingFilenameAndDate = Files.list(Paths.get(directoryPath))
	                    .filter(Files::isRegularFile)
	                    .map(Path::toString)
	                    .filter(filePath -> filePath.contains(filename) && filePath.contains(date))
	                    .collect(Collectors.toList());
	            filesToProcess.addAll(filesMatchingFilenameAndDate);
	        }
	        return filesToProcess;
	    }

	    private List<String> findFilesContaining(String directoryPath, String... keywords) throws IOException {
	        return Files.list(Paths.get(directoryPath))
	                .filter(Files::isRegularFile)
	                .map(Path::toString)
	                .filter(filePath -> containsAllKeywords(filePath, keywords))
	                .collect(Collectors.toList());
	    }

	    private boolean containsAllKeywords(String filePath, String[] keywords) {
	        for (String keyword : keywords) {
	            if (!filePath.contains(keyword)) {
	                return false;
	            }
	        }
	        return true;
	    }
	   
	    private void processFiles(List<String> filesToProcess, String destinationDirectoryPath, String pattern) {
	        Map<String, BufferedWriter> dateWriters = new HashMap<>();
	        for (String file : filesToProcess) {
	            processFile(file, destinationDirectoryPath, pattern, dateWriters);
	        }
	        // Close all writers
	        dateWriters.values().forEach(writer -> {
	            try {
	                writer.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        });
	    }

	    private void processFile(String file, String destinationDirectoryPath, String pattern, Map<String, BufferedWriter> dateWriters) {
	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	            String fileName = new File(file).getName();
	            String date = extractDate(fileName);

	            BufferedWriter writer = dateWriters.computeIfAbsent(date, d -> {
	                try {
	                    String destinationFilePath = destinationDirectoryPath + File.separator + fileName + ".txt";
	                    return new BufferedWriter(new FileWriter(destinationFilePath));
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    return null;
	                }
	            });

	            if (writer != null) {
	                writer.write("Document: " + fileName + "\n\n");

	                String line;
	                boolean foundPattern = false;

	                while ((line = reader.readLine()) != null) {
	                    if (line.contains(pattern)) {
	                        foundPattern = true;
	                        writeMatchingLine(writer, line, reader);
	                    }
	                }

	                if (!foundPattern) {
	                    writer.write("Pattern '" + pattern + "' not found in the file.\n\n");
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private String extractDate(String fileName) {
	        // Assuming date is in the format YYYYMMDD in the filename
	        return fileName.substring(5, 13);
	    }
	    
	    private void writeMatchingLine(BufferedWriter writer, String line, BufferedReader reader) throws IOException {
	        String[] words = line.split(" ");
	        if (line.contains(conf.getLogLevel())) {
	            writer.write("Time : " + words[0] + "\n" + words[1] + "\n" + "Error :"
	                    + reader.readLine() + "\n" + reader.readLine() + "\n" + "\n");
	        } else if (line.contains(conf.getLogLevel1())) {
	            writer.write("Time : " + words[0] + "\n" + "Error :" + reader.readLine() + "\n"
	                    + reader.readLine() + "\n" + "\n");
	        } else {
	            int errorIndex = line.indexOf("ERROR");
	            String errorMessage = errorIndex != -1 ? line.substring(errorIndex) : "";
	            if (line.contains(conf.getLogLevel2())) {
	                writer.write("Time : " + words[0] + "\n" + words[2] + "\n" + "Password : "
	                        + words[3] + "\n" + "URL : " + words[4] + "\n" + errorMessage + "\n"
	                        + "\n");
	            } else if (line.contains(conf.getLogLevel3())) {
	                writer.write("Time : " + words[0] + "\n" + words[1] + "\n" + errorMessage
	                        + "\n" + reader.readLine() + "\n" + "\n");
	            } else if (line.contains(conf.getLogLevel4())) {
	                writer.write("Time : " + words[0] + "\n" + words[2] + "\n" + "Password : "
	                        + words[3] + "\n" + "URL : " + words[4] + "\n" + errorMessage + "\n"
	                        + reader.readLine() + "\n" + "\n");
	            } else {
	                writer.write("Time : " + words[0] + "\n" + "userId : " + words[2] + "\n"
	                        + "Password : " + words[3] + "\n" + "URL : " + words[4] + "\n"
	                        + errorMessage + "\n" + "\n");
	            }
	        }
	    }
	    
	    
	    
	    public void sendEmailWithAttachments(String sendToEmail, List<String> ccEmails, String subject, String body, String attachmentDirectory) throws MessagingException {
	        // Create a MimeMessage
	        MimeMessage message = javaMailSender.createMimeMessage();

	        try {
	            // Enable multipart mode
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            // Set recipient email address
	            helper.setTo(sendToEmail);

	            // Set CC email addresses
	            if (ccEmails != null && !ccEmails.isEmpty()) {
	                helper.setCc(ccEmails.toArray(new String[0]));
	            }

	            // Set email subject
	            helper.setSubject(subject);

	            // Set email body
	            helper.setText(body);

	            // Add attachments
	            File folder = new File(attachmentDirectory);
	            File[] files = folder.listFiles();
	            if (files != null) {
	                for (File file : files) {
  	                    helper.addAttachment(file.getName(), file);
	                }
	            }

	            // Send email
	            javaMailSender.send(message);

	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }
	    }
	

