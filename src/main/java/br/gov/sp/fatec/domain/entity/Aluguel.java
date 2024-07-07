package br.gov.sp.fatec.domain.entity;

import br.gov.sp.fatec.domain.enums.AluguelStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Aluguel {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private AluguelStatus status;

    @Column(name= "dt_inicio")
    private Date dtInicio;

    @Column(name= "dt_fim")
    private Date dtFim;

    @Column(name= "valor")
    private Double valor;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_carro")
    private Carro carro;
}
