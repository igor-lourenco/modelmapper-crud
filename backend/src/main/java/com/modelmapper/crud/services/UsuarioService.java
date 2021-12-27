package com.modelmapper.crud.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modelmapper.crud.entities.Usuario;
import com.modelmapper.crud.entities.dto.UsuarioComSenhaDTO;
import com.modelmapper.crud.entities.dto.UsuarioDTO;
import com.modelmapper.crud.repository.UsuarioRepository;
import com.modelmapper.crud.services.exceptions.DatabaseException;
import com.modelmapper.crud.services.exceptions.ResourceNotFoundException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private ModelMapper modelMapper;

	public void delete(Long id) {
		try {
			repository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Usuario não deletado");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integridade no banco");
		}
	}

	@Transactional()
	public UsuarioDTO update(Long id, UsuarioDTO dto) {
		try {
			Usuario entity = repository.getById(id);
			modelMapper.map(dto, entity);
			entity = repository.save(entity);
			return modelMapper.map(entity, UsuarioDTO.class);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Usuario não atualizado");
		} catch (MappingException e) {
			throw new ResourceNotFoundException("Usuario não atualizado");
		}
	}

	@Transactional()
	public UsuarioDTO insert(UsuarioComSenhaDTO dto) {
		Usuario entity = modelMapper.map(dto, Usuario.class);
		entity = repository.save(entity);
		return modelMapper.map(entity, UsuarioDTO.class);
	}

	@Transactional(readOnly = true)
	public List<UsuarioDTO> findAll(String nome) {
		List<Usuario> entity = repository.findByNomeIgnoreCaseContainingOrderByNome(nome);
		return entity.stream().map(u -> modelMapper.map(u, UsuarioDTO.class)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UsuarioComSenhaDTO findById(Long id) {
		Usuario entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario não existe"));
		return modelMapper.map(entity, UsuarioComSenhaDTO.class);
	}
}
