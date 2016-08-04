package org.casadocodigo.loja.daos;

import java.math.BigDecimal;
import java.util.List;

import org.casadocodigo.loja.builder.ProdutoBuilder;
import org.casadocodigo.loja.conf.JPAConfiguration;
import org.casadocodigo.loja.models.Produto;
import org.casadocodigo.loja.models.TipoPreco;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class) //Spring vai cuidar do contexto
@ContextConfiguration(classes={JPAConfiguration.class, ProdutoDAO.class})
public class ProdutoDAOTest {

	@Autowired
	private ProdutoDAO produtoDAO;
	
	@Test
	@Transactional
	public void deveSomarTodosPrecosPorTipoPreco() {
		List<Produto> livrosImpressos = ProdutoBuilder
					.newProduto(TipoPreco.IMPRESSO, BigDecimal.TEN)
					.more(3).buildAll();
		
		List<Produto> livrosEbook = ProdutoBuilder
				.newProduto(TipoPreco.EBOOK, BigDecimal.TEN)
				.more(3).buildAll();
		
		livrosImpressos.stream().forEach(produtoDAO::gravar);
		livrosEbook.stream().forEach(produtoDAO::gravar);
		
		BigDecimal valor = produtoDAO.somaPrecoPorTipo(TipoPreco.EBOOK);
		Assert.assertEquals(new BigDecimal(40).setScale(2), valor);
	}
}
