package com.particle41.springbootstarter.presentation.pagination.mapper

import com.particle41.springbootstarter.domain.pagination.Page
import com.particle41.springbootstarter.domain.pagination.Pageable
import com.particle41.springbootstarter.presentation.pagination.dto.PageRequest
import com.particle41.springbootstarter.presentation.pagination.dto.PageResponse

object PageableMapper {

    fun toPageable(pageRequest: PageRequest): Pageable {
        return Pageable(
            page = pageRequest.page,
            size = pageRequest.size
        )
    }

    fun <T, R> toResponse(page: Page<T>, contentMapper: (T) -> R): PageResponse<R> {
        return PageResponse(
            content = page.content.map(contentMapper),
            pageNumber = page.pageNumber,
            pageSize = page.pageSize,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            isFirst = page.isFirst,
            isLast = page.isLast,
            hasNext = page.hasNext,
            hasPrevious = page.hasPrevious
        )
    }
}
