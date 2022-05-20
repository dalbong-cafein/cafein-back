package com.dalbong.cafein.domain.sticker;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@ToString(exclude = {"store"})
@DiscriminatorValue("store")
@Entity
public class StoreSticker extends Sticker{




}
