package com.gts.godting.config;

import com.gts.godting.config.exception.CustomException;
import com.gts.godting.config.exception.ExceptionMessage;
import com.gts.godting.profile.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileHandler {

    public List<Profile> parseFile(List<MultipartFile> multipartFiles) throws IOException {
        List<Profile> profileList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            //파일 저장할 세부 경로
            String path = "profiles" + File.separator + current_date;
            File file = new File(path);

            if (!file.exists()) {
                boolean createFolder = file.mkdir();

                if (!createFolder) {
                    throw new CustomException(ExceptionMessage.SERVER_FILE_UPLOAD_FAILED);
                }
            }

            for (MultipartFile multipartFile : multipartFiles) {

                String originalFileExtension;
                String contentType = multipartFile.getContentType();

                if (ObjectUtils.isEmpty(contentType)) {
                    continue;
                }
                else{
                    if (contentType.contains("image/jpeg")) {
                        originalFileExtension = ".jpg";
                    } else if (contentType.contains("image/png")) {
                        originalFileExtension = ".png";
                    } else if (multipartFiles.contains("image/gif")) {
                        originalFileExtension = ".gif";
                    } else {
                        continue;
                    }
                }

                String new_file_name = System.nanoTime() + originalFileExtension;
                Profile profile = Profile.builder()
                        .path(path + File.separator + new_file_name)
                        .fileSize(multipartFile.getSize())
                        .name(multipartFile.getOriginalFilename())
                        .save_name(new_file_name)
                        .fileSize(multipartFile.getSize())
                        .create_date_time(LocalDateTime.now())
                        .build();

                profileList.add(profile);

                file = new File(absolutePath + path + File.separator + new_file_name);
                multipartFile.transferTo(file);
            }
        }

        return profileList;
    }
}
