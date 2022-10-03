package com.example.requisicaoemprestimo.integracao;

import com.example.requisicaoemprestimo.domain.models.Emprestimo;
import com.example.requisicaoemprestimo.domain.models.ResultadoAnalise;
import com.example.requisicaoemprestimo.domain.models.ResultadoTesouraria;
import com.example.requisicaoemprestimo.domain.ports.IAnaliseProxy;
import com.example.requisicaoemprestimo.domain.ports.ITesourariaProxy;
import com.example.requisicaoemprestimo.domain.usecases.RequisicaoEmprestimoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequisicaoEmprestimoUseCaseTests {

     IAnaliseProxy iAnaliseProxy;
     ITesourariaProxy iTesourariaProxy;

     @BeforeEach
    public void setup(){
        iAnaliseProxy = mock(IAnaliseProxy.class);
        iTesourariaProxy = mock(ITesourariaProxy.class);
    }

    @Test
    public void test1(){
        String[] resultado = {"Emprestimo Aprovado com Sucesso!"};
        when(iAnaliseProxy.solicitarAnaliseDeCredito(any(Emprestimo.class))).thenReturn(new ResultadoAnalise(true, resultado));
        when(iTesourariaProxy.solicitarLiberacaoDaTesouraria(any(Emprestimo.class))).thenReturn(new ResultadoTesouraria(true, "Limite de Crédito Ideal"));
        RequisicaoEmprestimoUseCase requisicao = new RequisicaoEmprestimoUseCase(iAnaliseProxy, iTesourariaProxy);
        Emprestimo emprestimo = requisicao.executar(UUID.randomUUID(), 100, 12);
        assertEquals(true, emprestimo.isEmprestimoFoiAprovado());
    }

    @Test
    public void test2(){
        //TODO Fazer um teste caminho INFELIZ IAnaliseProxy retornando uma Análise reprovada
        String[] resultado = {"Emprestimo Reprovado"};
        when(iAnaliseProxy.solicitarAnaliseDeCredito(any(Emprestimo.class))).thenReturn(new ResultadoAnalise(false, resultado));
        when(iTesourariaProxy.solicitarLiberacaoDaTesouraria(any(Emprestimo.class))).thenReturn(new ResultadoTesouraria(true, "Limite de Crédito Ideal"));
        RequisicaoEmprestimoUseCase requisicao = new RequisicaoEmprestimoUseCase(iAnaliseProxy, iTesourariaProxy);
        Emprestimo emprestimo = requisicao.executar(UUID.randomUUID(), 100, 12);
        assertEquals(false, emprestimo.isEmprestimoFoiAprovado());
    }

    @Test
    public void test3(){
        String[] resultado = {"Emprestimo Aprovado com Sucesso!"};
        when(iAnaliseProxy.solicitarAnaliseDeCredito(any(Emprestimo.class))).thenReturn(new ResultadoAnalise(true, resultado));
        when(iTesourariaProxy.solicitarLiberacaoDaTesouraria(any(Emprestimo.class))).thenReturn(new ResultadoTesouraria(false, "Limite de Crédito Insuficiente"));
        RequisicaoEmprestimoUseCase requisicao = new RequisicaoEmprestimoUseCase(iAnaliseProxy, iTesourariaProxy);
        Emprestimo emprestimo = requisicao.executar(UUID.randomUUID(), 100, 12);
        assertEquals(false, emprestimo.isEmprestimoFoiAprovado());
    }
}