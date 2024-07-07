package br.gov.sp.fatec.service.impl;

import br.gov.sp.fatec.domain.mapper.CarroMapper;
import br.gov.sp.fatec.domain.request.CarroRequest;
import br.gov.sp.fatec.domain.request.CarroUpdateRequest;
import br.gov.sp.fatec.domain.response.CarroResponse;
import br.gov.sp.fatec.repository.CarroRepository;
import br.gov.sp.fatec.service.CarroService;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarroServiceImpl implements CarroService {

    private final CarroRepository carroRepository;
    private final CarroMapper carroMapper;

    @Override
    public CarroResponse save(CarroRequest carroRequest) {
        return this.carroMapper.map(this.carroRepository.save(this.carroMapper.map(carroRequest)));
    }

    @Override
    public CarroResponse findById(Long id) {
        return this.carroMapper.map(this.carroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado."))
        );
    }

    @Override
    public List<CarroResponse> findAll() {
        return this.carroRepository.findAll().stream()
                .map(carroMapper::map).toList();
    }

    @Override
    public void updateById(Long id, CarroUpdateRequest carroUpdateRequest) {
        final var saved = this.carroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado."));

        final var newcar = this.carroMapper.map(carroUpdateRequest);
        newcar.setId(saved.getId());

        this.carroRepository.save(newcar);
    }

    @Override
    public void deleteById(Long id) {
        this.carroRepository.deleteById(id);
    }
}
