package br.com.dlsistemas.servicesgeo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/error")
	public String erro404(){
		return "error/404";
	}

}
