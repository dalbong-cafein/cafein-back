package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"event"})
@DiscriminatorValue("event")
@Entity
public class EventImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id" , nullable = false)
    private Event event;

    protected EventImage(){};

    public EventImage(Event event, String imageUrl){
        super(imageUrl);
        this.event = event;
    }
}
