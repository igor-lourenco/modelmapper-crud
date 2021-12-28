package com.modelmapper.crud.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.modelmapper.crud.entities.Usuario;
import com.modelmapper.crud.entities.dto.UsuarioComSenhaDTO;
import com.modelmapper.crud.entities.dto.UsuarioDTO;
import com.modelmapper.crud.repository.UsuarioRepository;
import com.modelmapper.crud.services.exceptions.ResourceNotFoundException;
import com.modelmapper.crud.tests.Factory;

@ExtendWith(SpringExtension.class)
public class UsuarioServiceTests {

	@InjectMocks // pra simular uma injeção da classe
	private UsuarioService service;

	@Mock // pra simular o comportamento do modelmapper
	private ModelMapper modelMapper;

	@Mock // pra simular o comportamento dos métodos do repository
	private UsuarioRepository repository;

	private long idExistente;
	private long idNaoExistente;
	private List<Usuario> lista = new ArrayList<>();
	private Usuario usuario;
	private UsuarioDTO usuarioDTO;

	@BeforeEach
	private void setUp() throws Exception {
		idExistente = 1L;
		idNaoExistente = 1000L;
		usuario = Factory.CriaUsuario();
		usuarioDTO = Factory.CriaUsuarioDTO();

		when(modelMapper.map(any(), any())).thenReturn(usuarioDTO);

		when(repository.findByNomeIgnoreCaseContainingOrderByNome(any())).thenReturn(lista);

		when(repository.findById(idExistente)).thenReturn(Optional.of(usuario));
		when(repository.findById(idNaoExistente)).thenReturn(Optional.empty());

		when(repository.getById(idExistente)).thenReturn(usuario);
		when(repository.getById(idNaoExistente)).thenThrow(EntityNotFoundException.class);

		when(repository.save(any())).thenReturn(usuario);

		doNothing().when(repository).deleteById(idExistente);
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(idNaoExistente);
	}
	
	@Test
	public void saveDeveriaRetornarUsuarioDTO() {
		UsuarioComSenhaDTO novo = Factory.CriaUsuarioComSenhaDTO();
		
		UsuarioDTO dto = service.insert(novo);
		
		Assertions.assertEquals(dto.getNome(), "Igor");
		Assertions.assertNotNull(dto);
	}

	@Test
	public void updateDeveriaRetornarUsuarioDTOAtualizadoQuandoIdExistir() {
		UsuarioDTO dto = service.update(idExistente, usuarioDTO);

		Assertions.assertNotNull(dto);
		verify(repository, times(1)).getById(idExistente);
	}

	@Test
	public void updateDeveriaLancarEntityNotFoundExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(idNaoExistente, usuarioDTO);
		});
		verify(repository, times(1)).getById(idNaoExistente);
	}

	@Test
	public void findByidDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(idNaoExistente);
		});

		verify(repository, times(1)).findById(idNaoExistente);
	}

	@Test
	public void findByIdDeveriaRetornarUsuarioDTOQuandoIdExistir() {
		UsuarioDTO dto = service.findById(idExistente);

		Assertions.assertTrue(dto.getNome() == "Igor");
		verify(repository, times(1)).findById(idExistente);
	}

	@Test
	public void findAllDeveriaRetornarListaUsuario() {
		String nome = "a";

		List<UsuarioDTO> dto = service.findAll(nome);

		Assertions.assertNotNull(dto);
		verify(repository, times(1)).findByNomeIgnoreCaseContainingOrderByNome(nome);
	}

	@Test
	public void deleteDeveriaLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(idNaoExistente);
		});
		verify(repository, times(1)).deleteById(idNaoExistente);
	}

	@Test
	public void deleteDeveriaFazerNadaQuandoIdExistir() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(idExistente);
		});
		verify(repository, times(1)).deleteById(idExistente);
	}

}
