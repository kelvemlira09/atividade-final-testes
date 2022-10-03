package com.example.requisicaoemprestimo.unitarios;

import com.example.requisicaoemprestimo.domain.models.Emprestimo;
import com.example.requisicaoemprestimo.domain.models.Parcela;
import com.example.requisicaoemprestimo.domain.models.ResultadoAnalise;
import com.example.requisicaoemprestimo.domain.models.ResultadoTesouraria;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.example.requisicaoemprestimo.unitarios.EmprestimoTestsFixture.buildResultadoAnalise;
import static com.example.requisicaoemprestimo.unitarios.EmprestimoTestsFixture.buildResultadoTesouraria;
import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoTests {

    EmprestimoTestsFixture fixture;
    @Test
    public void testParcelas() {
        Emprestimo emprestimo = fixture.emprestimoAprovado(100, 12);
        Optional<Parcela[]> p = emprestimo.getParcelas();
        assertTrue(emprestimo.getParcelas().isPresent());
        assertEquals(106.50, emprestimo.getValorTotalEmprestimo());
        assertEquals(12, emprestimo.getQuantidadeParcelasSolicitadas());
    }
    @Test
    public void testeAnaliseDeCreditoInvalida(){
        Emprestimo emprestimo = fixture.emprestimoAprovado(100,12);
        String[] analise = {"Analise Reprovada"};
        ResultadoAnalise resultado = buildResultadoAnalise(false, analise);
        emprestimo.setResultadoAnalise(resultado);
        assertEquals(false, emprestimo.isEmprestimoFoiAprovado());

    }

    @Test
    public void testeResultadoDaTesourariaInvalida(){
        Emprestimo emprestimo = fixture.emprestimoAprovado(100,12);
        ResultadoTesouraria resultado = buildResultadoTesouraria(false,"limite insuficiente", emprestimo);
        emprestimo.setResultadoTesouraria(resultado);
        assertEquals(false, emprestimo.isEmprestimoFoiAprovado());

    }

}
