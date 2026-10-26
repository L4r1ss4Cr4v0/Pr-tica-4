import enums.Categoria;
import exceptions.ValorInvalidoException;
import interfaces.Entregavel;

public class PedidoEletronico extends Pedido implements Entregavel {
    public PedidoEletronico(int id, String nomeCliente, double valor)
            throws ValorInvalidoException {
        super(id, nomeCliente, valor, Categoria.ELETRONICO);
    }
 
    @Override
    public double calcularValorFinal() {
        if (getValor() > 100) {
            return getValor() * 0.90;
        }
        return getValor();
    }
 
    @Override
    public double calcularFrete() {
        return 20 + getValor() * 0.10;
    }
}
