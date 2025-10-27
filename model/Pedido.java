package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String id;
    private String nomeCliente;
    private String emailCliente;
    private StatusPedido status;
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {}

    public Pedido(String id, String nomeCliente, String emailCliente) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.status = StatusPedido.NOVO;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }

    public double calcularTotal() {
        return itens.stream().mapToDouble(i -> i.getProduto().getPreco() * i.getQuantidade()).sum();
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Cliente: %s | Status: %s | Total: %.2f",
                id, nomeCliente, status.getDescricao(), calcularTotal());
    }

    public static class ItemPedido {
        private Produto produto;
        private int quantidade;

        public ItemPedido() {}
        public ItemPedido(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public Produto getProduto() { return produto; }
        public void setProduto(Produto produto) { this.produto = produto; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

        @Override
        public String toString() {
            return String.format("%s | Qtde: %d | Subtotal: %.2f",
                    produto.getNome(), quantidade, produto.getPreco() * quantidade);
        }
    }
}
