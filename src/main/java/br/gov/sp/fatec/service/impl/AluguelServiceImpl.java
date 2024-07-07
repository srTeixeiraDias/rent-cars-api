package br.gov.sp.fatec.service.impl;

import br.gov.sp.fatec.domain.enums.AluguelStatus;
import br.gov.sp.fatec.domain.enums.CarroStatus;
import br.gov.sp.fatec.domain.mapper.AluguelMapper;
import br.gov.sp.fatec.domain.request.AluguelRequest;
import br.gov.sp.fatec.domain.request.AluguelUpdateRequest;
import br.gov.sp.fatec.domain.response.AluguelResponse;
import br.gov.sp.fatec.repository.AluguelRepository;
import br.gov.sp.fatec.repository.CarroRepository;
import br.gov.sp.fatec.repository.ClienteRepository;
import br.gov.sp.fatec.service.AluguelService;

import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AluguelServiceImpl implements AluguelService {

    private final AluguelRepository aluguelRepository;
    private final AluguelMapper aluguelMapper;

    private final CarroRepository carroRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public AluguelResponse save(AluguelRequest aluguelRequest) {
        var carro = this.carroRepository.findById(aluguelRequest.carroId())
                .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado "));

        var cliente = this.clienteRepository.findById(aluguelRequest.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado "));

        if (carro.getStatus() != CarroStatus.DISPONIVEL)
            throw new IllegalArgumentException("Carro não está disponivel");

        if (aluguelRequest.dataInicio().after(aluguelRequest.dataFim()))
            throw new IllegalArgumentException("Data de início não pode ser após a data de fim.");

        final var date = new Date();
        if (aluguelRequest.dataInicio().before(date))
            throw new IllegalArgumentException("Data de início não pode ser anterior a data atual.");

        final var aluguel = this.aluguelMapper.map(aluguelRequest);
        aluguel.setCarro(carro);
        aluguel.setCliente(cliente);

        return this.aluguelMapper.map(this.aluguelRepository.save(aluguel));
    }
    @Override
    public AluguelResponse findById(Long id) {
        return aluguelMapper.map(this.aluguelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("aluguel não encontrado")));
    }

    @Override
    public List<AluguelResponse> findAll() {
        return this.aluguelRepository.findAll().stream()
                .map(this.aluguelMapper::map)
                .toList();
    }

    @Override
    public void updateById(Long id, AluguelUpdateRequest aluguelUpdateRequest) {
        var aluguel =this.aluguelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("aluguel não encontrado"));
        var atualizado = this.aluguelMapper.map(aluguelUpdateRequest);

        atualizado.setId(aluguel.getId());

        this.aluguelRepository.save(atualizado);

    }

    @Override
    public void deleteById(Long id) {
        final var aluguel = this.aluguelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado."));

        if (aluguel.getStatus() == AluguelStatus.ATIVO)
            throw new IllegalArgumentException("Aluguel ativo não pode ser deletado.");

        this.aluguelRepository.deleteById(id);
    }
}
