package com.service.desk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.service.desk.dto.AuthenticationDTO;
import com.service.desk.dto.UsuarioRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(RequestMappingConstants.AUTH)
@Tag(name = "Autenticação", description = "End points de autenticação")
public class AuthenticationController  extends ControllerServiceDesk{
	@Autowired
	private UsuarioService usuarioService;
	
	@Operation(summary = "Login", security = {})
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public ResponseServiceDesk login(@RequestBody @Valid AuthenticationDTO dadosAutenticacao) {
		return new ResponseServiceDesk(usuarioService.authenticarUsuario(dadosAutenticacao));
	}
	
	@Operation(summary = "Registrar novo usuário")
	@PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
	public ResponseServiceDesk registrar(@RequestBody @Valid UsuarioRequestDTO dadosRegistro) {
		usuarioService.registrarUsuario(dadosRegistro);
		return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
	}
}
