package com.dalbong.cafein.service.memo;

import com.dalbong.cafein.domain.memo.Memo;
import com.dalbong.cafein.dto.memo.MemoRegDto;

public interface MemoService {

    Memo register(MemoRegDto memoRegDto, Long principalId);
    ;

}
