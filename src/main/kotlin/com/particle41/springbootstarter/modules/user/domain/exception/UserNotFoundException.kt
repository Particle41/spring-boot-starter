package com.particle41.springbootstarter.modules.user.domain.exception

import com.particle41.springbootstarter.common.exceptions.DomainNotFoundException

class UserNotFoundException(id: String) : DomainNotFoundException("User", id)
