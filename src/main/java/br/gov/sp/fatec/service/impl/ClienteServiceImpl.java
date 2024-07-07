package br.gov.sp.fatec.service.impl;

import br.gov.sp.fatec.domain.mapper.ClienteMapper;
import br.gov.sp.fatec.domain.request.ClienteRequest;
import br.gov.sp.fatec.domain.request.ClienteUpdateRequest;
import br.gov.sp.fatec.domain.response.ClienteResponse;
import br.gov.sp.fatec.repository.ClienteRepository;
import br.gov.sp.fatec.service.ClienteService;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public ClienteResponse save(ClienteRequest clienteRequest) {
        var cliente = this.clienteMapper.map(clienteRequest);

        return this.clienteMapper.map(cliente);
    }

    @Override
    public ClienteResponse findById(Long id) {
        return clienteMapper.map(clienteRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("usuario não encontrado!")));
    }

    @Override
    public List<ClienteResponse> findAll() {
        return this.clienteRepository.findAll().stream()
                .map(this.clienteMapper::map)
                .toList();
    }

    @Override
    public void updateById(Long id, ClienteUpdateRequest clienteUpdateRequest) {
        var cliente = this.clienteRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("usuario não encontrado!"));
        var atualizado = this.clienteMapper.map(clienteUpdateRequest);
        atualizado.setId(cliente.getId());
        this.clienteRepository.save(atualizado);
    }

    @Override
    public void deleteById(Long id) {
        this.clienteRepository.deleteById(id);
    }
}
