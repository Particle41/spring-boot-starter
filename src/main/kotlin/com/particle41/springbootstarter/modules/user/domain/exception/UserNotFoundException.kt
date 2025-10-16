package com.particle41.springbootstarter.modules.user.domain.exception

class UserNotFoundException(id: Long) :
    RuntimeException("User with id=$id not found")
