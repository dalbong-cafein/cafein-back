package com.dalbong.cafein.service.memo;

import com.dalbong.cafein.domain.memo.Memo;
import com.dalbong.cafein.dto.memo.MemoRegDto;
import com.dalbong.cafein.dto.memo.MemoUpdateDto;

public interface MemoService {

    Memo register(MemoRegDto memoRegDto, Long principalId);

    void modify(MemoUpdateDto memoUpdateDto);

    void remove(Long memoId);

}
