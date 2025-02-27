package com.alper.shotify.backend.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoAnalysisProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int photoId;
    private String status;
    @ElementCollection
    private List<Integer> recommendedSongIds;
}
