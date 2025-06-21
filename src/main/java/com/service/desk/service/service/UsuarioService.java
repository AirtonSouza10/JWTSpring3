package com.service.desk.service.service;

import com.service.desk.dto.AuthenticationDTO;
import com.service.desk.dto.LoginResponseDTO;
import com.service.desk.dto.UsuarioRequestDTO;

public interface UsuarioService {

	LoginResponseDTO authenticarUsuario(AuthenticationDTO dadosAutenticacao);

	void registrarUsuario(UsuarioRequestDTO dadosRegistro);

}
