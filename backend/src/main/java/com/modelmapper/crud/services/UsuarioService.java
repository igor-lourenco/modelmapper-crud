package com.modelmapper.crud.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modelmapper.crud.entities.Usuario;
import com.modelmapper.crud.entities.dto.UsuarioDTO;
import com.modelmapper.crud.repository.UsuarioRepository;
import com.modelmapper.crud.services.exceptions.DatabaseException;
import com.modelmapper.crud.services.exceptions.ResourceNotFoundException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public void delete(Long id) {
		try {
			repository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Usuario não deletado");
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integridade no banco"); 
		}
	}

	@Transactional()
	public UsuarioDTO update(Long id, UsuarioDTO dto) {
		Usuario entity = repository.getById(id);
		try {
			entity.setNome(dto.getNome());
			entity.setSobrenome(dto.getSobrenome());
			entity.setSenha(dto.getSenha());
			entity = repository.save(entity);
			return new UsuarioDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Usuario não atualizado");
		}
	}

	@Transactional()
	public UsuarioDTO insert(UsuarioDTO dto) {
		Usuario entity = new Usuario();
		entity.setNome(dto.getNome());
		entity.setSobrenome(dto.getSobrenome());
		entity.setSenha(dto.getSenha());
		entity = repository.save(entity);
		return new UsuarioDTO(entity);
	}

	@Transactional(readOnly = true)
	public List<UsuarioDTO> findAll(String nome) {
		List<Usuario> entity = repository.findByNomeContainingOrderByNome(nome);
		return entity.stream().map(u -> new UsuarioDTO(u)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UsuarioDTO findById(Long id) {
		Usuario entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario não existe"));
		return new UsuarioDTO(entity);
	}
}
