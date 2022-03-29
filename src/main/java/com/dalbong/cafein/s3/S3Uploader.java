package com.dalbong.cafein.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ImageRepository;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;


    /**
     * S3 다중 업로드
     */
    public List<String> s3MultipleUpload(List<MultipartFile> multipartFiles) throws IOException {
        List<String> imageUrlList = new ArrayList<>();

        if (!multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {
                imageUrlList.add(s3Upload(multipartFile));
            }
        }

        return imageUrlList;
    }

    /**
     * S3 업로드
     */
    public String s3Upload(MultipartFile multipartFile) throws IOException {

        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        //폴더경로
        String folderPath = createFolderPath();

        //파일이름
        String fileNm = uploadFile.getName();

        //S3에 저장될 위치 + 저장파일명
        String storeKey = folderPath + "/" + fileNm;

        //s3로 업로드
        String imageUrl = putS3(uploadFile, storeKey);

        //File 로 전환되면서 로컬에 생성된 파일을 제거
        removeNewFile(uploadFile);

        return imageUrl;
    }



    /**
     * S3파일 삭제
     */
    public void delete(Long imageId) {
        try{
            Image image = imageRepository.findById(imageId).orElseThrow(() ->
                    new CustomException("존재하지 않는 이미지입니다."));

            String imageUrl = image.getImageUrl();
            String storeKey = imageUrl.replace("https://"+bucket+".s3.ap-northeast-2.amazonaws.com/", "");

            System.out.println("imageUrl: " + imageUrl);
            System.out.println("storeKey: "+ storeKey);
      
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, storeKey));

        }catch(Exception e) {
            log.error("delete file error"+e.getMessage());
        }
    }

    // S3로 업로드
    private String putS3(File uploadFile, String storeKey) {
        amazonS3.putObject(new PutObjectRequest(bucket, storeKey, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket,storeKey).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile  multipartFile) throws IOException {

        //파일 이름
        String originalFilename = multipartFile.getOriginalFilename();

        //파일 저장 이름
        String storeFileName = createStoreFileName(originalFilename);

        File convertFile = new File(System.getProperty("user.dir") + "/" + storeFileName);

        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private String createFolderPath() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    private String createStoreFileName(String originalFileName) {

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + originalFileName;
    }


}
