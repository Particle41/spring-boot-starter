package com.particle41.springbootstarter.infrastructure.pagination.mapper

import com.particle41.springbootstarter.domain.pagination.Direction
import com.particle41.springbootstarter.domain.pagination.Page
import com.particle41.springbootstarter.domain.pagination.Pageable as DomainPageable
import org.springframework.data.domain.PageRequest as SpringPageRequest
import org.springframework.data.domain.Pageable as SpringPageable
import org.springframework.data.domain.Sort as SpringSort

object PageableMapper {

    fun toSpringPageable(pageable: DomainPageable): SpringPageable {
        val sort = pageable.sort?.let {
            val direction = if (it.direction == Direction.ASC) {
                SpringSort.Direction.ASC
            } else {
                SpringSort.Direction.DESC
            }
            SpringSort.by(direction, it.property)
        } ?: SpringSort.unsorted()

        return SpringPageRequest.of(
            pageable.page,
            pageable.size,
            sort
        )
    }

    fun <T, R> toDomainPage(
        springPage: org.springframework.data.domain.Page<T>,
        contentMapper: (T) -> R
    ): Page<R> {
        return Page(
            content = springPage.content.map(contentMapper),
            pageNumber = springPage.number,
            pageSize = springPage.size,
            totalElements = springPage.totalElements,
            totalPages = springPage.totalPages,
            isFirst = springPage.isFirst,
            isLast = springPage.isLast,
            hasNext = springPage.hasNext(),
            hasPrevious = springPage.hasPrevious()
        )
    }
}
