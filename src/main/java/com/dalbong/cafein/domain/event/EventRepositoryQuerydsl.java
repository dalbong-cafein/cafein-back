package com.dalbong.cafein.domain.event;

import com.dalbong.cafein.domain.image.EventImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EventRepositoryQuerydsl {

    Page<Object[]> getEventList(Pageable pageable);

    Optional<Object[]> latestEvent();

}
