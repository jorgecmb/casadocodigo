package org.casadocodigo.loja.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice //Observar os outros Controllers
public class ExceptionHandlerController {

	@ExceptionHandler(Exception.class)
	public ModelAndView trataExceptionGenerica(Exception exception) {
		System.out.println("Erro genérico");
		exception.printStackTrace();
		
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("exception", exception);

		return modelAndView;
	}
}
