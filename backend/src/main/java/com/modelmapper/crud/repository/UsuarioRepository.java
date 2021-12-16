package com.modelmapper.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modelmapper.crud.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	
	List<Usuario> findByNomeIgnoreCaseContainingOrderByNome(String nome);

}
