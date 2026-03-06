package com.javanauta.bffagendadortarefas.business;

import com.javanauta.bffagendadortarefas.business.dto.EnderecoDTO;
import com.javanauta.bffagendadortarefas.business.dto.TelefoneDTO;
import com.javanauta.bffagendadortarefas.business.dto.UsuarioDTO;
import com.javanauta.bffagendadortarefas.infrastructure.client.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioClient client;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {

        return client.salvaUsuario(usuarioDTO);
    }

    public String loginUsuario(UsuarioDTO usuarioDTO) {

        return client.login(usuarioDTO);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email, String token) {
        return client.buscaUsuarioPorEmail(email, token);
    }


    public void deletaUsuarioPorEmail(String email, String token) {

        client.deletaUsuarioPorEmail(email, token);

    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {

        return client.atualizaDadosUsuario(usuarioDTO, token);

    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO, String token) {

        return client.atualizaEndereco(enderecoDTO, idEndereco, token);

    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO, String token) {

        return client.atualizaTelefone(telefoneDTO, idTelefone, token);
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO enderecoDTO) {

        return client.cadastraEndereco(enderecoDTO, token);

    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO telefoneDTO) {


        return client.cadastraTelefone(telefoneDTO, token);


    }


}
