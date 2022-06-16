package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@ToString
@DiscriminatorValue("event")
@Entity
public class EventImage extends Image{

    protected EventImage(){};

    public EventImage(String imageUrl){
        super(imageUrl);
    }
}
