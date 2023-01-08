package com.formacionbdi.springboot.app.gateway.filters.factory;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {

    public EjemploGatewayFilterFactory() {
        super(Configuracion.class);
    }

    @Override
    public GatewayFilter apply(Configuracion config) {
        return (exchange, chain) -> {

            log.info("Ejecutando pre gateway filter factory: "+config.mensaje);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Optional.ofNullable(config.cookieValor).ifPresent(cookie ->
                    exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, config.cookieValor).build())
                );
                log.info("Ejecutando post gateway filter factory: "+config.mensaje);
            }));
        };
    }
    
    @Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("mensaje", "cookieNombre", "cookieValor");
	}

    @Override
    public String name() {
        return "EjemploCookie";
    }

    @Getter
    @Setter
    public static class Configuracion {
        private String mensaje;
        private String cookieValor;
        private String cookieNombre;
    }
}
