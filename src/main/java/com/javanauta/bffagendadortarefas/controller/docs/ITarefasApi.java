package com.javanauta.bffagendadortarefas.controller.docs;

import com.javanauta.bffagendadortarefas.business.dto.in.TarefasDTORequest;
import com.javanauta.bffagendadortarefas.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendadortarefas.business.enums.StatusNotificacaoEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Tarefas", description = "Cadastro de Tarefas de Usuarios")
public interface ITarefasApi {


    @Operation(summary = "Salvar Tarefas de Usuario ", description = "Criar uma nova tarefa")
    @ApiResponse(responseCode = "200", description = "Tarefa salva com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PostMapping
    ResponseEntity<TarefasDTOResponse> gravarTarefas(TarefasDTORequest dto, String token);


    @Operation(summary = "Listar Tarefas por periodo", description = "Listar tarefas cadastradas por periodo")
    @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping("/eventos")
    ResponseEntity<List<TarefasDTOResponse>> buscarTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal, String token);


    @Operation(summary = "Listar tarefas por email de Usuario", description = "Listar tarefas cadastradas por usuario")
    @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @GetMapping
    ResponseEntity<List<TarefasDTOResponse>> buscarTarefasPorEmail(String token);


    @Operation(summary = "Deletar Tarefas por ID", description = "Deleta tarefas cadastradas por ID")
    @ApiResponse(responseCode = "200", description = "Tarefa deletada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @DeleteMapping
    ResponseEntity<Void> deletaTarefasPorId(String id, String token);

    @Operation(summary = "Alterar status de tarefas", description = "Alterar status de tarefas cadastradas")
    @ApiResponse(responseCode = "200", description = "Satatus da Tarefa alterada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PatchMapping
    ResponseEntity<TarefasDTOResponse> alteraStatusTarefa(StatusNotificacaoEnum status, String id, String token);


    @Operation(summary = "Atualizar dados Tarefas", description = "Atualizar dados uma tarefa")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    @PutMapping
    ResponseEntity<TarefasDTOResponse> udateTarefas(TarefasDTORequest dto, String id, String token);

}
