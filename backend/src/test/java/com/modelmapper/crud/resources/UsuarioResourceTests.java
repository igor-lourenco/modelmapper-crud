package com.modelmapper.crud.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modelmapper.crud.entities.dto.UsuarioDTO;
import com.modelmapper.crud.services.UsuarioService;
import com.modelmapper.crud.services.exceptions.ResourceNotFoundException;
import com.modelmapper.crud.tests.Factory;

@WebMvcTest(UsuarioResource.class)
public class UsuarioResourceTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	private UsuarioDTO usuarioDTO;
	private List<UsuarioDTO> lista = new ArrayList<>();
	private long idExistente;
	private long idNaoExistente;
	
	@MockBean // usado quando a classe de teste carrega o ocntexto da aplicação e precisa mockar algum bean do sistema
	private UsuarioService service;

	@BeforeEach
	private void setUp() throws Exception {
		idExistente = 1L;
		idNaoExistente = 1000L;
		usuarioDTO = Factory.CriaUsuarioDTO();
		lista.add(usuarioDTO);
		
		when(service.findAll(any())).thenReturn(lista);
		
		when(service.findById(idExistente)).thenReturn(usuarioDTO);
		when(service.findById(idNaoExistente)).thenThrow(ResourceNotFoundException.class);
		
		when(service.insert(any())).thenReturn(usuarioDTO);

		when(service.update(eq(idExistente), any())).thenReturn(usuarioDTO);
		when(service.update(eq(idNaoExistente), any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(service).delete(idExistente);
		doThrow(ResourceNotFoundException.class).when(service).delete(idNaoExistente);
	}
	
	@Test
	public void insertDeveriaRetornarUsuarioDTO() throws Exception {
	String json = objectMapper.writeValueAsString(usuarioDTO); // converte para json
		
	ResultActions resultado = mockMvc.perform(post("/usuarios")
			.content(json)  //corpo da requisição
			.contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
			.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
	
	resultado.andExpect(status().isCreated());
	resultado.andExpect(jsonPath("$.id").exists()); // tem que existir um campo id no json
	resultado.andExpect(jsonPath("$.nome").exists()); // tem que existir um campo nome no json
	resultado.andExpect(jsonPath("$.sobrenome").exists()); // tem que existir um campo nome no json
	}
	
	@Test
	public void deleteDeveriaRetornarNoContentQuandoIdExistir() throws Exception {
		ResultActions resultado = mockMvc.perform(delete("/usuarios/{id}", idExistente)
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isNoContent());
	}
		
	@Test
	public void deleteDeveriaRetornarNotFoundQuandoIdNaoExistir() throws Exception {
		ResultActions resultado = mockMvc.perform(delete("/usuarios/{id}", idNaoExistente)
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isNotFound());
	}
	@Test
	public void updateDeveriaRetornarUsuarioDTOQuandoIdExistir() throws Exception {
	String json = objectMapper.writeValueAsString(usuarioDTO); // converte para json
		
	ResultActions resultado = mockMvc.perform(put("/usuarios/{id}", idExistente)
			.content(json)  //corpo da requisição
			.contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
			.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
	
	resultado.andExpect(status().isOk());
	resultado.andExpect(jsonPath("$.id").exists()); // tem que existir um campo id no json
	resultado.andExpect(jsonPath("$.nome").exists()); // tem que existir um campo nome no json
	resultado.andExpect(jsonPath("$.sobrenome").exists()); // tem que existir um campo nome no json
	}
	
	@Test
	public void updateDeveriaRetornarNotFoundQuandoIdNaoExistir() throws Exception {
		String json = objectMapper.writeValueAsString(usuarioDTO); // converte para json
		
		ResultActions resultado = mockMvc.perform(put("/usuarios/{id}", idNaoExistente)
				.content(json)  //corpo da requisição
				.contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdDeveriaRetornarUsuarioDTOQuandoIdExistir() throws Exception {
		ResultActions resultado = mockMvc.perform(get("/usuarios/{id}", idExistente)
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.id").exists()); // tem que existir um campo id no json
		resultado.andExpect(jsonPath("$.nome").exists()); // tem que existir um campo nome no json
		resultado.andExpect(jsonPath("$.sobrenome").exists()); // tem que existir um campo nome no json
	}
	
	@Test
	public void findByIdDeveriaRetornarNotFoundQuandoIdNaoExistir() throws Exception {
		ResultActions resultado = mockMvc.perform(get("/usuarios/{id}", idNaoExistente)
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isNotFound());
	}
	
	@Test
	public void findAllDeveriaRetornarLista() throws Exception {
		ResultActions resultado = mockMvc.perform(get("/usuarios")
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isOk());
	}
}
