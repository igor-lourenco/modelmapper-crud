package com.modelmapper.crud.entities.dto;

import java.io.Serializable;

import com.modelmapper.crud.entities.Usuario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class UsuarioComSenhaDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String sobrenome;
	private String senha;
	
	public UsuarioComSenhaDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.sobrenome = usuario.getSobrenome();
		this.senha = usuario.getSenha();
	}	
}
