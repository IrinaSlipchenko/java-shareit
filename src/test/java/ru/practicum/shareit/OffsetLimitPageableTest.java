package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import static org.junit.jupiter.api.Assertions.*;

class OffsetLimitPageableTest {
    public static final int FROM = 1;
    public static final int SIZE = 2;

    Pageable pageable = OffsetLimitPageable.of(FROM, SIZE);

    @Test
    void getPageNumber() {
        assertEquals(0, pageable.getPageNumber());
    }

    @Test
    void getPageSize() {
        assertEquals(SIZE, pageable.getPageSize());
    }

    @Test
    void getOffset() {
        assertEquals(FROM, pageable.getOffset());
    }

    @Test
    void getSort() {
        assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void next() {
        assertEquals(OffsetLimitPageable.of(FROM + SIZE, SIZE, Sort.unsorted()), pageable.next());
    }

    @Test
    void previousOrFirst() {
        assertEquals(OffsetLimitPageable.of(FROM, SIZE, Sort.unsorted()), pageable.previousOrFirst());
    }

    @Test
    void first() {
        assertEquals(OffsetLimitPageable.of(FROM, SIZE, Sort.unsorted()), pageable.first());
    }

    @Test
    void withPage() {
        int pageNumber = 1;
        assertEquals(OffsetLimitPageable.of(FROM + SIZE * pageNumber, SIZE, Sort.unsorted()),
                pageable.withPage(pageNumber));
    }

    @Test
    void hasPrevious() {
        assertFalse(pageable.hasPrevious());
    }
}