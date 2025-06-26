package com.example.tiengtrungapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;

@Service
public class VideoStorageService {

    private final Path videoStorageLocation;
    private final Path imageStorageLocation;

    // Allowed video formats
    private final List<String> allowedVideoExtensions = Arrays.asList(
            ".mp4", ".avi", ".mkv", ".mov", ".wmv", ".flv", ".webm");

    // Allowed image formats
    private final List<String> allowedImageExtensions = Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");

    public VideoStorageService(@Value("${app.video.storage.path:uploads/videos/}") String videoStoragePath,
            @Value("${app.image.storage.path:uploads/images/}") String imageStoragePath) {

        this.videoStorageLocation = Paths.get(videoStoragePath).toAbsolutePath().normalize();
        this.imageStorageLocation = Paths.get(imageStoragePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.videoStorageLocation);
            Files.createDirectories(this.imageStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create storage directories.", ex);
        }
    }

    /**
     * Upload video file
     */
    public String storeVideo(MultipartFile file) {
        return storeFile(file, videoStorageLocation, allowedVideoExtensions, "video");
    }

    /**
     * Upload image file
     */
    public String storeImage(MultipartFile file) {
        return storeFile(file, imageStorageLocation, allowedImageExtensions, "image");
    }

    /**
     * Generic file storage method
     */
    private String storeFile(MultipartFile file, Path storageLocation, List<String> allowedExtensions,
            String fileType) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Validate file name
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Invalid file path: " + originalFileName);
            }

            // Validate file extension
            String fileExtension = getFileExtension(originalFileName).toLowerCase();
            if (!allowedExtensions.contains(fileExtension)) {
                throw new RuntimeException("File type not allowed for " + fileType + ": " + fileExtension);
            }

            // Validate file size (max 100MB for videos, 10MB for images)
            long maxSize = fileType.equals("video") ? 100 * 1024 * 1024 : 10 * 1024 * 1024;
            if (file.getSize() > maxSize) {
                throw new RuntimeException("File size too large. Max size: " + (maxSize / 1024 / 1024) + "MB");
            }

            // Generate unique file name
            String fileName = generateUniqueFileName(originalFileName);

            // Store file
            Path targetLocation = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName, ex);
        }
    }

    /**
     * Load video file as resource
     */
    public Resource loadVideoAsResource(String fileName) {
        return loadFileAsResource(fileName, videoStorageLocation);
    }

    /**
     * Load image file as resource
     */
    public Resource loadImageAsResource(String fileName) {
        return loadFileAsResource(fileName, imageStorageLocation);
    }

    /**
     * Generic file loading method
     */
    private Resource loadFileAsResource(String fileName, Path storageLocation) {
        try {
            Path filePath = storageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + fileName, ex);
        }
    }

    /**
     * Delete video file
     */
    public boolean deleteVideo(String fileName) {
        return deleteFile(fileName, videoStorageLocation);
    }

    /**
     * Delete image file
     */
    public boolean deleteImage(String fileName) {
        return deleteFile(fileName, imageStorageLocation);
    }

    /**
     * Generic file deletion method
     */
    private boolean deleteFile(String fileName, Path storageLocation) {
        try {
            Path filePath = storageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Get file information
     */
    public VideoInfo getVideoInfo(String fileName) {
        try {
            Path filePath = videoStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                VideoInfo info = new VideoInfo();
                info.setFileName(fileName);
                info.setFileSize(Files.size(filePath));
                info.setContentType(getContentType(fileName));
                return info;
            }
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Check if video file exists
     */
    public boolean videoExists(String fileName) {
        Path filePath = videoStorageLocation.resolve(fileName).normalize();
        return Files.exists(filePath);
    }

    /**
     * Generate unique file name
     */
    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = getFileExtension(originalFileName);
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String sanitizedBaseName = baseName.replaceAll("[^a-zA-Z0-9]", "_");

        return sanitizedBaseName + "_" + UUID.randomUUID().toString() + fileExtension;
    }

    /**
     * Get file extension
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex);
    }

    /**
     * Get content type based on file extension
     */
    private String getContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        switch (extension) {
            case ".mp4":
                return "video/mp4";
            case ".avi":
                return "video/x-msvideo";
            case ".mkv":
                return "video/x-matroska";
            case ".mov":
                return "video/quicktime";
            case ".wmv":
                return "video/x-ms-wmv";
            case ".flv":
                return "video/x-flv";
            case ".webm":
                return "video/webm";
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * Video information class
     */
    public static class VideoInfo {
        private String fileName;
        private long fileSize;
        private String contentType;

        // Getters and setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }
}