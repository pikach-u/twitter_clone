package com.pikachu.backend.controller;

import com.nimbusds.jose.util.Resource;
import com.pikachu.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    /**
//     * 프로필 이미지 업로드
//     */
//    @PostMapping("/profile")
//    public ResponseEntity<Map<String, String>> uploadProfileImage(
//            @RequestParam("image") MultipartFile file
//    ) throws IOException {
//        String fileUrl = fileUploadService.uploadProfileImage(file);
//        return ResponseEntity.ok(Map.of("imageUrl", fileUrl));
//    }
//
//    /**
//     * 업로드된 이미지 서빙
//     */
//    @GetMapping("/profiles/{filename:.+}")
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
//        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
//        Resource resource = new UrlResource(filePath.toUri());
//
//        if (!resource.exists()) {
//            throw new ResourceNotFoundException("파일을 찾을 수 없습니다");
//        }
//
//        // Content-Type 자동 감지
//        String contentType = Files.probeContentType(filePath);
//        if (contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .body(resource);
//    }
}
