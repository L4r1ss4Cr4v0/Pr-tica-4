import enums.Categoria;
import enums.StatusPedido;
import exceptions.PedidoInvalidoException;
import exceptions.ValorInvalidoException;
import interfaces.Processavel;

public abstract class Pedido implements Processavel {
    private int id;
    private String nomeCliente;
    private double valor;
    private StatusPedido status;
    private Categoria categoria;
 
    public Pedido(int id, String nomeCliente, double valor, Categoria categoria)
            throws ValorInvalidoException {
        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do pedido deve ser maior que zero.");
        }
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.valor = valor;
        this.categoria = categoria;
        this.status = StatusPedido.PENDENTE;
    }
 
    public int getId() {
        return id;
    }
 
    public String getNomeCliente() {
        return nomeCliente;
    }
 
    public double getValor() {
        return valor;
    }
 
    public StatusPedido getStatus() {
        return status;
    }
 
    public Categoria getCategoria() {
        return categoria;
    }
 
    public void alterarStatus(StatusPedido novoStatus) throws PedidoInvalidoException {
        if (status == StatusPedido.ENTREGUE) {
            throw new PedidoInvalidoException(
                    "Pedido #" + id + " ja foi entregue e nao pode mudar de status.");
        }
        this.status = novoStatus;
    }
 
    @Override
    public void processarPedido() throws PedidoInvalidoException {
        if (status == StatusPedido.CANCELADO) {
            throw new PedidoInvalidoException(
                    "Pedido #" + id + " esta cancelado e nao pode ser processado.");
        }
        if (status == StatusPedido.ENTREGUE) {
            throw new PedidoInvalidoException(
                    "Pedido #" + id + " ja foi entregue e nao pode mudar de status.");
        }
        if (status == StatusPedido.PENDENTE) {
            status = StatusPedido.PROCESSANDO;
        }
    }
 
    @Override
    public String toString() {
        return String.format("Pedido #%d | Cliente: %s | Categoria: %s | Valor: R$ %.2f | "
                        + "Valor final: R$ %.2f | Status: %s",
                id, nomeCliente, categoria, valor, calcularValorFinal(), status);
    }
}
