package com.service.desk.service.service;

import java.util.List;

import com.service.desk.dto.AlterarSenhaDTO;
import com.service.desk.dto.AuthenticationDTO;
import com.service.desk.dto.LoginResponseDTO;
import com.service.desk.dto.UsuarioRequestDTO;
import com.service.desk.dto.UsuarioResponseDTO;

public interface UsuarioService {

	LoginResponseDTO authenticarUsuario(AuthenticationDTO dadosAutenticacao);

	void registrarUsuario(UsuarioRequestDTO dadosRegistro);

	void atualizarUsuario(Long usuarioId, UsuarioRequestDTO dto);

	void atualizarStatusUsuario(Long idUsuario, Boolean ativo);

	List<UsuarioResponseDTO> listarUsuarios();

	void alterarSenha(Long usuarioId, AlterarSenhaDTO dto);

}
