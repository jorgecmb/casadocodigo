package org.casadocodigo.loja.controllers;

import java.util.List;

import javax.persistence.NoResultException;
import javax.validation.Valid;

import org.casadocodigo.loja.daos.ProdutoDAO;
import org.casadocodigo.loja.infra.FileSaver;
import org.casadocodigo.loja.models.Produto;
import org.casadocodigo.loja.models.TipoPreco;
import org.casadocodigo.loja.validation.ProdutoValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutosController {

	@Autowired
	private ProdutoDAO produtoDao;
	
	@Autowired
	private FileSaver fileSaver;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new ProdutoValidation());
	}

	@RequestMapping("/form")
	public ModelAndView form(Produto produto) {
		ModelAndView modelAndView = new ModelAndView("produtos/form");
		modelAndView.addObject("tipos", TipoPreco.values());
		return modelAndView;
	}

	@RequestMapping(method=RequestMethod.POST)
	@CacheEvict(value="produtosHome", allEntries = true)
	public ModelAndView gravar(MultipartFile sumario, @Valid Produto produto, BindingResult result, RedirectAttributes redirectAttributes) {
	
		//Qualquer erro no ProdutoValidation cai aqui
		if (result.hasErrors()) {
			return form(produto);
			//return new ModelAndView("redirect:produtos/form");
		}
		
		String path = fileSaver.write("sumarios", sumario);
		produto.setSumarioPath(path);
		
		produtoDao.gravar(produto);
		
		//Dura apenas 1 request, do POST para gravar para o REDIRECT da listagem e depois some
		redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
		
		return new ModelAndView("redirect:produtos");
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView listar() {
		List<Produto> produtos = produtoDao.listar();
		ModelAndView modelAndView = new ModelAndView("produtos/lista");
		modelAndView.addObject("produtos", produtos);
		return modelAndView;
	}
	
	@RequestMapping("/detalhe/{id}")
    public ModelAndView detalhe(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("produtos/detalhe");
        Produto produto = produtoDao.find(id);
        modelAndView.addObject("produto",produto);
        return modelAndView;
    }
	
}
