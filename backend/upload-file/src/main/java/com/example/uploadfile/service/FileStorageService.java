package com.example.uploadfile.service;

import com.example.uploadfile.model.File;
import com.example.uploadfile.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository repository;

    public File store(MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File fileDB = new File(fileName, file.getContentType(), file.getBytes());

        return repository.save(fileDB);

    }

    public File getFile(String id) {
        return repository.findById(id).get();
    }

    public Stream<File> getAllFiles() {
        return repository.findAll().stream();
    }

}
