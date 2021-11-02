package com.zoomout.myservice.core.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class CurrencyConverter {

    private static final NumberFormat BOX_OFFICE_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    public static Mono<Long> convertToNumber(String value) {
        try {
            return Mono.just(BOX_OFFICE_FORMAT.parse(value).longValue());
        } catch (ParseException e) {
            return Mono.error(e);
        }
    }

    public static String convertFromNumber(Long value) {
        return BOX_OFFICE_FORMAT.format(value);
    }

}
