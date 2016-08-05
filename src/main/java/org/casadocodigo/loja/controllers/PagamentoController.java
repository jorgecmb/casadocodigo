package org.casadocodigo.loja.controllers;

import java.util.concurrent.Callable;

import org.casadocodigo.loja.models.CarrinhoCompras;
import org.casadocodigo.loja.models.DadosPagamento;
import org.casadocodigo.loja.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {
		
	@Autowired
	private CarrinhoCompras carrinho;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MailSender sender; 
	
	@RequestMapping(value="/finalizar", method = RequestMethod.POST)
	public Callable<ModelAndView> finalizar(@AuthenticationPrincipal Usuario usuario,
			RedirectAttributes model) {
		//Classe anônima de Callable, para ser assíncrono
		return () -> {
			//Envio de requisição para o pagamento no servidor
			String uri = "http://book-payment.herokuapp.com/payment";
			
			try {
				String response = restTemplate.postForObject(uri, new DadosPagamento(carrinho.getTotal()), String.class);
				System.out.println(response);
				enviaEmailCompraProduto(usuario);
				model.addFlashAttribute("sucesso", response);
				return new ModelAndView("redirect:/produtos");
				
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				model.addFlashAttribute("falha", "Valor maior que o permitido");
				return new ModelAndView("redirect:/produtos");
			}
		};
	}

	private void enviaEmailCompraProduto(Usuario usuario) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject("Compra finalizada");
//		email.setTo(usuario.getEmail());
		email.setTo("jorgecmb@gmail.com");
		email.setText("Sua compra foi aprovada e finalizada no valor de R$ " + carrinho.getTotal());
		email.setFrom("compras@casadocodigo.com.br");
		
		sender.send(email);
	}
}
