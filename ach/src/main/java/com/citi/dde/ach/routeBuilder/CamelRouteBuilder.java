package com.citi.dde.ach.routeBuilder;

import javax.annotation.PostConstruct;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelRouteBuilder extends RouteBuilder {
	
	@PostConstruct
	void init(){
		System.out.println("CamelRouteBuilder");
	}
	
	@Override
	public void configure() throws Exception {
	
	}

}
