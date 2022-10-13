package ru.practicum.shareit;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.ValidationException;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

public class OffsetLimitPageable implements Pageable {
    private final int offset;
    private final int limit;
    private final Sort sort;

    protected OffsetLimitPageable(int offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    public static Pageable of(Integer from, Integer size) {
        if (from == null && size == null) {
            from = 0;
            size = DEFAULT_PAGE_SIZE;
        }
        validateOrThrowException(from, size);
        return new OffsetLimitPageable(saveUnboxing(from), saveUnboxing(size), Sort.unsorted());
    }

    public static Pageable of(Integer from, Integer size, Sort sort) {
        if (from == null && size == null) {
            from = 0;
            size = DEFAULT_PAGE_SIZE;
        }
        validateOrThrowException(from, size);
        return new OffsetLimitPageable(saveUnboxing(from), saveUnboxing(size), sort);
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetLimitPageable(offset + limit, limit, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return new OffsetLimitPageable(offset, limit, sort);
    }

    @Override
    public Pageable first() {
        return new OffsetLimitPageable(offset, limit, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetLimitPageable(offset + limit * pageNumber, limit, sort);
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    private static int saveUnboxing(Integer value) {
        return Optional.ofNullable(value).orElse(0);
    }

    private static void validateOrThrowException(Integer from, Integer size) {
        if (saveUnboxing(size) < 1 || saveUnboxing(from) < 0) {
            throw new ValidationException("from must be positive and size must be more then 0");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffsetLimitPageable that = (OffsetLimitPageable) o;
        return offset == that.offset && limit == that.limit && Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, limit, sort);
    }
}
