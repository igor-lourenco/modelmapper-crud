package com.modelmapper.crud.entities.dto;

import java.io.Serializable;

import com.modelmapper.crud.entities.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String sobrenome;
	private String senha;
	
	public UsuarioDTO(Usuario obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.sobrenome = obj.getSobrenome();
		this.senha = obj.getSenha();
	}
}
