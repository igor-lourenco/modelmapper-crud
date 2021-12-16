package com.modelmapper.crud.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.modelmapper.crud.entities.dto.UsuarioComSenhaDTO;
import com.modelmapper.crud.entities.dto.UsuarioDTO;
import com.modelmapper.crud.services.UsuarioService;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

	@DeleteMapping(value = "{id}")
	public ResponseEntity<UsuarioDTO> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> findAll(@RequestParam(value = "nome", defaultValue = "") String nome) {
		var obj = service.findAll(nome.trim());
		return ResponseEntity.ok().body(obj);
	}

	@PutMapping(value = "{id}")
	public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
		var obj = service.update(id, dto);
		return ResponseEntity.ok(obj);
	}
 

	@PostMapping()
	public ResponseEntity<UsuarioDTO> insert(@RequestBody UsuarioComSenhaDTO dto) {
		var obj = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UsuarioComSenhaDTO> findById(@PathVariable Long id) {
		var obj = service.findById(id);
		return ResponseEntity.ok(obj);
	}
}
