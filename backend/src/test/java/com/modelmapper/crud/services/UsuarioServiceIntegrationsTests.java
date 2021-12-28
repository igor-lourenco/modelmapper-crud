package com.modelmapper.crud.services;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.modelmapper.crud.entities.Usuario;
import com.modelmapper.crud.entities.dto.UsuarioComSenhaDTO;
import com.modelmapper.crud.entities.dto.UsuarioDTO;
import com.modelmapper.crud.repository.UsuarioRepository;
import com.modelmapper.crud.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional // pra dar rollback no banco depois de cada teste
public class UsuarioServiceIntegrationsTests {

	@Autowired
	private UsuarioService service;
	@Autowired
	private UsuarioRepository repository;

	private long idExistente;
	private long idNaoExistente;
	private long totalUsuarios;
	private Usuario usuarioNovo;
	private Usuario usuario;

	@BeforeEach
	private void setUp() throws Exception {
		idExistente = 1L;
		idNaoExistente = 1000L;
		totalUsuarios = 10L;
		usuarioNovo = new Usuario(null, "Igor", "Lourenço", "123");
		usuario = repository.getById(idExistente);
	}
	
	@Test
	public void findByIdDeveriaRetornarUsuarioDTOQuandoIdExistir() {	
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(idNaoExistente);
		});
	}

	@Test
	public void updateDeveriaRetornarUsuarioDTOAtualizadoQuandoIdExistir() {
		UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
		usuarioDTO.setNome("AlexTeste");

		UsuarioDTO novoUsuarioDTO = service.update(idExistente, usuarioDTO);

		Assertions.assertEquals(totalUsuarios, repository.count());
		Assertions.assertTrue(novoUsuarioDTO.getNome().contains("AlexTeste"));
		Assertions.assertTrue(novoUsuarioDTO.getId() == 1);
	}

	@Test
	public void updateDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
		UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
		usuarioDTO.setNome("AlexTeste");

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(idNaoExistente, usuarioDTO);
		});
	}

	@Test
	public void insertDeveriaRetornarNovoUsuarioDTO() {
		UsuarioComSenhaDTO dto = new UsuarioComSenhaDTO(usuarioNovo);

		UsuarioDTO usuarioDTO = service.insert(dto);

		Assertions.assertEquals(totalUsuarios + 1, repository.count());
		Assertions.assertTrue(usuarioDTO.getNome().contains("Igor"));
		Assertions.assertTrue(usuarioDTO.getId() == 11);
	}

	@Test
	public void findAllDeveriaRetornarListaOrdenadaPorNome() {
		String nome = "a";

		List<UsuarioDTO> resultado = service.findAll(nome);

		Assertions.assertTrue(resultado.get(0).getNome().contains("Alex"));
		Assertions.assertTrue(resultado.get(1).getNome().contains("Arthur"));
		Assertions.assertTrue(resultado.get(2).getNome().contains("Helena"));
		Assertions.assertTrue(resultado.get(3).getNome().contains("Laura"));
	}

	@Test
	public void findAllDeveriaRetornarListaVaziaQuandoNomeNaoExistir() {
		String nome = "w";

		List<UsuarioDTO> resultado = service.findAll(nome);

		Assertions.assertTrue(resultado.isEmpty());
	}

	@Test
	public void findAllDeveriaRetornarLista() {
		String nome = "a";

		List<UsuarioDTO> resultado = service.findAll(nome);

		Assertions.assertFalse(resultado.isEmpty());
		Assertions.assertEquals(5, resultado.size());
		Assertions.assertTrue(resultado.get(0).getNome().contains("Alex"));
	}

	@Test
	public void deleteDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(idNaoExistente);
		});
	}

	@Test
	public void deleteDeveriaDeletarUsuarioQuandoIdExistir() {
		service.delete(idExistente);

		Assertions.assertEquals(totalUsuarios - 1, repository.count());
		Assertions.assertFalse(repository.existsById(idExistente));
	}
}
