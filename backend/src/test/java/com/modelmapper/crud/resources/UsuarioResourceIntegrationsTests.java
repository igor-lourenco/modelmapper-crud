package com.modelmapper.crud.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modelmapper.crud.entities.dto.UsuarioComSenhaDTO;
import com.modelmapper.crud.entities.dto.UsuarioDTO;
import com.modelmapper.crud.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsuarioResourceIntegrationsTests {

	@Autowired //pra fazer as requisições com o mock
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	
	private long idExistente;
	private long idNaoExistente;
	private UsuarioDTO usuarioDTO;
	private UsuarioComSenhaDTO usuarioComSenhaDTO;
	

	@BeforeEach
	private void setUp() throws Exception {
		idExistente = 1L;
		idNaoExistente = 1000L;
		usuarioDTO = Factory.CriaUsuarioDTO();
		usuarioComSenhaDTO = Factory.CriaUsuarioComSenhaDTO();
	}
	
	@Test
	public void findByIdDeveriaRetornarUsuarioDTOQuandoIdExistir() throws Exception {
		ResultActions resultado = mockMvc.perform(get("/usuarios/{id}", idExistente)
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.id").value(idExistente)); // tem que existir um campo id no json
		resultado.andExpect(jsonPath("$.nome").value("Alex")); // tem que existir um campo nome no json
		resultado.andExpect(jsonPath("$.sobrenome").value("Green")); // tem que existir um campo nome no json
	}
	
	@Test
	public void findByIdDeveriaRetornarNotFoundQuandoIdNaoExistir() throws Exception {
		ResultActions resultado = mockMvc.perform(get("/usuarios/{id}", idNaoExistente)
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isNotFound());
	}
	
	@Test
	public void insertDeveriaRetornarUsuarioDTO() throws Exception {
		String corpoJson = objectMapper.writeValueAsString(usuarioComSenhaDTO); // converte para json
		
		String nomeEsperado = usuarioComSenhaDTO.getNome(); // antes de atualizar
		String sobrenomeEsperado = usuarioComSenhaDTO.getSobrenome();
		
		ResultActions resultado = mockMvc.perform(post("/usuarios")
				.content(corpoJson)  //corpo da requisição
				.contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isCreated());
		resultado.andExpect(jsonPath("$.id").value(11)); // tem que existir um campo id no json
		resultado.andExpect(jsonPath("$.nome").value(nomeEsperado)); // tem que existir um campo nome no json
		resultado.andExpect(jsonPath("$.sobrenome").value(sobrenomeEsperado)); // tem que existir um campo nome no json
	}
	
	@Test
	public void updateDeveriaRetornarNotFoundQuandoIdNaoExistir() throws Exception {
		String corpoJson = objectMapper.writeValueAsString(usuarioDTO); // converte para json
		
			
		ResultActions resultado = mockMvc.perform(put("/usuarios/{id}", idNaoExistente)
				.content(corpoJson)  //corpo da requisição
				.contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateDeveriaRetornarUsuarioDTOQuandoIdExistir() throws Exception {
		String corpoJson = objectMapper.writeValueAsString(usuarioDTO); // converte para json
		
		String nomeEsperado = usuarioDTO.getNome(); // antes de atualizar
		String sobrenomeEsperado = usuarioDTO.getSobrenome();
		
		ResultActions resultado = mockMvc.perform(put("/usuarios/{id}", idExistente)
				.content(corpoJson)  //corpo da requisição
				.contentType(MediaType.APPLICATION_JSON) // tipo do corpo da requisição
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$.id").value(idExistente)); // tem que existir um campo id no json
		resultado.andExpect(jsonPath("$.nome").value(nomeEsperado)); // tem que existir um campo nome no json
		resultado.andExpect(jsonPath("$.sobrenome").value(sobrenomeEsperado)); // tem que existir um campo nome no json
	}
	
	@Test
	public void findAllDeveriaRetornarListaOrdenadaPorNome() throws Exception{
		ResultActions resultado = mockMvc.perform(get("/usuarios?nome=a")
				.accept(MediaType.APPLICATION_JSON)); // tipo de resposta da requisição
		
		resultado.andExpect(status().isOk());
		resultado.andExpect(jsonPath("$[0].nome").value("Alex"));
		resultado.andExpect(jsonPath("$[1].nome").value("Arthur"));
	}
}
