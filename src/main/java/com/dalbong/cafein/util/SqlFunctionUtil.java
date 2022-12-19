package com.dalbong.cafein.util;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import org.springframework.stereotype.Component;

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

}
