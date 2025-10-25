package com.particle41.springbootstarter.domain.user.exception

import com.particle41.springbootstarter.shared.exceptions.DomainNotFoundException

class UserNotFoundException(id: String) : DomainNotFoundException("User", id)
