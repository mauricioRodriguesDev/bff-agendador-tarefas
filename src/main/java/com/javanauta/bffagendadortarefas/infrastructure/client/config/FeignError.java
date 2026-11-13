package com.javanauta.bffagendadortarefas.infrastructure.client.config;

import com.javanauta.bffagendadortarefas.infrastructure.exceptions.BusinessException;
import com.javanauta.bffagendadortarefas.infrastructure.exceptions.ConflictException;
import com.javanauta.bffagendadortarefas.infrastructure.exceptions.IllegalArgumentException;
import com.javanauta.bffagendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.bffagendadortarefas.infrastructure.exceptions.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class FeignError implements ErrorDecoder {

    private static final String ERRO = "Erro: ";


    @Override
    public Exception decode(String s, Response response) {


        String mensagemErro = mensagemErro(response);

        switch (response.status()) {
            case 409:
                return new ConflictException(ERRO + mensagemErro);
            case 403:
                return new ResourceNotFoundException(ERRO + mensagemErro);
            case 401:
                return new UnauthorizedException(ERRO + mensagemErro);
            case 400:
                return new IllegalArgumentException(ERRO + mensagemErro);
            default:
                return new BusinessException(ERRO + mensagemErro);
        }
    }

    private String mensagemErro(Response response) {
        try {
            if (Objects.isNull(response.body())){
                return "";
            }
            return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao ler os bytes do corpo de resposta", e);
        }
    }
}