package com.example.loganalyzer.model;

public class LogRequest {

    private String filePath;
    private String outputPath;

    public LogRequest() {
    }

    public LogRequest(String filePath, String outputPath) {
        this.filePath = filePath;
        this.outputPath = outputPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
