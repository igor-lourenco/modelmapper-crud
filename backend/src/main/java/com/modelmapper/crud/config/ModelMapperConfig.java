package com.modelmapper.crud.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.modelmapper.crud.entities.Usuario;
import com.modelmapper.crud.entities.dto.UsuarioDTO;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		
		//método para fazer update no service
		mapper.addMappings(new PropertyMap<UsuarioDTO, Usuario>() {
			@Override
			protected void configure() {
				skip(destination.getId());
			}
		});
		
		return mapper;
	}
}
