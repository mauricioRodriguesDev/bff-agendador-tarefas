package com.javanauta.bffagendadortarefas.controller;


import com.javanauta.bffagendadortarefas.business.TarefasService;
import com.javanauta.bffagendadortarefas.business.dto.in.TarefasDTORequest;
import com.javanauta.bffagendadortarefas.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendadortarefas.business.enums.StatusNotificacaoEnum;
import com.javanauta.bffagendadortarefas.controller.docs.ITarefasApi;
import com.javanauta.bffagendadortarefas.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class TarefasController implements ITarefasApi {

    private final TarefasService tarefasService;

    @PostMapping
    public ResponseEntity<TarefasDTOResponse> gravarTarefas(@RequestBody TarefasDTORequest dto,
                                                            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefasService.gravarTarefa(token, dto));
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<TarefasDTOResponse>> buscarTarefasAgendadasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefasService.buscarTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token));
    }


    @GetMapping
    public ResponseEntity<List<TarefasDTOResponse>> buscarTarefasPorEmail(
            @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefasService.buscarTarefasPorEmail(token));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaTarefasPorId(@RequestParam("id") String id,
                                                   @RequestHeader(value = "Authorization", required = false) String token) {

        tarefasService.deletaTarefaPorId(id, token);

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<TarefasDTOResponse> alteraStatusTarefa(@RequestParam("status") StatusNotificacaoEnum status,
                                                                 @RequestParam("id") String id,
                                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefasService.alteraStatusTarefas(status, id, token));

    }

    @PutMapping
    public ResponseEntity<TarefasDTOResponse> udateTarefas(@RequestBody TarefasDTORequest dto,
                                                           @RequestParam("id") String id,
                                                           @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(tarefasService.updateTarefas(dto, id, token));
    }

}
