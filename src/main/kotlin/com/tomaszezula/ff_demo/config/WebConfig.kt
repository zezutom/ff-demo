package com.tomaszezula.ff_demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class WebConfig {

    @Bean
    fun indexRouter() = RouterFunctions
        // Serve all static assets (js/css/etc) from classpath:/static/
        .resources("/**", ClassPathResource("static/"))
        // Fallback: for any GET that asks for HTML, return index.html
        .andRoute(
            RequestPredicates.GET("/**")
                .and(RequestPredicates.accept(MediaType.TEXT_HTML))
        ) {
            ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(BodyInserters.fromResource(ClassPathResource("static/index.html")))
        }

}
