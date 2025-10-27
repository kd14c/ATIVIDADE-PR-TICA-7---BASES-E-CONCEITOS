package view;

import model.Produto;
import model.Pedido;
import model.StatusPedido;
import controller.SistemaController;

import java.util.Scanner;

public class Main {
    private SistemaController controller = new SistemaController();
    private Scanner sc = new Scanner(System.in);

    public void exibirMenu() {
        int opc;
        do {
            System.out.println("\n1 - Listar Produtos");
            System.out.println("2 - Adicionar Produto");
            System.out.println("3 - Buscar Produto por ID");
            System.out.println("4 - Criar Pedido");
            System.out.println("5 - Listar Pedidos");
            System.out.println("6 - Atualizar Status Pedido");
            System.out.println("7 - Ver Detalhes Pedido");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            opc = sc.nextInt();
            sc.nextLine();

            switch (opc) {
                case 1 -> controller.listarProdutos();
                case 2 -> adicionarProduto();
                case 3 -> buscarProduto();
                case 4 -> criarPedido();
                case 5 -> controller.listarPedidos();
                case 6 -> atualizarStatus();
                case 7 -> verDetalhesPedido();
                case 0 -> System.out.println("Saindo");
                default -> System.out.println("Opção inválida!");
            }
        } while (opc != 0);
    }

    private void adicionarProduto() {
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("Nome: "); String nome = sc.nextLine();
        System.out.print("Preço: "); double preco = sc.nextDouble();
        System.out.print("Quantidade: "); int qtde = sc.nextInt();
        sc.nextLine();
        controller.adicionarProduto(new Produto(id, nome, preco, qtde));
    }

    private void buscarProduto() {
        System.out.print("ID do produto: ");
        String id = sc.nextLine();
        controller.buscarProdutoPorId(id).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Produto não encontrado!")
        );
    }

    private void criarPedido() {
        System.out.print("ID do pedido: "); String idPedido = sc.nextLine();
        System.out.print("Nome do cliente: "); String nome = sc.nextLine();
        String email;
        do {
            System.out.print("Email do cliente: "); email = sc.nextLine();
            if (!controller.validarEmail(email)) System.out.println("Email inválido!");
        } while (!controller.validarEmail(email));

        Pedido pedido = new Pedido(idPedido, nome, email);
        String resp;
        do {
            System.out.print("ID do produto: "); String idProduto = sc.nextLine();
            controller.buscarProdutoPorId(idProduto).ifPresentOrElse(produto -> {
                System.out.print("Quantidade: "); int qtde = sc.nextInt(); sc.nextLine();
                if (qtde <= produto.getQuantidade()) {
                    produto.setQuantidade(produto.getQuantidade() - qtde);
                    pedido.getItens().add(new Pedido.ItemPedido(produto, qtde));
                } else {
                    System.out.println("Quantidade insuficiente em estoque!");
                }
            }, () -> System.out.println("Produto não encontrado!"));
            System.out.print("Adicionar mais itens? (S/N): "); resp = sc.nextLine().toUpperCase();
        } while (resp.equals("S"));
        controller.criarPedido(pedido);
    }

    private void atualizarStatus() {
        System.out.print("ID do pedido: "); String id = sc.nextLine();
        controller.buscarPedidoPorId(id).ifPresentOrElse(pedido -> {
            System.out.println("Status disponíveis:");
            for (StatusPedido s : StatusPedido.values()) System.out.println(s.ordinal() + " - " + s.getDescricao());
            System.out.print("Escolha o status: "); int escolha = sc.nextInt(); sc.nextLine();
            if (escolha >= 0 && escolha < StatusPedido.values().length) {
                controller.atualizarStatusPedido(id, StatusPedido.values()[escolha]);
            } else {
                System.out.println("Opção inválida!");
            }
        }, () -> System.out.println("Pedido não encontrado!"));
    }

    private void verDetalhesPedido() {
        System.out.print("ID do pedido: "); String id = sc.nextLine();
        controller.verDetalhesPedido(id);
    }

    public static void main(String[] args) {
        new Main().exibirMenu();
    }
}
