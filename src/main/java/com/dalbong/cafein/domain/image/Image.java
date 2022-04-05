package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false, length = 300)
    private String imageUrl;

    public Image(String imageUrl){
        this.imageUrl = imageUrl;
    }


}
