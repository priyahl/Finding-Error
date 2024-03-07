package com.findingError.model;

import java.util.ArrayList;
import java.util.List;

public class FileProcessingRequest {
	  private String sourceFilePath;
	    private String destinationFilePath;
	    private String pattern;
	    private String directoryPath;
	    private String date;
	    private String destinationDirectoryPath;
	    private String fileName;
	    private List<String> fileNames = new ArrayList<>();
	    private List<String> dates = new ArrayList<>();
	    private String sendToEmail;
	    private List<String> ccEmails;
	    private String subject;
	    private String body;
	    
		public String getSourceFilePath() {
			return sourceFilePath;
		}
		public void setSourceFilePath(String sourceFilePath) {
			this.sourceFilePath = sourceFilePath;
		}
		public String getDestinationFilePath() {
			return destinationFilePath;
		}
		public void setDestinationFilePath(String destinationFilePath) {
			this.destinationFilePath = destinationFilePath;
		}
		public String getPattern() {
			return pattern;
		}
		public void setPattern(String pattern) {
			this.pattern = pattern;
		}
		public String getDirectoryPath() {
			return directoryPath;
		}
		public void setDirectoryPath(String directoryPath) {
			this.directoryPath = directoryPath;
		}
		public String getDestinationDirectoryPath() {
			return destinationDirectoryPath;
		}
		public void setDestinationDirectoryPath(String destinationDirectoryPath) {
			this.destinationDirectoryPath = destinationDirectoryPath;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public List<String> getFileNames() {
			return fileNames;
		}
		public void setFileNames(List<String> fileNames) {
			this.fileNames = fileNames;
		}
		public List<String> getDates() {
			return dates;
		}
		public void setDates(List<String> dates) {
			this.dates = dates;
		}
		public String getSendToEmail() {
			return sendToEmail;
		}
		public void setSendToEmail(String sendToEmail) {
			this.sendToEmail = sendToEmail;
		}
		public List<String> getCcEmails() {
			return ccEmails;
		}
		public void setCcEmails(List<String> ccEmails) {
			this.ccEmails = ccEmails;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		
	
	    
}
