package com.particle41.springbootstarter.domain.pagination

data class Pageable(
    val page: Int,
    val size: Int,
    val sort: Sort? = null
) {
    init {
        require(page >= 0) { "Page index must not be less than zero" }
        require(size > 0) { "Page size must be greater than zero" }
    }
}

data class Sort(
    val property: String,
    val direction: Direction = Direction.ASC
)

enum class Direction {
    ASC, DESC
}
