package com.dalbong.cafein.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class DistanceUtilTest {

    @Test
    void 거리계산() throws Exception{
        //given
        //동대문역 37.571687, 127.01093
        //종로 5가 37.570926, 127.001849
        //종로 3가 37.570406, 126.991847
        //when
        double distance = DistanceUtil.calculateDistance(37.571687, 127.01093, 37.570406, 126.991847, "meter");
    }


}