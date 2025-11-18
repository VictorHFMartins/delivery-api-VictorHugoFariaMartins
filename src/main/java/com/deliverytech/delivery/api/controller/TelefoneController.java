package com.deliverytech.delivery.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.api.dto.TelefoneRequest;
import com.deliverytech.delivery.api.dto.TelefoneResponse;
import com.deliverytech.delivery.api.dto.TelefoneUpdateRequest;
import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.services.TelefoneService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/telefones")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class TelefoneController {

    private final TelefoneService telefoneService;

    @PostMapping("/{id}")
    public ResponseEntity<TelefoneResponse> cadastrar(Long usuarioId, TelefoneRequest dto) {
        TelefoneResponse telefone = telefoneService.criar(usuarioId, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(telefone.id()).toUri();
        return ResponseEntity.created(location).body(telefone);
    }

    @PutMapping("/usuario/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<TelefoneResponse> alterar(Long usuarioId, Long telefoneId, TelefoneUpdateRequest telefoneUpdateDto) {
        TelefoneResponse telefone = telefoneService.atualizar(usuarioId, telefoneId, telefoneUpdateDto);
        return ResponseEntity.ok(telefone);
    }

    @DeleteMapping("usuario/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<Void> remover(Long usuarioId, Long telefoneId) {
        telefoneService.remover(usuarioId, telefoneId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TelefoneResponse>> listarTodos() {
        List<TelefoneResponse> telefones = telefoneService.listarTodos();
        return ResponseEntity.ok(telefones);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TelefoneResponse>> buscarPorFiltro(
            @RequestParam(required = false) String ddd,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) TipoUsuario tipoUsuario,
            @RequestParam(required = false) TipoTelefone tipoTelefone) {

        List<TelefoneResponse> telefones = telefoneService.buscarPorFiltro(ddd, usuarioId, numero, tipoUsuario, tipoTelefone);
        return ResponseEntity.ok(telefones);
    }
}
