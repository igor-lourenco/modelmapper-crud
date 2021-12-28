package com.modelmapper.crud.repositories;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.modelmapper.crud.entities.Usuario;
import com.modelmapper.crud.repository.UsuarioRepository;
import com.modelmapper.crud.tests.Factory;

@DataJpaTest
public class UsuarioRepositoryTests {

	@Autowired
	private UsuarioRepository repository;
	private long idExistente;
	private long idNaoExistente;
	private long totalUsuarios;

	@BeforeEach
	private void setUp() throws Exception {
		idExistente = 1L;
		idNaoExistente = 1000L;
		totalUsuarios = 10L;
	}

	@Test
	public void findByIdDeveriaRetornarOptionalEmptyQuandoIdNaoExistir() {
		Optional<Usuario> usuario = repository.findById(idNaoExistente);

		Assertions.assertTrue(usuario.isEmpty());
	}

	@Test
	public void findByIdDeveriaRetornarObjetoQuandoIdExistir() {
		Optional<Usuario> usuario = repository.findById(idExistente);

		Assertions.assertTrue(usuario.isPresent());
		Assertions.assertEquals(usuario.get().getNome(), "Alex");
	}

	@Test
	public void updateDeveriaAtualizarObjeto() {
		Usuario usuario = repository.getById(idExistente);

		usuario.setNome("AlexTeste");
		usuario = repository.save(usuario);

		Assertions.assertEquals(usuario.getNome(), "AlexTeste");
	}

	@Test
	public void updateDeveriaLancarEntityNotFoundExceptionQuandoIdNaoexistir() {
		Usuario usuario = repository.getById(idNaoExistente);

		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			usuario.setNome("AlexTeste");
			repository.save(usuario);
		});
	}

	@Test
	public void insertDeveriaAutoIncrementarObjetoQuandoIdForNulo() {
		Usuario usuario = Factory.CriaUsuario();

		usuario = repository.save(usuario);

		Assertions.assertEquals(usuario.getId(), totalUsuarios + 1);
		Assertions.assertNotNull(usuario.getId());
	}

	@Test
	public void deleteDeveriaLançarEmptyResultDataAccessExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(idNaoExistente);
		});
	}

	@Test
	public void deleteDeveriaDeletarObjetoQuandoIdExistir() {
		repository.deleteById(idExistente);

		Optional<Usuario> resultado = repository.findById(idExistente);
		Assertions.assertFalse(resultado.isPresent());
	}
}
