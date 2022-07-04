package com.dalbong.cafein.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ImageRepositoryQuerydsl {

    Page<EventImage> getEventImageList(Pageable pageable);

}
