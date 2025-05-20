package com.example.tiengtrungapp.service;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.stereotype.Service;

@Service
public class PinyinService {

    /**
     * Chuyển đổi chữ Hán sang phiên âm Pinyin
     * @param chineseText Chữ Hán cần chuyển đổi
     * @return Phiên âm Pinyin
     */
    public String convertToPinyin(String chineseText) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        StringBuilder pinyinBuilder = new StringBuilder();
        char[] chars = chineseText.toCharArray();

        try {
            for (char c : chars) {
                // Kiểm tra xem ký tự có phải là chữ Hán không
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    // Lấy tất cả các cách đọc Pinyin có thể có của ký tự
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    // Chọn cách đọc đầu tiên
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        pinyinBuilder.append(pinyinArray[0]).append(" ");
                    }
                } else {
                    // Nếu không phải chữ Hán thì giữ nguyên
                    pinyinBuilder.append(c);
                }
            }
            return pinyinBuilder.toString().trim();
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
            return "Lỗi chuyển đổi Pinyin: " + e.getMessage();
        }
    }
}