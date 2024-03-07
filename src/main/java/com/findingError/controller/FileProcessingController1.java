package com.findingError.controller;

import org.springframework.web.bind.annotation.RestController;

import com.findingError.model.FileProcessingRequest;
import com.findingError.service.FileProcessingService1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class FileProcessingController1 {
	
	 @Autowired
	    private FileProcessingService1 fileProcessingService1;
	 
	  @PostMapping("/process")
	    public String processFiles(@RequestBody FileProcessingRequest request) {
	        try {
	        	fileProcessingService1.process(
	                    request.getDirectoryPath(),
	                    request.getDestinationDirectoryPath(),
	                    request.getPattern(),
	                    request.getDate(),
	                    request.getFileName(),
	                    request.getSendToEmail(),
	                    request.getCcEmails(),
	                    request.getSubject(),
	                    request.getBody()
	            );
	            return "Processing request received successfully.";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error processing request: " + e.getMessage();
	        }
	    }

	  @PostMapping("/processMultiple")
	    public String processMultipleFiles(@RequestBody FileProcessingRequest request) {
	        try {
	        	fileProcessingService1.processMultiple(
	                    request.getDirectoryPath(),
	                    request.getDestinationDirectoryPath(),
	                    request.getPattern(),
	                    request.getDates(),
	                    request.getFileNames(),
	                    request.getSendToEmail(),
	                    request.getCcEmails(),
	                    request.getSubject(),
	                    request.getBody()
	            );
	            return "Processing request received successfully.";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error processing request: " + e.getMessage();
	        }
	    }
}
