package com.example.tiengtrungapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationService {

    @Value("${app.service.mock.translation:true}")
    private boolean useMock;

    // Từ điển đơn giản cho dịch Việt-Trung
    private final Map<String, String> vietnameseToChinese = new HashMap<>();
    
    // Từ điển đơn giản cho dịch Trung-Việt
    private final Map<String, String> chineseToVietnamese = new HashMap<>();
    
    public TranslationService() {
        // Thêm một số từ mẫu cho việc dịch
        initDictionaries();
    }
    
    private void initDictionaries() {
        // Vietnamese to Chinese
        vietnameseToChinese.put("xin chào", "你好");
        vietnameseToChinese.put("cảm ơn", "谢谢");
        vietnameseToChinese.put("tạm biệt", "再见");
        vietnameseToChinese.put("tôi", "我");
        vietnameseToChinese.put("bạn", "你");
        vietnameseToChinese.put("chúng ta", "我们");
        vietnameseToChinese.put("ăn", "吃");
        vietnameseToChinese.put("uống", "喝");
        vietnameseToChinese.put("đi", "去");
        vietnameseToChinese.put("đến", "来");
        vietnameseToChinese.put("học", "学习");
        vietnameseToChinese.put("nói", "说");
        
        // Chinese to Vietnamese
        chineseToVietnamese.put("你好", "xin chào");
        chineseToVietnamese.put("谢谢", "cảm ơn");
        chineseToVietnamese.put("再见", "tạm biệt");
        chineseToVietnamese.put("我", "tôi");
        chineseToVietnamese.put("你", "bạn");
        chineseToVietnamese.put("我们", "chúng ta");
        chineseToVietnamese.put("吃", "ăn");
        chineseToVietnamese.put("喝", "uống");
        chineseToVietnamese.put("去", "đi");
        chineseToVietnamese.put("来", "đến");
        chineseToVietnamese.put("学习", "học");
        chineseToVietnamese.put("说", "nói");
    }

    /**
     * Dịch văn bản từ tiếng Việt sang tiếng Trung
     * @param text Văn bản tiếng Việt
     * @return Văn bản tiếng Trung
     */
    public String translateVietnameseToChinese(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        // Nếu từ có trong từ điển, trả về bản dịch
        if (vietnameseToChinese.containsKey(text.toLowerCase())) {
            return vietnameseToChinese.get(text.toLowerCase());
        }
        
        // Nếu không có trong từ điển, trả về một số chữ Hán phổ biến
        // Đây chỉ là dịch vụ giả lập nên chúng ta không thực sự dịch chính xác
        StringBuilder result = new StringBuilder();
        // Thêm 你好 (Xin chào) vào đầu
        result.append("你好，");
        
        // Thêm một số ký tự Hán ngẫu nhiên (giả lập)
        char[] commonChars = {'我', '你', '他', '她', '们', '是', '有', '好', '在', '这', '那', '和', '的', '了', '个'};
        for (int i = 0; i < text.length() / 2 + 3; i++) {
            int randomIndex = (int) (Math.random() * commonChars.length);
            result.append(commonChars[randomIndex]);
        }
        
        return result.toString();
    }

    /**
     * Dịch văn bản từ tiếng Trung sang tiếng Việt
     * @param text Văn bản tiếng Trung
     * @return Văn bản tiếng Việt
     */
    public String translateChineseToVietnamese(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        // Nếu có trong từ điển, trả về bản dịch
        if (chineseToVietnamese.containsKey(text)) {
            return chineseToVietnamese.get(text);
        }
        
        // Nếu không có, trả về một câu tiếng Việt phổ biến
        return "Xin chào, đây là bản dịch tiếng Việt mẫu.";
    }
}