package model;

public enum StatusPedido {
    NOVO("Novo pedido"),
    PROCESSANDO("Pedido em processamento"),
    ENVIADO("Pedido enviado"),
    ENTREGUE("Pedido entregue"),
    CANCELADO("Pedido cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
