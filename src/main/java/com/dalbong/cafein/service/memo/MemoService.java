package com.dalbong.cafein.service.memo;

import com.dalbong.cafein.domain.memo.Memo;
import com.dalbong.cafein.dto.admin.memo.AdminMemoRegDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoResDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoUpdateDto;

import java.util.List;

public interface MemoService {

    Memo register(AdminMemoRegDto adminMemoRegDto, Long principalId);

    void modify(AdminMemoUpdateDto adminMemoUpdateDto);

    void remove(Long memoId);

    List<AdminMemoResDto> getCustomLimitMemoList(int limit);

}
