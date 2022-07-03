package com.dalbong.cafein.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ImageRepository;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
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
     * 이벤트 이미지 S3 업로드
     */

    /**
     * 프로필 이미지 S3 업로드
     */
    public String s3UploadOfProfileImage(Member member, MultipartFile multipartFile) throws IOException {

        //폴더 경로
        String folderPath = "member";

        //파일 이름
        String frontName = member.getMemberId().toString();
        String storeFileName = createFileName(frontName, multipartFile.getOriginalFilename());

        return s3Upload(folderPath, storeFileName, multipartFile);
    }

    /**
     * 가게 이미지 S3 다중업로드
     */
    public List<String> s3MultipleUploadOfStore(Store store, List<MultipartFile> multipartFiles) throws IOException {

        List<String> imageUrlList = new ArrayList<>();

        if (!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()){
            for (MultipartFile multipartFile : multipartFiles){
                imageUrlList.add(s3UploadOfStore(store, multipartFile));
            }
        }

        return imageUrlList;
    }

    /**
     * 가게 이미지 S3 업로드
     */
    public String s3UploadOfStore(Store store, MultipartFile multipartFile) throws IOException {

        String folderPath = null;
        //폴더 경로
        if(store.getAddress().getSggNm().equals("노원구")){
            folderPath = "store/nowon";
        }else if(store.getAddress().getSggNm().equals("강남구")){
            folderPath = "store/gangnam";
        }else if(store.getAddress().getSggNm().equals("서대문구")){
            folderPath = "store/seodaemoon";
        }else if(store.getAddress().getSggNm().equals("마포구")){
            folderPath = "store/mapo";
        }else if(store.getAddress().getSggNm().equals("동대문구")){
            folderPath = "store/dongdaemoon";
        }else if(store.getAddress().getSggNm().equals("종로구")){
            folderPath = "store/jongro";
        }

        //파일 이름
        String frontName = store.getStoreId().toString();
        String storeFileName = createFileName(frontName, multipartFile.getOriginalFilename());

        return s3Upload(folderPath, storeFileName, multipartFile);
    }

    /**
     * 리뷰 이미지 S3 다중업로드
     */
    public List<String> s3MultipleUploadOfReview(Review review, List<MultipartFile> multipartFiles) throws IOException {

        List<String> imageUrlList = new ArrayList<>();

        if (!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()){
            for (MultipartFile multipartFile : multipartFiles){
                imageUrlList.add(s3UploadOfReview(review, multipartFile));
            }
        }

        return imageUrlList;
    }

    /**
     * 리뷰 이미지 S3 업로드
     */
    public String s3UploadOfReview(Review review, MultipartFile multipartFile) throws IOException {

        //폴더 경로
        String folderPath = createFolderPathOfReview();

        //파일 이름
        String frontName = review.getMember().getMemberId().toString() + review.getStore().getStoreId().toString();
        String reviewFileName = createFileName(frontName, multipartFile.getOriginalFilename());

        return s3Upload(folderPath, reviewFileName, multipartFile);
    }

    /**
     * 게시글 이미지 S3 다중업로드
     */
    public List<String> s3MultipleUploadOfBoard(Board board, List<MultipartFile> multipartFiles) throws IOException {

        List<String> imageUrlList = new ArrayList<>();

        if (!multipartFiles.isEmpty() && !multipartFiles.get(0).isEmpty()){
            for (MultipartFile multipartFile : multipartFiles){
                imageUrlList.add(s3UploadOfBoard(board, multipartFile));
            }
        }

        return imageUrlList;
    }

    /**
     * 게시글 이미지 S3 업로드
     */
    public String s3UploadOfBoard(Board board, MultipartFile multipartFile) throws IOException {


        //폴더 경로
        String folderPath = createFolderPathOfBoard();

        //파일 이름
        String frontName = board.getBoardId().toString();
        String boardFileName = createFileName(frontName, multipartFile.getOriginalFilename());

        return s3Upload(folderPath, boardFileName, multipartFile);
    }

    /**
     * 이벤트 이미지 S3 업로드
     */
    public String s3UploadOfEvent(MultipartFile multipartFile) throws IOException {

        //폴더 경로
        String folderPath = "event";

        //파일 이름
        String storeFileName = createFileName("", multipartFile.getOriginalFilename());

        return s3Upload(folderPath, storeFileName, multipartFile);
    }


    /**
     * S3 업로드
     */
    private String s3Upload(String folderPath, String fileNm, MultipartFile multipartFile) throws IOException {

        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

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

    //TODO data 세팅 후 public -> private 변경
    // S3로 업로드
    public String putS3(File uploadFile, String storeKey) {
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
        String storeFileName = UUID.randomUUID().toString()+"_"+originalFilename;

        File convertFile = new File(System.getProperty("user.dir") + "/" + storeFileName);

        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private String createFolderPathOfReview() {
        return "review/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    private String createFolderPathOfBoard() {
        return "board/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    private String createFileName(String frontName, String originalFileName) {

        String uuid = UUID.randomUUID().toString();

        return frontName + "_" + uuid + "_" + originalFileName;
    }


}
