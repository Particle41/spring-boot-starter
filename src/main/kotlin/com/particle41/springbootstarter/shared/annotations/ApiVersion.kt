package com.particle41.springbootstarter.shared.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ApiVersion(val value: Int)
