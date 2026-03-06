package com.javanauta.bffagendadortarefas.infrastructure.documentation;



import com.javanauta.bffagendadortarefas.business.dto.EnderecoDTO;
import com.javanauta.bffagendadortarefas.business.dto.TelefoneDTO;
import com.javanauta.bffagendadortarefas.business.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Usuario", description = "Cadastro e login de Usuarios")
public interface UsuarioControllerDocumentation {

    @Operation(summary = "Salva um usuario", description = "Criar um novo usuario" )
    @ApiResponse(responseCode = "200", description = "Usuario salvo com sucesso")
    @ApiResponse(responseCode = "400", description = "Usuario ja cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    ResponseEntity<UsuarioDTO> salvaUsuario(UsuarioDTO usuarioDTO);

    @Operation(summary = "Login usuario", description = "Login de usuario" )
    @ApiResponse(responseCode = "200", description = "Usuario logado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping("/login")
    String login(UsuarioDTO usuarioDTO);

    @Operation(summary = "Buscar dados de usuario por Email", description = "Buscar dados do usuario" )
    @ApiResponse(responseCode = "200", description = "Usuario encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(String email, String token);

    @Operation(summary = "Deletar usuario por ID", description = "Deleta usuario" )
    @ApiResponse(responseCode = "200", description = "Usuario deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping("/{email}")
    ResponseEntity<Void> deletaUsuarioPorEmail(String email, String token);

    @Operation(summary = "Atualizar dados de Usuarios", description = "Atualizar dados de usuario" )
    @ApiResponse(responseCode = "200", description = "Usuario atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PutMapping
    ResponseEntity<UsuarioDTO> atualizaDadosUsuario(UsuarioDTO usuarioDTO, String Token);

    @Operation(summary = "Atualiza Endereco de Usuario", description = "Atualizar enderecos de usuario" )
    @ApiResponse(responseCode = "200", description = "Endereco atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PutMapping("/endereco")
    ResponseEntity<EnderecoDTO> atualizaEndereco(EnderecoDTO enderecoDTO, Long id, String token);

    @Operation(summary = "Atualiza Telefones de Usuario", description = "Atualizar telefones de usuarios" )
    @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PutMapping("/telefone")
    ResponseEntity<TelefoneDTO> atualizaTelefone(TelefoneDTO telefoneDTO, Long id, String token);

    @Operation(summary = "Salva Endereco de Usuario", description = "Salvar o endereco de um usuario" )
    @ApiResponse(responseCode = "200", description = "Endereco salvo com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping("/endereco")
    ResponseEntity<EnderecoDTO> cadastraEndereco(EnderecoDTO enderecoDTO, String token);

    @Operation(summary = "Salva Telefone Usuario", description = "Salvar o telefone de um usuario" )
    @ApiResponse(responseCode = "200", description = "Telefone salvo com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping("/telefone")
    ResponseEntity<TelefoneDTO> cadastraTelefone(TelefoneDTO telefoneDTO, String token);



}
