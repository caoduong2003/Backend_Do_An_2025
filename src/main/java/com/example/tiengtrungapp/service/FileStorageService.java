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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${app.file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("Upload directory created/verified: " + this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Store file với tên UUID (method gốc của bạn - giữ nguyên để tương thích)
     */
    public String storeFile(MultipartFile file) {
        return storeFile(file, false); // Mặc định dùng UUID
    }

    /**
     * Store file với option giữ tên gốc hoặc dùng UUID
     * @param file File cần upload
     * @param keepOriginalName true nếu muốn giữ tên gốc, false nếu dùng UUID
     * @return tên file đã lưu
     */
    public String storeFile(MultipartFile file, boolean keepOriginalName) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            String fileName;
            if (keepOriginalName) {
                // GIỮ TÊN GỐC - nhưng xử lý tên file an toàn
                fileName = sanitizeFileName(originalFileName);
                
                // Nếu file đã tồn tại, thêm số thứ tự
                fileName = getUniqueFileName(fileName);
                
                System.out.println("Storing file with original name: " + fileName);
            } else {
                // DÙNG UUID (cách cũ của bạn)
                String fileExtension = "";
                if (originalFileName.lastIndexOf('.') > 0) {
                    fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                }
                fileName = UUID.randomUUID().toString() + fileExtension;
                
                System.out.println("Storing file with UUID name: " + fileName);
            }
            
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File saved successfully: " + fileName + " (original: " + originalFileName + ")");
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    /**
     * Load file as resource (method gốc của bạn - giữ nguyên)
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                System.out.println("File loaded successfully: " + fileName);
                return resource;
            } else {
                System.err.println("File not found: " + fileName);
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            System.err.println("Malformed URL for file: " + fileName);
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    /**
     * Làm sạch tên file để an toàn
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "unnamed_file";
        }
        
        // Thay thế các ký tự không an toàn bằng underscore
        fileName = fileName.replaceAll("[^a-zA-Z0-9.\\-_()\\s]", "_");
        
        // Thay thế nhiều spaces liên tiếp bằng một space
        fileName = fileName.replaceAll("\\s+", " ");
        
        // Trim spaces
        fileName = fileName.trim();
        
        // Đảm bảo tên file không quá dài
        if (fileName.length() > 100) {
            String extension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = fileName.substring(dotIndex);
                fileName = fileName.substring(0, 100 - extension.length()) + extension;
            } else {
                fileName = fileName.substring(0, 100);
            }
        }
        
        // Đảm bảo tên file không rỗng sau khi sanitize
        if (fileName.trim().isEmpty()) {
            fileName = "file_" + System.currentTimeMillis();
        }
        
        System.out.println("Sanitized filename: " + fileName);
        return fileName;
    }

    /**
     * Tạo tên file unique nếu file đã tồn tại
     */
    private String getUniqueFileName(String fileName) {
        Path filePath = this.fileStorageLocation.resolve(fileName);
        
        if (!Files.exists(filePath)) {
            return fileName;
        }
        
        // File đã tồn tại, thêm số thứ tự
        String nameWithoutExt = fileName;
        String extension = "";
        
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            nameWithoutExt = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        }
        
        int counter = 1;
        String newFileName;
        do {
            newFileName = nameWithoutExt + "_" + counter + extension;
            filePath = this.fileStorageLocation.resolve(newFileName);
            counter++;
        } while (Files.exists(filePath) && counter < 1000); // Giới hạn để tránh infinite loop
        
        System.out.println("Generated unique filename: " + newFileName);
        return newFileName;
    }

    /**
     * Liệt kê tất cả files trong thư mục upload (cho debug)
     */
    public List<String> listAllFiles() {
        List<String> fileNames = new ArrayList<>();
        try {
            Files.list(this.fileStorageLocation)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        fileNames.add(fileName);
                        System.out.println("File in upload dir: " + fileName);
                    });
        } catch (IOException e) {
            System.err.println("Error listing files: " + e.getMessage());
        }
        return fileNames;
    }

    /**
     * Lấy đường dẫn upload directory
     */
    public String getUploadDirectory() {
        return this.fileStorageLocation.toString();
    }

    /**
     * Kiểm tra file có tồn tại không
     */
    public boolean fileExists(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            boolean exists = Files.exists(filePath);
            System.out.println("File exists check - " + fileName + ": " + exists);
            return exists;
        } catch (Exception e) {
            System.err.println("Error checking file existence: " + fileName + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa file
     */
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            boolean deleted = Files.deleteIfExists(filePath);
            System.out.println("File deletion - " + fileName + ": " + (deleted ? "success" : "file not found"));
            return deleted;
        } catch (IOException e) {
            System.err.println("Error deleting file: " + fileName + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy kích thước file
     */
    public long getFileSize(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                long size = Files.size(filePath);
                System.out.println("File size - " + fileName + ": " + size + " bytes");
                return size;
            }
        } catch (IOException e) {
            System.err.println("Error getting file size: " + fileName + " - " + e.getMessage());
        }
        return -1;
    }

    /**
     * Lấy thông tin chi tiết file
     */
    public Map<String, Object> getFileInfo(String fileName) {
        Map<String, Object> info = new HashMap<>();
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                info.put("fileName", fileName);
                info.put("size", Files.size(filePath));
                info.put("lastModified", Files.getLastModifiedTime(filePath).toString());
                info.put("isReadable", Files.isReadable(filePath));
                info.put("path", filePath.toString());
                
                // Thêm content type guess
                String contentType = Files.probeContentType(filePath);
                info.put("contentType", contentType);
                
                System.out.println("File info retrieved for: " + fileName);
            } else {
                info.put("error", "File not found: " + fileName);
            }
        } catch (IOException e) {
            info.put("error", "Error getting file info: " + e.getMessage());
            System.err.println("Error getting file info: " + fileName + " - " + e.getMessage());
        }
        return info;
    }
}