package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoRegDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoResDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoUpdateDto;
import com.dalbong.cafein.service.memo.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class MemoAdminController {

    private final MemoService memoService;

    /**
     * 관리자단 메모 생성
     */
    @PostMapping("/memos")
    public ResponseEntity<?> registerMemo(@RequestBody AdminMemoRegDto adminMemoRegDto, @AuthenticationPrincipal PrincipalDetails principalDetails){

        //memoService.register(adminMemoRegDto, principalDetails.getMember().getMemberId());
        memoService.register(adminMemoRegDto, 1L);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 생성 성공", null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 메모 수정
     */
    @PutMapping("/memos/{memoId}")
    public ResponseEntity<?> modifyMemo(@Validated @RequestBody AdminMemoUpdateDto adminMemoUpdateDto, BindingResult bindingResult){

        memoService.modify(adminMemoUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 수정 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 메모 삭제
     */
    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<?> removeMemo(@PathVariable("memoId") Long memoId){

        memoService.remove(memoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 삭제 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 메모 조회
     */
    @GetMapping("/memos/{memoId}")
    public ResponseEntity<?> getMemo(@PathVariable("memoId") Long memoId){

        AdminMemoResDto adminMemoResDto = memoService.getMemo(memoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 조회 성공", adminMemoResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 최근 생성 or 수정된 메모 리스트 개수 지정 조회
     */
    @GetMapping("/memos/recent")
    public ResponseEntity<?> getRecentMemoList(@RequestParam(value = "limit", defaultValue = "6", required = false) int limit){

        List<AdminMemoResDto> adminMemoResDtoList = memoService.getCustomLimitMemoList(limit);

        return new ResponseEntity<>(
                new CMRespDto<>(1, "관리자단 최근 생성 or 수정된 메모 리스트 개수 지정 조회 성공", adminMemoResDtoList), HttpStatus.OK);

    }
}
