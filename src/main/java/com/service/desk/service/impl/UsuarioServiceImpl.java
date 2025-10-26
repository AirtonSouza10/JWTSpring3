package com.service.desk.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.desk.dto.AlterarSenhaDTO;
import com.service.desk.dto.AuthenticationDTO;
import com.service.desk.dto.LoginResponseDTO;
import com.service.desk.dto.UsuarioRequestDTO;
import com.service.desk.dto.UsuarioResponseDTO;
import com.service.desk.entidade.Pessoa;
import com.service.desk.entidade.Usuario;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.exceptions.NegocioException;
import com.service.desk.repository.PessoaRepository;
import com.service.desk.repository.RoleRepository;
import com.service.desk.repository.UsuarioRepository;
import com.service.desk.security.TokenService;
import com.service.desk.service.service.UsuarioService;
import com.service.desk.utils.UsuarioLogadoUtil;

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
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Override
	public LoginResponseDTO authenticarUsuario (AuthenticationDTO dadosAutenticacao) {
		var userNamePassword = new UsernamePasswordAuthenticationToken(dadosAutenticacao.getLogin(), dadosAutenticacao.getSenha());
		var auth = this.authenticationManager.authenticate(userNamePassword);
		var token = tokenService.generateToken((Usuario) auth.getPrincipal());
		return LoginResponseDTO.builder().token(token).build();
	}
	
	@Override
	public void registrarUsuario (UsuarioRequestDTO dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()) != null) {
            throw new NegocioException("Login já existe");
        }

        var encryptedPassword = new BCryptPasswordEncoder().encode(dto.getSenha());

        var roleUsuario = roleRepository.findByNome("USER");

        var usuario = Usuario.builder()
                .login(dto.getIdentificacao())
                .identificacao(dto.getIdentificacao())
                .senha(encryptedPassword)
                .roles(Set.of(roleUsuario))
                .build();
        var usuarioSalvo = usuarioRepository.save(usuario);
        
        var pessoa = new Pessoa();
        pessoa.setNome(dto.getNome());
        pessoa.setIdentificacao(dto.getIdentificacao());
        pessoa.setEmail(dto.getEmail());
        pessoa.setTelefone(dto.getTelefone());
        pessoa.setTpPessoa(dto.getIdentificacao().length() == 11 ? "4" : "3");
        pessoa.setUsuario(usuarioSalvo);
        pessoa.setDtInclusao(new Date());
        pessoa.setAtivo(true);

        pessoaRepository.save(pessoa);
	}

	@Override
	public void atualizarUsuario(Long usuarioId, UsuarioRequestDTO dto) {
	    var usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE014.getKey()));

	    if (dto.getLogin() != null && !dto.getLogin().isBlank()) {
	        usuario.setLogin(dto.getLogin());
	    }
	    if (dto.getIdentificacao() != null && !dto.getIdentificacao().isBlank()) {
	        usuario.setIdentificacao(dto.getIdentificacao());
	    }

	    var pessoa = pessoaRepository.findByIdentificacao(usuario.getIdentificacao());

	    if (dto.getNome() != null) pessoa.setNome(dto.getNome());
	    if (dto.getEmail() != null) pessoa.setEmail(dto.getEmail());
	    if (dto.getTelefone() != null) pessoa.setTelefone(dto.getTelefone());
	    if (dto.getTpPessoa() != null) pessoa.setTpPessoa(dto.getTpPessoa().length() == 11 ? "4" : "3");

	    usuarioRepository.save(usuario);
	    pessoaRepository.save(pessoa);
	}
	
	@Override
	public void atualizarStatusUsuario(Long idUsuario, Boolean ativo) {
	    var usuario = usuarioRepository.findById(idUsuario)
	            .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE014.getKey()));

	    var pessoa = pessoaRepository.findByIdentificacao(usuario.getIdentificacao());

	    pessoa.setAtivo(ativo);
	    pessoaRepository.save(pessoa);
	}
	
	@Override
	public List<UsuarioResponseDTO> listarUsuarios() {
		Usuario usuarioLogado = UsuarioLogadoUtil.getUsuarioLogado();

		boolean isAdmin = usuarioLogado.getRoles().stream().anyMatch(role -> role.getNome().equalsIgnoreCase("ADMIN"));

		List<Pessoa> pessoas;
		if (isAdmin) {
			pessoas = pessoaRepository.findAll();
		} else {
			Pessoa pessoa = pessoaRepository.findByIdentificacao(usuarioLogado.getIdentificacao());
			pessoas = List.of(pessoa);
		}

		return pessoas.stream().map(p -> UsuarioResponseDTO.builder().login(p.getUsuario().getLogin()).id(p.getId())
				.identificacao(p.getIdentificacao()).nome(p.getNome()).email(p.getEmail()).telefone(p.getTelefone())
				.tpPessoa(p.getTpPessoa())
				.ativo(p.getAtivo().booleanValue())
				.build()).toList();
	}
	
	@Override
	public void alterarSenha(Long usuarioId, AlterarSenhaDTO dto) {
	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new NegocioException(MensagemEnum.MSGE014.getKey()));

	    Usuario usuarioLogado = UsuarioLogadoUtil.getUsuarioLogado();

	    boolean isAdmin = usuarioLogado.getRoles().stream()
	            .anyMatch(role -> role.getNome().equalsIgnoreCase("ADMIN"));

	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	    if (!isAdmin) {
	        if (!encoder.matches(dto.getSenhaAntiga(), usuario.getSenha())) {
	            throw new NegocioException(MensagemEnum.MSGE014.getKey());
	        }
	    }

	    usuario.setSenha(encoder.encode(dto.getSenhaNova()));
	    usuarioRepository.save(usuario);
	}
}
