package com.BTL_JAVA.BTL.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResult<T> {
     List<T> items;
     int page;        // trang hiện tại (0-based)
     int size;        // kích cỡ trang
     long total;      // tổng bản ghi
     int totalPages;  // tổng số trang
}

