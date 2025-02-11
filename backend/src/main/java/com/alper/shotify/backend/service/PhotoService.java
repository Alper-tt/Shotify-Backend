package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.model.request.CreatePhotoRequestDTO;
import com.alper.shotify.backend.model.request.UpdatePhotoRequestDTO;
import com.alper.shotify.backend.model.response.PhotoResponseDTO;
import com.alper.shotify.backend.repository.IPhotoRepository;
import com.alper.shotify.backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final IPhotoRepository photoRepository;
    private final IUserRepository userRepository;

    public PhotoResponseDTO createPhoto (CreatePhotoRequestDTO requestDTO){
        UserEntity user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));

        PhotoEntity photo = PhotoEntity.builder()
                .url(requestDTO.getUrl())
                .analysisData(requestDTO.getAnalysisData())
                .recommendations(requestDTO.getRecommendation())
                .photoPath(requestDTO.getPhotoPath())
                .user(user)
                .build();

        PhotoEntity savedPhoto = photoRepository.save(photo);
        return new PhotoResponseDTO(savedPhoto.getPhotoId(),
                savedPhoto.getUser().getUserId(),
                savedPhoto.getPhotoPath(),
                savedPhoto.getUrl());
    }

    public PhotoResponseDTO getPhotoById(int photoId){
        PhotoEntity photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı"));
        return new PhotoResponseDTO(photo.getPhotoId(),
                photo.getUser().getUserId(),
                photo.getPhotoPath(),
                photo.getUrl());
    }

    public List<PhotoResponseDTO> getAllPhotos(){
        List<PhotoEntity> photos = photoRepository.findAll();
        if(photos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return photos.stream()
                .map(photo -> new PhotoResponseDTO(
                        photo.getPhotoId(),
                        photo.getUser().getUserId(),
                        photo.getPhotoPath(),
                        photo.getUrl()
                )).toList();
    }

    public void deletePhoto (int photoId){
        if(!photoRepository.existsById(photoId))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı");
        }
        photoRepository.deleteById(photoId);
    }

    public PhotoResponseDTO updatePhoto(UpdatePhotoRequestDTO requestDTO){
        PhotoEntity existPhoto = photoRepository.findById(requestDTO.getPhotoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı"));
        userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
        existPhoto.setUrl(requestDTO.getUrl());
        existPhoto.setAnalysisData(requestDTO.getAnalysisData());
        existPhoto.setRecommendations(requestDTO.getRecommendation());
        photoRepository.save(existPhoto);

        return new PhotoResponseDTO(existPhoto.getPhotoId(),
                existPhoto.getUser().getUserId(),
                existPhoto.getPhotoPath(),
                existPhoto.getUrl());
    }
}
