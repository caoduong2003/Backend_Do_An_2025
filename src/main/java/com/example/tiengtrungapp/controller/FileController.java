package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(fileName)
                    .toUriString();

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileDownloadUri", fileDownloadUri);
            response.put("fileType", file.getContentType());
            response.put("size", String.valueOf(file.getSize()));

            System.out.println("File uploaded successfully: " + fileName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error uploading file: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        try {
            // QUAN TRỌNG: Debug thông tin request
            System.out.println("=== FILE REQUEST DEBUG ===");
            System.out.println("Original fileName: " + fileName);
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Request URL: " + request.getRequestURL());
            
            // QUAN TRỌNG: Decode URL nếu cần thiết
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            System.out.println("Decoded fileName: " + decodedFileName);
            
            // Thử load file với tên gốc trước
            Resource resource = null;
            try {
                resource = fileStorageService.loadFileAsResource(fileName);
                System.out.println("Loaded file with original name: " + fileName);
            } catch (Exception e1) {
                // Nếu không được, thử với tên đã decode
                try {
                    resource = fileStorageService.loadFileAsResource(decodedFileName);
                    System.out.println("Loaded file with decoded name: " + decodedFileName);
                } catch (Exception e2) {
                    System.err.println("Cannot load file with original name: " + e1.getMessage());
                    System.err.println("Cannot load file with decoded name: " + e2.getMessage());
                    throw new RuntimeException("File not found: " + fileName);
                }
            }

            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                System.out.println("Content type from servlet context: " + contentType);
            } catch (IOException ex) {
                contentType = determineContentTypeFromFileName(fileName);
                System.out.println("Content type from filename: " + contentType);
            }

            // Fallback to the default content type if type could not be determined
            if (contentType == null) {
                contentType = "application/octet-stream";
                System.out.println("Using fallback content type: " + contentType);
            }

            // QUAN TRỌNG: Xử lý khác nhau cho video/audio vs file khác
            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType));

            // Nếu là video hoặc audio, KHÔNG thêm Content-Disposition để browser có thể stream
            if (isMediaFile(contentType, fileName)) {
                // Thêm các headers cho media streaming
                responseBuilder
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000") // Cache 1 năm cho media
                    .header("Accept-Ranges", "bytes"); // Cho phép range requests cho video
                
                System.out.println("Serving media file: " + fileName + " with content-type: " + contentType);
            } else {
                // Với file khác (image, document), vẫn có thể inline view
                // KHÔNG dùng attachment cho hình ảnh để có thể hiển thị trực tiếp
                if (contentType.startsWith("image/")) {
                    responseBuilder.header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400"); // Cache 1 ngày cho image
                    System.out.println("Serving image file: " + fileName + " with content-type: " + contentType);
                } else {
                    responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + resource.getFilename() + "\"");
                    System.out.println("Serving attachment: " + fileName + " with content-type: " + contentType);
                }
            }

            System.out.println("File served successfully: " + fileName);
            return responseBuilder.body(resource);

        } catch (Exception e) {
            System.err.println("ERROR serving file " + fileName + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Xác định xem có phải file media (video/audio) không
     */
    private boolean isMediaFile(String contentType, String fileName) {
        if (contentType != null) {
            return contentType.startsWith("video/") || contentType.startsWith("audio/");
        }
        
        // Fallback: check bằng file extension
        String lowerFileName = fileName.toLowerCase();
        return lowerFileName.endsWith(".mp4") || lowerFileName.endsWith(".avi") ||
               lowerFileName.endsWith(".mov") || lowerFileName.endsWith(".mkv") ||
               lowerFileName.endsWith(".webm") || lowerFileName.endsWith(".3gp") ||
               lowerFileName.endsWith(".mp3") || lowerFileName.endsWith(".wav") ||
               lowerFileName.endsWith(".ogg") || lowerFileName.endsWith(".aac");
    }

    /**
     * Xác định content type từ tên file
     */
    private String determineContentTypeFromFileName(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        
        // Video formats
        if (lowerFileName.endsWith(".mp4")) return "video/mp4";
        if (lowerFileName.endsWith(".avi")) return "video/x-msvideo";
        if (lowerFileName.endsWith(".mov")) return "video/quicktime";
        if (lowerFileName.endsWith(".mkv")) return "video/x-matroska";
        if (lowerFileName.endsWith(".webm")) return "video/webm";
        if (lowerFileName.endsWith(".3gp")) return "video/3gpp";
        
        // Audio formats
        if (lowerFileName.endsWith(".mp3")) return "audio/mpeg";
        if (lowerFileName.endsWith(".wav")) return "audio/wav";
        if (lowerFileName.endsWith(".ogg")) return "audio/ogg";
        if (lowerFileName.endsWith(".aac")) return "audio/aac";
        
        // Image formats
        if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) return "image/jpeg";
        if (lowerFileName.endsWith(".png")) return "image/png";
        if (lowerFileName.endsWith(".gif")) return "image/gif";
        if (lowerFileName.endsWith(".webp")) return "image/webp";
        if (lowerFileName.endsWith(".bmp")) return "image/bmp";
        
        return null;
    }

    /**
     * Endpoint để test xem file có tồn tại không
     */
    @RequestMapping(value = "/{fileName:.+}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkFile(@PathVariable String fileName) {
        try {
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            
            Resource resource = null;
            try {
                resource = fileStorageService.loadFileAsResource(fileName);
            } catch (Exception e1) {
                resource = fileStorageService.loadFileAsResource(decodedFileName);
            }
            
            if (resource.exists()) {
                System.out.println("HEAD request - File exists: " + fileName);
                return ResponseEntity.ok().build();
            } else {
                System.out.println("HEAD request - File not found: " + fileName);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("HEAD request - Error checking file: " + fileName + " - " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint để debug - liệt kê files trong thư mục upload
     */
    @GetMapping("/debug/list")
    public ResponseEntity<Map<String, Object>> listFiles() {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Sử dụng methods từ FileStorageService (đã có trong FileStorageService mới)
            List<String> files = fileStorageService.listAllFiles();
            String uploadDir = fileStorageService.getUploadDirectory();
            
            response.put("message", "Files in upload directory:");
            response.put("uploadDirectory", uploadDir);
            response.put("files", files);
            response.put("totalFiles", files.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Endpoint để test connectivity
     */
    @GetMapping("/debug/test")
    public ResponseEntity<Map<String, Object>> testConnectivity() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "FileController is working");
        response.put("timestamp", System.currentTimeMillis());
        
        System.out.println("Test connectivity endpoint called");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint để upload file với option giữ tên gốc
     */
    @PostMapping("/upload-keep-name")
    public ResponseEntity<Map<String, String>> uploadFileKeepName(@RequestParam("file") MultipartFile file) {
        try {
            // Upload với option giữ tên gốc
            String fileName = fileStorageService.storeFile(file, true);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/")
                    .path(fileName)
                    .toUriString();

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileDownloadUri", fileDownloadUri);
            response.put("fileType", file.getContentType());
            response.put("size", String.valueOf(file.getSize()));
            response.put("originalName", file.getOriginalFilename());

            System.out.println("File uploaded with original name: " + fileName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error uploading file with original name: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Exception handler cho FileController
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        System.err.println("FileController Exception: " + e.getMessage());
        e.printStackTrace();
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        errorResponse.put("controller", "FileController");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}