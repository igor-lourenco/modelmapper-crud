package com.modelmapper.crud.tests;

import com.modelmapper.crud.entities.Usuario;
import com.modelmapper.crud.entities.dto.UsuarioComSenhaDTO;

public class Factory {

	public static Usuario CriaUsuario() {
		Usuario usuario = new Usuario(null, "Igor", "Lourenço", "123");
		return usuario;
	}
	public static Usuario AtualizaUsuario() {
		Usuario usuario = new Usuario(1L, "AlexTeste", "Green", "123");
		return usuario;
	}
	public static UsuarioComSenhaDTO CriaUsuarioComSenhaDTO() {
		Usuario usuario = CriaUsuario();
		return new UsuarioComSenhaDTO(usuario);
	}
}
