package com.dalbong.cafein.util;

import com.dalbong.cafein.handler.exception.CustomException;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import org.springframework.stereotype.Component;

import static com.dalbong.cafein.domain.store.QStore.store;

@Component
public class SqlFunctionUtil {

    public static NumberTemplate<Double> acos(Object num) {
        return Expressions.numberTemplate(Double.class, "function('acos',{0})", num);
    }

    public static NumberTemplate<Double> sin(Object num) {
        return Expressions.numberTemplate(Double.class, "function('sin',{0})", num);
    }

    public static NumberTemplate<Double> cos(Object num) {
        return Expressions.numberTemplate(Double.class, "function('cos',{0})", num);
    }

    public static NumberTemplate<Double> radians(Object num) {
        return Expressions.numberTemplate(Double.class, "function('radians',{0})", num);
    }

    public static NumberExpression<Double> calculateDistance(Object lat1, Object lng1, Object lat2, Object lng2){

        return acos(cos(radians(lat1))
                .multiply(cos(radians(lat2)))
                .multiply(cos(radians(lng1).subtract(radians(lng2))))
                .add(sin(radians(lat1)).multiply(sin(radians(lat2)))))
                .multiply(6371);
    }
    public static NumberExpression<Double> calculateDistance(String coordinate) {

            try {
                String[] coordinateArr = coordinate.split(",");

                double latY = Double.parseDouble(coordinateArr[0]);
                double lngX = Double.parseDouble(coordinateArr[1]);

                return SqlFunctionUtil.calculateDistance(store.latY, store.lngX, latY, lngX);
            }catch (IndexOutOfBoundsException e) {
                throw new CustomException("잘못된 좌표 형식입니다.");
            }
    }
}


