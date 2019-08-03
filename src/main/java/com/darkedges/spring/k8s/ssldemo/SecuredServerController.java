package com.darkedges.spring.k8s.ssldemo;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SecuredServerController {
	@RequestMapping("/secured")
	public String secured() {
		System.out.println("Inside secured()");
		return "Hello user !!! : " + new Date();
	}
}