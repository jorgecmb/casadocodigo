package org.casadocodigo.loja.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.casadocodigo.loja.models.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO implements UserDetailsService {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Usuario loadUserByUsername(String email) throws UsernameNotFoundException {
		List<Usuario> usuarios = manager.createQuery("select u from Usuario u where u.email = :email",
				Usuario.class)
			.setParameter("email", email)
			.getResultList();
		
		if (usuarios.isEmpty()) {
			throw new UsernameNotFoundException("Usuário " + email + " não foi encontrado!");
		}
		
		return usuarios.get(0);
	}
}
