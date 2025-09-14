package com.service.desk.service.impl;

import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.desk.dto.AuthenticationDTO;
import com.service.desk.dto.LoginResponseDTO;
import com.service.desk.dto.UsuarioRequestDTO;
import com.service.desk.entidade.Usuario;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.exceptions.NegocioException;
import com.service.desk.repository.RoleRepository;
import com.service.desk.repository.UsuarioRepository;
import com.service.desk.security.TokenService;
import com.service.desk.service.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public LoginResponseDTO authenticarUsuario (AuthenticationDTO dadosAutenticacao) {
		var userNamePassword = new UsernamePasswordAuthenticationToken(dadosAutenticacao.getLogin(), dadosAutenticacao.getSenha());
		var auth = this.authenticationManager.authenticate(userNamePassword);
		var token = tokenService.generateToken((Usuario) auth.getPrincipal());
		return LoginResponseDTO.builder().token(token).build();
	}
	
	@Override
	public void registrarUsuario (UsuarioRequestDTO dadosRegistro) {
		var usuario = usuarioRepository.findByLogin(dadosRegistro.getLogin());
		if(Objects.nonNull(usuario)) {
			throw new NegocioException(MensagemEnum.MSGE006.getKey());
		}
		
		var encryptedPassword = new BCryptPasswordEncoder().encode(dadosRegistro.getSenha());
		
	    var roleTipoNota = roleRepository.findByNome("USER");
		
		var novoUsuario = Usuario.builder()
				.login(dadosRegistro.getLogin())
				.identificacao(dadosRegistro.getIdentificacao())
				.senha(encryptedPassword)
				.roles(Set.of(roleTipoNota))
				.build();
		
		usuarioRepository.save(novoUsuario);
	}
}
