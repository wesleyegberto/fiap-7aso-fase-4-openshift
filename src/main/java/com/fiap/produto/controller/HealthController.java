package com.fiap.produto.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
	@RequestMapping("/check")
	public String check() {
		return "OK";
	}
}