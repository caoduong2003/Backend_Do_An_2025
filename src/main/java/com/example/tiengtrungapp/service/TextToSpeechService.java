package com.example.tiengtrungapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TextToSpeechService {

    @Value("${app.service.mock.tts:true}")
    private boolean useMock;
    
    @Value("${app.file.upload-dir}")
    private String uploadDir;
    
    // Map lưu trữ file âm thanh mẫu cho các từ tiếng Trung phổ biến
    private final Map<String, String> sampleAudioMap = new HashMap<>();
    
    // Đường dẫn tới file âm thanh mẫu
    private final String defaultAudioPath = "classpath:static/audio/default.mp3";
    
    public TextToSpeechService() {
        // Khởi tạo map các từ mẫu
        initSampleAudioMap();
    }
    
    private void initSampleAudioMap() {
        // Trong thực tế, bạn sẽ có các file âm thanh mẫu trong thư mục resources
        // Ở đây chúng ta chỉ giả lập với một số từ phổ biến
        sampleAudioMap.put("你好", "ni_hao.mp3");
        sampleAudioMap.put("谢谢", "xie_xie.mp3");
        sampleAudioMap.put("再见", "zai_jian.mp3");
    }

    /**
     * Chuyển đổi văn bản tiếng Trung sang giọng nói
     * @param text Văn bản tiếng Trung
     * @return URL của file audio
     */
    public String textToSpeech(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        try {
            // Tạo thư mục upload nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo file âm thanh mới
            String fileName = "audio_" + UUID.randomUUID().toString() + ".mp3";
            Path filePath = uploadPath.resolve(fileName);
            
            // Copy file âm thanh mẫu (giả lập)
            File audioFile;
            if (sampleAudioMap.containsKey(text)) {
                // Nếu có file mẫu cho từ này
                String sampleFileName = sampleAudioMap.get(text);
                audioFile = ResourceUtils.getFile("classpath:static/audio/" + sampleFileName);
            } else {
                // Nếu không có, dùng file mẫu mặc định
                audioFile = ResourceUtils.getFile(defaultAudioPath);
            }
            
            try (FileInputStream in = new FileInputStream(audioFile)) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // Trả về URL tương đối để truy cập file
            return "/api/files/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Lỗi tạo file âm thanh: " + e.getMessage();
        }
    }
}