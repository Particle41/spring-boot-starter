package com.particle41.springbootstarter.feature.user.domain.exception

class UserNotFoundException(id: Long) :
    RuntimeException("User with id=$id not found")
