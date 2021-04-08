package com.fatec.scel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@GetMapping("/login")
	public ModelAndView autenticacao() {
		     
		return new ModelAndView("paginaLogin");
	}

	@GetMapping("/")
	public ModelAndView home() {
		return new ModelAndView ("paginaMenu");
	}
	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, Authentication authentication) {
		logger.info("Logout para o usuario: " + authentication.getName());
		HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();
        if(session != null) {
            session.invalidate();
        }

        return new ModelAndView("paginaLogin");
	}
		
}
