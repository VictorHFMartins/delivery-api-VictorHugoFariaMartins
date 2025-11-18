package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.TelefoneRequest;
import com.deliverytech.delivery.api.dto.TelefoneResponse;
import com.deliverytech.delivery.api.dto.TelefoneUpdateRequest;
import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.model.Telefone;

public interface TelefoneService {

    Telefone buscarOuCriarTelefone(TelefoneRequest telefoneDto);

    void remover(Long usuarioId, Long telefoneId);

    TelefoneResponse criar(Long usuarioId, TelefoneRequest telefoneDto);

    TelefoneResponse atualizar(Long usuarioId, Long telefoneId, TelefoneUpdateRequest telefoneUpdateDto);

    TelefoneResponse buscarTelefonePorId(long telefoneId);

    List<TelefoneResponse> listarNumerosAtivos();

    List<TelefoneResponse> listarTodos();

    List<TelefoneResponse> buscarPorFiltro(String ddd, Long usuarioId, String numero,  TipoUsuario tipoUsuario, TipoTelefone tipoTelefone);

}
