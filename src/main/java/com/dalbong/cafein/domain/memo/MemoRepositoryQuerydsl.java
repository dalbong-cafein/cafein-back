package com.dalbong.cafein.domain.memo;

import java.util.List;

public interface MemoRepositoryQuerydsl {

    List<Memo> getCustomLimitMemoList(int limit);

}
