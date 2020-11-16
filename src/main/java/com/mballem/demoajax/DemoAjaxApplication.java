package com.mballem.demoajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mballem.demoajax.service.SocialMetaTagService;

@SpringBootApplication
public class DemoAjaxApplication { // implements CommandLineRunner

	public static void main(String[] args) {
		SpringApplication.run(DemoAjaxApplication.class, args);
	}

	@Autowired
	SocialMetaTagService service;
	
	/*@Override
	public void run(String... args) throws Exception {
		
		SocialMetaTag tag = service.getSocialMetaTagByUrl("https://www.udemy.com/spring-boot-mvc-com-thymeleaf/"); // https://www.pichau.com.br/destaques/zotac-geforcer-gtx-1060-amp-edition-zt-p10600b-10m
		if(tag != null)
			System.out.println(tag.toString());
	}*/
}
