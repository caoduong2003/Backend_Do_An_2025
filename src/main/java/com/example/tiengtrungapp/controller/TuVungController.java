package com.example.tiengtrungapp.controller;

import com.example.tiengtrungapp.model.entity.TuVung;
import com.example.tiengtrungapp.repository.TuVungRepository;
import com.example.tiengtrungapp.service.PinyinService;
import com.example.tiengtrungapp.service.TextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tuvung")
@CrossOrigin(origins = "*")
public class TuVungController {

    @Autowired
    private TuVungRepository tuVungRepository;

    @Autowired
    private PinyinService pinyinService;

    @Autowired
    private TextToSpeechService textToSpeechService;

    @GetMapping
    public ResponseEntity<List<TuVung>> getAllTuVung() {
        try {
            List<TuVung> tuVungs = tuVungRepository.findAll();
            return new ResponseEntity<>(tuVungs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TuVung> getTuVungById(@PathVariable Long id) {
        Optional<TuVung> tuVung = tuVungRepository.findById(id);
        if (tuVung.isPresent()) {
            return ResponseEntity.ok(tuVung.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/baigiang/{baiGiangId}")
    public ResponseEntity<List<TuVung>> getTuVungByBaiGiang(@PathVariable Long baiGiangId) {
        List<TuVung> tuVungs = tuVungRepository.findByBaiGiangId(baiGiangId);
        return ResponseEntity.ok(tuVungs);
    }

    @PostMapping
    public ResponseEntity<TuVung> createTuVung(@RequestBody TuVung tuVung) {
        try {
            // Tự động tạo phiên âm nếu chưa có
            if (tuVung.getPhienAm() == null || tuVung.getPhienAm().isEmpty()) {
                String pinyin = pinyinService.convertToPinyin(tuVung.getTiengTrung());
                tuVung.setPhienAm(pinyin);
            }

            // Tự động tạo audio nếu chưa có
            if (tuVung.getAudioURL() == null || tuVung.getAudioURL().isEmpty()) {
                String audioUrl = textToSpeechService.textToSpeech(tuVung.getTiengTrung());
                tuVung.setAudioURL(audioUrl);
            }

            TuVung savedTuVung = tuVungRepository.save(tuVung);
            return new ResponseEntity<>(savedTuVung, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TuVung> updateTuVung(@PathVariable Long id, @RequestBody TuVung tuVung) {
        Optional<TuVung> tuVungData = tuVungRepository.findById(id);

        if (tuVungData.isPresent()) {
            TuVung updatedTuVung = tuVungData.get();
            updatedTuVung.setTiengTrung(tuVung.getTiengTrung());
            updatedTuVung.setPhienAm(tuVung.getPhienAm());
            updatedTuVung.setTiengViet(tuVung.getTiengViet());
            updatedTuVung.setLoaiTu(tuVung.getLoaiTu());
            updatedTuVung.setViDu(tuVung.getViDu());
            updatedTuVung.setHinhAnh(tuVung.getHinhAnh());
            updatedTuVung.setAudioURL(tuVung.getAudioURL());
            updatedTuVung.setGhiChu(tuVung.getGhiChu());
            updatedTuVung.setCapDoHSK(tuVung.getCapDoHSK());

            return new ResponseEntity<>(tuVungRepository.save(updatedTuVung), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTuVung(@PathVariable Long id) {
        try {
            tuVungRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<TuVung>> searchTuVung(@RequestParam String keyword,
            @RequestParam(required = false) String language) {
        List<TuVung> tuVungs;

        if ("vi".equals(language)) {
            tuVungs = tuVungRepository.findByTiengVietContaining(keyword);
        } else {
            tuVungs = tuVungRepository.findByTiengTrungContaining(keyword);
        }

        return ResponseEntity.ok(tuVungs);
    }

    @PostMapping("/pinyin")
    public ResponseEntity<String> generatePinyin(@RequestBody String chineseText) {
        try {
            String pinyin = pinyinService.convertToPinyin(chineseText);
            return ResponseEntity.ok(pinyin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo pinyin: " + e.getMessage());
        }
    }

    @PostMapping("/audio")
    public ResponseEntity<String> generateAudio(@RequestBody String chineseText) {
        try {
            String audioUrl = textToSpeechService.textToSpeech(chineseText);
            return ResponseEntity.ok(audioUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo audio: " + e.getMessage());
        }
    }
}