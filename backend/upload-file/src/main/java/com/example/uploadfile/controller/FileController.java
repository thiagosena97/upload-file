package com.example.uploadfile.controller;

import com.example.uploadfile.message.ResponseFile;
import com.example.uploadfile.message.ResponseMessage;
import com.example.uploadfile.model.File;
import com.example.uploadfile.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:8080")
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

        String message = "";

        try {

            storageService.store(file);
            message = "Upload realizado com Sucesso: " + file.getOriginalFilename();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (Exception e) {

            message = "Não foi possível realizar o Upload do arquivo" + file.getOriginalFilename();

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

        }

    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {

        List<ResponseFile> files = storageService.getAllFiles()
                .map(file -> {
                    String fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/files/")
                            .path(file.getId())
                            .toUriString();

                    return new ResponseFile(
                            file.getName(),
                            fileDownloadUri,
                            file.getType(),
                            file.getData().length);

                }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {

        File file = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }
}
