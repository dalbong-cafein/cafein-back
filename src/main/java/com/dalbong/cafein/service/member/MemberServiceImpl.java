package com.dalbong.cafein.service.member;

import com.dalbong.cafein.domain.coupon.CouponRepository;
import com.dalbong.cafein.domain.heart.HeartRepository;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.loginHistory.LoginHistory;
import com.dalbong.cafein.domain.loginHistory.LoginHistoryRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.notice.NoticeRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.sticker.StickerRepository;
import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberListResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberUpdateDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.login.AccountUniteRegDto;
import com.dalbong.cafein.dto.member.AgreeRegDto;
import com.dalbong.cafein.dto.member.MemberInfoDto;
import com.dalbong.cafein.dto.member.MemberUpdateDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.oAuth.apple.AppleTokenService;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.dalbong.cafein.domain.member.AuthProvider.KAKAO;
import static com.dalbong.cafein.domain.member.AuthProvider.NAVER;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final StickerRepository stickerRepository;
    private final CouponRepository couponRepository;
    private final HeartRepository heartRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final NoticeRepository noticeRepository;
    private final AppleTokenService appleTokenService;

    /**
     * 로그인 기록 저장
     */
    @Transactional
    @Override
    public void saveLoginHistory(Member member, AuthProvider authProvider, String clientIp) {

        LoginHistory loginHistory = LoginHistory.builder()
                .member(member)
                .authProvider(authProvider)
                .ip(clientIp)
                .build();

        //TODO MEMBER DB 삭제 시 - loginHistory 처리
        loginHistoryRepository.save(loginHistory);
    }

    /**
     * 계정 통합
     */
    @Transactional
    @Override
    public Long uniteAccount(String email, AccountUniteRegDto accountUniteRegDto) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new CustomException("해당 email은 등록된 계정이 없습니다."));

        AuthProvider newAuthProvider = accountUniteRegDto.getNewAuthProvider();

        switch (newAuthProvider) {
            case KAKAO:
                member.setKakaoId(accountUniteRegDto.getNewOAuthId());
                break;

            case NAVER:
                member.setNaverId(accountUniteRegDto.getNewOAuthId());
                break;

            default:
                throw new CustomException("Unexpected value: " + newAuthProvider);
        }

        return member.getMemberId();
    }

    /**
     * 닉네임 중복체크
     */
    @Transactional(readOnly = true)
    @Override
    public Boolean isDuplicateNickname(String nickname) {

        return !memberRepository.existNickname(nickname);
    }


    /**
     * 약관 동의 데이터 저장
     */
    @Transactional
    @Override
    public void saveAgreeTerms(AgreeRegDto agreeRegDto, Long principalId) {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        member.changeIsAgreeLocation(agreeRegDto.getIsAgreeLocation());
        member.changeIsAgreeMarketingPush(agreeRegDto.getIsAgreeMarketingPush());
    }

    /**
     * 휴대폰 번호 변경
     */
    @Transactional
    @Override
    public void modifyPhone(String phone, Long principalId) {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하는 회원이 아닙니다."));

        member.changePhone(phone);
    }

    /**
     * 프로필 사진, 닉네임 변경
     */
    @Transactional
    @Override
    public ImageDto modifyImageAndNickname(MemberUpdateDto memberUpdateDto, Long principalId) throws IOException {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하는 회원이 아닙니다."));

        member.changeNickname(memberUpdateDto.getNickname());

        ImageDto memberImageDto = null;

        //프로필 이미지 갱신
        if (memberUpdateDto.getImageFile() != null && !memberUpdateDto.getImageFile().isEmpty()){

            //기존 프로필 이미지 삭제
            if(memberUpdateDto.getDeleteImageId() != null){
                imageService.remove(memberUpdateDto.getDeleteImageId());
            }
            
            //새로운 이미지로 갱신
            Image image = imageService.saveMemberImage(member, memberUpdateDto.getImageFile());

            memberImageDto = new ImageDto(image.getImageId(), image.getImageUrl());
        }
        //기본 이미지로 변경
        else if(memberUpdateDto.getDeleteImageId() != null){
            //기존 프로필 이미지 삭제
            imageService.remove(memberUpdateDto.getDeleteImageId());
        }

        return memberImageDto;
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    @Override
    public void leave(Long memberId, String code) throws IOException {

        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        //회원 탈퇴
        member.leave();

        //회원의 notice, sticker, coupon, heart 데이터 삭제
        noticeRepository.deleteByMemberId(member.getMemberId());
        stickerRepository.deleteByMember(member);
        couponRepository.deleteByMember(member);
        heartRepository.deleteByMember(member);

        //애플 계정 회원일 경우
        if(member.getAppleId() != null && !member.getAppleId().isEmpty()){
            appleTokenService.generateAuthToken(code);
        }

    }

    /**
     * 회원 프로필 정보 조회
     */
    @Transactional
    @Override
    public MemberInfoDto getMemberInfo(Long memberId) {
        List<Object[]> result = memberRepository.getMemberInfo(memberId);
        Object[] arr = result.get(0);

        MemberImage memberImage = (MemberImage) arr[1];

        ImageDto imageDto = null;
        if (memberImage != null){
            Long imageId = memberImage.getImageId();
            String imageUrl = memberImage.getImageUrl();
            imageDto = new ImageDto(imageId, imageUrl);
        }

        return new MemberInfoDto((Member) arr[0], imageDto);
    }

    /**
     * 관리지단 회원 정보 수정 (생년월일, 성별, 휴대폰 번호)
     */
    @Transactional
    @Override
    public void modifyOfAdmin(AdminMemberUpdateDto adminMemberUpdateDto) {

        Member member = memberRepository.findById(adminMemberUpdateDto.getMemberId()).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        member.changeBirth(adminMemberUpdateDto.getBirth());
        member.changeGender(adminMemberUpdateDto.getGenderType());
        member.changePhone(adminMemberUpdateDto.getPhone());
    }

    /**
     * 관리자단 회원 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminMemberListResDto getMemberListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("memberId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("memberId").descending());
        }

        Page<Object[]> results = memberRepository.getAllMemberListOfAdmin(
                pageRequestDto.getSearchType(), pageRequestDto.getKeyword(), pageable);


        Function<Object[], AdminMemberResDto> fn = (arr -> {

            Member member = (Member) arr[0];

            //회원 프로필 이미지
            MemberImage memberImage = (MemberImage) arr[1];

            ImageDto imageDto = null;
            if(memberImage != null){
                imageDto = new ImageDto(memberImage.getImageId(), memberImage.getImageUrl());
            }

            return new AdminMemberResDto(member, imageDto, (Long) arr[2]);
        });

        return new AdminMemberListResDto(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 관리자단 회원 상세 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminDetailMemberResDto getDetailMemberOfAdmin(Long memberId) {

        Object[] arr = memberRepository.getDetailMemberOfAdmin(memberId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        //회원 프로필 이미지
        MemberImage memberImage = (MemberImage) arr[1];

        ImageDto memberImageDto = null;
        if(memberImage != null){
            memberImageDto = new ImageDto(memberImage.getImageId(), memberImage.getImageUrl());
        }

        long heartCnt = (long) arr[2];
        long congestionCnt = (long) arr[3];
        long reviewCnt = (long) arr[4];
        long stickerCnt = (long) arr[5];

        return new AdminDetailMemberResDto((Member)arr[0], memberImageDto,
                heartCnt, congestionCnt, reviewCnt, stickerCnt);
    }

    /**
     * 오늘 등록된 회원 수 조회
     */
    @Transactional(readOnly = true)
    @Override
    public Long getRegisterCountOfToday() {
        return (Long) memberRepository.getRegisterCountOfToday();
    }
}
