package com.fcosoftware.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fcosoftware.minhasfinancas.exception.ErroAutenticacao;
import com.fcosoftware.minhasfinancas.exception.RegraNegocioException;
import com.fcosoftware.minhasfinancas.model.entity.Usuario;
import com.fcosoftware.minhasfinancas.model.repository.UsuarioRepository;
import com.fcosoftware.minhasfinancas.service.impl.UsuarioServiceImpl;

//@SpringBootTest
@RunWith(SpringRunner.class)
//@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	//@Autowired
	//UsuarioService service;
	
	@SpyBean
	UsuarioServiceImpl service;
	
	//@Autowired
	@MockBean
	UsuarioRepository repository;
	
	/*
	@Before
	public void setUp() {
		//repository = Mockito.mock(UsuarioRepository.class);
		//service = new UsuarioServiceImpl(repository);
		service = Mockito.spy(UsuarioServiceImpl.class);
	}
	*/
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {
		//Cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("franciscocunha35@gmail.com")
				.senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//Acão
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("franciscocunha35@gmail.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//Cenario 
		String email = "franciscocunha35@gmail.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//Ação
		service.salvarUsuario(usuario);
		
		//Verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//Cenario
		//String nome = "francisco";
		String email = "franciscocunha35@gmail.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when( repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//Ação 
		Usuario result = service.autenticar(email, senha);
	
		//Verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	//@Test(expected = ErroAutenticacao.class)
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//Cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("franciscocunha35@gmail.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//Ação
	    Throwable exception = Assertions.catchThrowable(() -> service.autenticar("franciscocunha35@gmail.com", "123"));
	    Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Invalida!!");
	}
	
	//@Test(expected = ErroAutenticacao.class)
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCacastradoComEmailInformado() {
		//Cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//Ação
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("franciscocunha35@gmail.com", "senha"));
	    
		//Verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
	    .hasMessage("Usuario não encontrado, para email informado!!");
	    
		
	}
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//Cenario
		//repository.deleteAll();
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("franciscocunha35@gmail.com");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLançarErroAoValidarEmailQuandoExixtirEmailCadastrado() {
		//Cenario
		//Usuario usuario = Usuario.builder().nome("Francisco").email("franciscocunha35@gmail.com").build();
		//repository.save(usuario);
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//Ação
		service.validarEmail("franciscocunha35@gmail.com");
	}
	
	
}
