package com.particle41.springbootstarter.shared.config

import com.particle41.springbootstarter.shared.annotations.ApiVersion
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method

@Configuration
class ApiVersionConfig {

    @Bean
    fun apiVersionRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return object : RequestMappingHandlerMapping() {
            override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
                val info = super.getMappingForMethod(method, handlerType) ?: return null
                val apiVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion::class.java)
                    ?: return info
                val versionPath = "/api/v${apiVersion.value}"
                val versionInfo = RequestMappingInfo.paths(versionPath).build()
                return versionInfo.combine(info)
            }
        }.apply {
            order = 0
        }
    }
}
