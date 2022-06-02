package com.dalbong.cafein.domain.memo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo,Long>,  MemoRepositoryQuerydsl{

}
