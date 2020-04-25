package com.fcosoftware.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fcosoftware.minhasfinancas.model.entity.Usuario;

//@SpringBootTest
//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TestEntityManager entityManage;
	
	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		// cenário
	 	Usuario usuario = criarUsuario();
	 	//usuarioRepository.save(usuario);
	 	entityManage.persist(usuario);
	 	
		// ação execução
		boolean result = usuarioRepository.existsByEmail("francisco@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioComEmail() {
		// cenário
		//usuarioRepository.deleteAll();
		
		//ação
		boolean result = usuarioRepository.existsByEmail("francisco@gmail.com");
		
		//Verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		// Cenário
		Usuario usuario = criarUsuario();
		
		 //Ação
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		//Verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUsuarioPorEmail() {
		// Cenário
				Usuario usuario = criarUsuario();
				entityManage.persist(usuario);
				
		//Verificação
		Optional<Usuario> result = usuarioRepository.findByEmail("francisco@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetonarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		// Cenário
				
		//Verificação
		Optional<Usuario> result = usuarioRepository.findByEmail("francisco@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criarUsuario() {
		return Usuario.builder()
				.nome("francisco")
				.email("francisco@gmail.com")
				.senha("senha").build();
		
	}

	
}
