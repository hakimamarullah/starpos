package com.starline.utils;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 11/6/2024 12:55 PM
@Last Modified 11/6/2024 12:55 PM
Version 1.0
*/

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.Sort;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CommonUtil {

    private CommonUtil() {

    }

    public static String formatRupiah(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(number);
    }

    public static Optional<Sort> toJpaSort(List<QuerySortOrder> querySortOrders) {
        Function<SortDirection, Sort.Direction> getDirection = vaadinDirection -> "ASCENDING".equalsIgnoreCase(vaadinDirection.name()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (querySortOrders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                Sort.by(querySortOrders.stream().map(sort -> new Sort.Order(getDirection.apply(sort.getDirection()), sort.getSorted())).toList())
        );
    }

}
