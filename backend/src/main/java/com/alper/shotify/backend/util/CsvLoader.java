package com.alper.shotify.backend.util;

import com.alper.shotify.backend.entity.SongEntity;
import com.alper.shotify.backend.repository.ISongRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
public class CsvLoader {

    private final ISongRepository songRepository;

    @Autowired
    public CsvLoader(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @PostConstruct
    public void loadCsvData() throws IOException {
        Resource resource = new ClassPathResource("songs.csv");
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            CsvToBean<SongEntity> csvToBean = new CsvToBeanBuilder<SongEntity>(reader)
                    .withType(SongEntity.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<SongEntity> songs = csvToBean.parse();
            songRepository.saveAll(songs);
        }
    }
}