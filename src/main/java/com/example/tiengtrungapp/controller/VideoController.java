package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.service.VideoStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class VideoController {

    private final VideoStorageService videoStorageService;

    /**
     * Upload video file
     */
    @PostMapping("/upload/video")
    public ResponseEntity<Map<String, Object>> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading video: {}", file.getOriginalFilename());

            String fileName = videoStorageService.storeVideo(file);
            String videoUrl = "/api/media/video/" + fileName;

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileName", fileName);
            response.put("videoUrl", videoUrl);
            response.put("originalName", file.getOriginalFilename());
            response.put("size", file.getSize());
            response.put("contentType", file.getContentType());

            log.info("Video uploaded successfully: {}", fileName);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Error uploading video: {}", ex.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Upload image file
     */
    @PostMapping("/upload/image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading image: {}", file.getOriginalFilename());

            String fileName = videoStorageService.storeImage(file);
            String imageUrl = "/api/media/image/" + fileName;

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fileName", fileName);
            response.put("imageUrl", imageUrl);
            response.put("originalName", file.getOriginalFilename());
            response.put("size", file.getSize());
            response.put("contentType", file.getContentType());

            log.info("Image uploaded successfully: {}", fileName);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Error uploading image: {}", ex.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Stream video file with range support
     */
    @GetMapping("/video/{fileName:.+}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String fileName,
            @RequestHeader(value = "Range", required = false) String rangeHeader,
            HttpServletRequest request) {

        try {
            Resource videoResource = videoStorageService.loadVideoAsResource(fileName);

            if (!videoResource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Get video info
            VideoStorageService.VideoInfo videoInfo = videoStorageService.getVideoInfo(fileName);
            String contentType = videoInfo != null ? videoInfo.getContentType() : "video/mp4";

            long contentLength = videoResource.contentLength();

            // Handle range requests for video streaming
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                return handleRangeRequest(videoResource, rangeHeader, contentType, contentLength);
            } else {
                // Full content response
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .contentLength(contentLength)
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .body(videoResource);
            }

        } catch (Exception ex) {
            log.error("Error streaming video {}: {}", fileName, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Serve image file
     */
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = videoStorageService.loadImageAsResource(fileName);

            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                log.info("Could not determine file type for: {}", fileName);
            }

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception ex) {
            log.error("Error serving image {}: {}", fileName, ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get video information
     */
    @GetMapping("/video/{fileName}/info")
    public ResponseEntity<VideoStorageService.VideoInfo> getVideoInfo(@PathVariable String fileName) {
        try {
            VideoStorageService.VideoInfo videoInfo = videoStorageService.getVideoInfo(fileName);
            if (videoInfo != null) {
                return ResponseEntity.ok(videoInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            log.error("Error getting video info for {}: {}", fileName, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete video file
     */
    @DeleteMapping("/video/{fileName}")
    public ResponseEntity<Map<String, Object>> deleteVideo(@PathVariable String fileName) {
        try {
            boolean deleted = videoStorageService.deleteVideo(fileName);

            Map<String, Object> response = new HashMap<>();
            response.put("success", deleted);
            response.put("fileName", fileName);

            if (deleted) {
                log.info("Video deleted successfully: {}", fileName);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "File not found or could not be deleted");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception ex) {
            log.error("Error deleting video {}: {}", fileName, ex.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Handle range requests for video streaming
     */
    private ResponseEntity<Resource> handleRangeRequest(Resource resource, String rangeHeader,
            String contentType, long contentLength) {
        try {
            // Parse range header
            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : contentLength - 1;

            if (end >= contentLength) {
                end = contentLength - 1;
            }

            long rangeLength = end - start + 1;

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + contentLength)
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception ex) {
            log.error("Error handling range request: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
        }
    }
}