package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/translation")
@CrossOrigin(origins = "*")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping("/vi-to-zh")
    public ResponseEntity<Map<String, String>> translateVietnameseToChinese(@RequestBody String text) {
        String translatedText = translationService.translateVietnameseToChinese(text);
        Map<String, String> response = new HashMap<>();
        response.put("originalText", text);
        response.put("translatedText", translatedText);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/zh-to-vi")
    public ResponseEntity<Map<String, String>> translateChineseToVietnamese(@RequestBody String text) {
        String translatedText = translationService.translateChineseToVietnamese(text);
        Map<String, String> response = new HashMap<>();
        response.put("originalText", text);
        response.put("translatedText", translatedText);
        return ResponseEntity.ok(response);
    }
}