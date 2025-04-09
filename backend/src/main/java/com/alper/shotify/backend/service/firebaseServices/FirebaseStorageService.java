package com.alper.shotify.backend.service.firebaseServices;

import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseStorageService {

    public String uploadFile(MultipartFile file, String destinationFolder) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename;
        String blobString = destinationFolder + "/" + fileName;

        StorageClient.getInstance().bucket().create(blobString, file.getBytes(), file.getContentType());

        String bucketName = StorageClient.getInstance().bucket().getName();
        String encodedBlob = URLEncoder.encode(blobString, StandardCharsets.UTF_8);
        String publicUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucketName, encodedBlob);
        return publicUrl;
    }
}
