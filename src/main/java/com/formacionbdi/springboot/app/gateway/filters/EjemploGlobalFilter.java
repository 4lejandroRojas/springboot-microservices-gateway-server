package com.formacionbdi.springboot.app.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class EjemploGlobalFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("Ejecutando filtro pre");
		exchange.getRequest().mutate().headers(header -> header.add("token", "12345"));

		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			log.info("Ejecutando filtro post");
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor ->
				exchange.getResponse().getHeaders().add("token", valor)
			);
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			//exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
