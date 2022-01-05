package com.example.uploadfile.repository;

import com.example.uploadfile.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<File, String> { }
