package interfaces;

import exceptions.PedidoInvalidoException;

public interface Processavel {
    void processarPedido() throws PedidoInvalidoException;
 
    double calcularValorFinal();
}