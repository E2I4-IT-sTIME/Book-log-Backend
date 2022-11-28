package com.dormammu.BooklogWeb.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.portfolio.PortfolioRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(int meetingId, MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(meetingId, uploadFile, dirName);
    }

    // s3로 파일 업로드하기 - meeting
    private String upload(int meetingId, File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        Meeting meeting = meetingRepository.findById(meetingId);
        meeting.setImage(uploadImageUrl);
        removeNewFile(uploadFile);

        return "모임 생성 및 사진 저장 성공";
    }

    // s3로 파일 업로드하기 - portfolio
    /* --- */
    public String uploadPortfolio(int portfolio_id, MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return uploadPortfolio(portfolio_id, uploadFile, dirName);
    }

    private String uploadPortfolio(int portfolio_id, File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        Portfolio portfolio = portfolioRepository.findById(portfolio_id);
        portfolio.setImage(uploadImageUrl);
        removeNewFile(uploadFile);

        return "포트폴리오 생성 및 사진 저장 성공";
    }
    /* --- */

    public String uploadProfile(int userId, MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        return uploadProfile(userId, uploadFile, dirName);
    }

    // s3로 파일 업로드하기
    private String uploadProfile(int userId, File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드

        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    // 유저 이미지 업데이트(삭제 -> 수정)
    public String deleteProfileFile(int userId, MultipartFile multipartFile, String dirName) throws IOException {
        User user = userRepository.findById(userId);
        System.out.println(user.getImgPath());
        System.out.println("deleteProfileFile 들어옴");
        String ImageUrl = user.getImgPath();
        System.out.println(ImageUrl);
//        DeleteObjectRequest request = new DeleteObjectRequest(bucket, ImageUrl);  // 삭제
        amazonS3Client.deleteObject(bucket, ImageUrl);
        System.out.println("삭제 완료");
//        amazonS3Client.deleteObject(request);
        return updateProfile(userId, multipartFile, dirName);

    }

    // 유저 이미지 업데이트(삭제 -> 수정)
    public String updateProfile(int userId, MultipartFile multipartFile, String dirName) throws IOException{

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        User user = userRepository.findById(userId);
        user.setImgPath(uploadImageUrl);
        removeNewFile(uploadFile);

        return "사진 업데이트 완료";
    }

    // 업로드하기
    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());

        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
