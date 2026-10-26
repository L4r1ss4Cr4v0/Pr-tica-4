import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import exceptions.PedidoInvalidoException;
import exceptions.ValorInvalidoException;
import interfaces.Entregavel;
import enums.StatusPedido;

public class App {
    private static final List<Pedido> pedidos = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static int proximoId = 1;
 
    public static void main(String[] args) {
        int opcao = -1;
        do {
            System.out.println("\n===== SISTEMA GERENCIADOR DE PEDIDOS =====");
            System.out.println("1 - Cadastrar pedido");
            System.out.println("2 - Alterar status de um pedido");
            System.out.println("3 - Processar pedidos");
            System.out.println("4 - Remover cancelados");
            System.out.println("5 - Exibir lista de pedidos");
            System.out.println("6 - Exibir pedidos entregaveis");
            System.out.println("0 - Encerrar");
            System.out.print("Opcao: ");
 
            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opcao invalida! Digite um numero.");
                continue;
            }
 
            switch (opcao) {
                case 1: 
                    cadastrarPedido();
                case 2:
                    alterarStatus();
                case 3:
                    processarPedidos();
                case 4:
                    removerCancelados();
                case 5:
                    exibirPedidos();
                case 6:
                    exibirEntregaveis();
                default:
                    System.out.println("Opcao invalida!");
            }
        } while (opcao != 0);
 
        sc.close();
    }
 
    private static void cadastrarPedido() {
        try {
            System.out.print("Nome do cliente: ");
            String nome = sc.nextLine().trim();
 
            System.out.print("Valor do pedido: ");
            double valor = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
 
            System.out.println("Categoria: 1-Eletronico  2-Roupa  3-Alimento");
            System.out.print("Escolha: ");
            int cat = Integer.parseInt(sc.nextLine().trim());
 
            Pedido p;
            switch (cat) {
                case 1 -> p = new PedidoEletronico(proximoId, nome, valor);
                case 2 -> p = new PedidoRoupa(proximoId, nome, valor);
                case 3 -> p = new PedidoAlimento(proximoId, nome, valor);
                default -> {
                    System.out.println("Categoria invalida!");
                    return;
                }
            }
            pedidos.add(p);
            proximoId++;
            System.out.println("Pedido cadastrado com sucesso! ID: " + p.getId());
        } catch (ValorInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro: entrada numerica invalida.");
        }
    }
 
    private static Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
 
    private static void alterarStatus() {
        try {
            System.out.print("ID do pedido: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Pedido p = buscarPorId(id);
            if (p == null) {
                System.out.println("Pedido nao encontrado.");
                return;
            }
 
            System.out.println("Novo status:");
            StatusPedido[] valores = StatusPedido.values();
            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + " - " + valores[i]);
            }
            System.out.print("Escolha: ");
            int escolha = Integer.parseInt(sc.nextLine().trim());
            if (escolha < 1 || escolha > valores.length) {
                System.out.println("Status invalido!");
                return;
            }
 
            p.alterarStatus(valores[escolha - 1]);
            System.out.println("Status alterado para " + p.getStatus());
        } catch (PedidoInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro: entrada numerica invalida.");
        }
    }
 
    private static void processarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        Iterator<Pedido> it = pedidos.iterator();
        while (it.hasNext()) {
            Pedido p = it.next();
            try {
                p.processarPedido();
                System.out.println("Pedido #" + p.getId() + " processado. Status: " + p.getStatus());
            } catch (PedidoInvalidoException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
 
    private static void removerCancelados() {
        int removidos = 0;
        Iterator<Pedido> it = pedidos.iterator();
        while (it.hasNext()) {
            if (it.next().getStatus() == StatusPedido.CANCELADO) {
                it.remove();
                removidos++;
            }
        }
        System.out.println(removidos + " pedido(s) cancelado(s) removido(s).");
    }
 
    private static void exibirPedidos() {
        boolean achou = false;
        Iterator<Pedido> it = pedidos.iterator();
        while (it.hasNext()) {
            Pedido p = it.next();
            if (p.getStatus() != StatusPedido.CANCELADO) {
                System.out.println(p);
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Nenhum pedido para exibir.");
        }
    }
 
    private static void exibirEntregaveis() {
        boolean achou = false;
        for (Pedido p : pedidos) {
            if (p instanceof Entregavel e && p.getStatus() != StatusPedido.CANCELADO) {
                System.out.println(p + String.format(" | Frete: R$ %.2f", e.calcularFrete()));
                achou = true;
            }
        }
        if (!achou) {
            System.out.println("Nenhum pedido entregavel.");
        }
    }
}

