package com.javanauta.bffagendadortarefas.business;


import com.javanauta.bffagendadortarefas.business.dto.in.TarefasDTORequest;
import com.javanauta.bffagendadortarefas.business.dto.out.TarefasDTOResponse;
import com.javanauta.bffagendadortarefas.business.enums.StatusNotificacaoEnum;
import com.javanauta.bffagendadortarefas.infrastructure.client.TarefasClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    public final TarefasClient client;


    public TarefasDTOResponse gravarTarefa(String token, TarefasDTORequest dto) {

        return client.gravarTarefas(dto, token);
    }

    public List<TarefasDTOResponse> buscarTarefasAgendadasPorPeriodo(LocalDateTime dataInicial,
                                                                     LocalDateTime dataFinal,
                                                                     String token) {
        return client.buscarTarefasAgendadasPorPeriodo(dataInicial, dataFinal, token);

    }

    public List<TarefasDTOResponse> buscarTarefasPorEmail(String token) {

        return client.buscarTarefasPorEmail(token);

    }

    public void deletaTarefaPorId(String id, String token) {

        client.deletaTarefasPorId(id, token);

    }

    public TarefasDTOResponse alteraStatusTarefas(StatusNotificacaoEnum status, String id, String token) {

        return client.alteraStatusTarefa(status, id, token);

    }

    public TarefasDTOResponse updateTarefas(TarefasDTORequest dto, String id, String token) {

        return client.udateTarefas(dto, id, token);
    }


}


