package controller;

import model.Produto;
import model.Pedido;
import model.StatusPedido;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SistemaController {
    private Map<String, Produto> produtos = new HashMap<>();
    private Map<String, Pedido> pedidos = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final String ARQ_PRODUTOS = "produtos.json";
    private final String ARQ_PEDIDOS = "pedidos.json";
    private final Pattern patternEmail = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public SistemaController() {
        carregarProdutos();
        carregarPedidos();
    }

    public void adicionarProduto(Produto p) {
        produtos.merge(p.getId(), p, (existente, novo) -> {
            existente.setQuantidade(existente.getQuantidade() + novo.getQuantidade());
            return existente;
        });
        salvarProdutos();
    }

    public void listarProdutos() {
        produtos.values().forEach(System.out::println);
    }

    public Optional<Produto> buscarProdutoPorId(String id) {
        return Optional.ofNullable(produtos.get(id));
    }

    public boolean validarEmail(String email) {
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }

    public void criarPedido(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
        salvarPedidos();
    }

    public void listarPedidos() {
        pedidos.values().forEach(System.out::println);
    }

    public Optional<Pedido> buscarPedidoPorId(String id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    public void atualizarStatusPedido(String id, StatusPedido status) {
        buscarPedidoPorId(id).ifPresent(p -> {
            p.setStatus(status);
            salvarPedidos();
        });
    }

    public void verDetalhesPedido(String id) {
        buscarPedidoPorId(id).ifPresentOrElse(p -> {
            System.out.println("ID: " + p.getId());
            System.out.println("Cliente: " + p.getNomeCliente());
            System.out.println("Email: " + p.getEmailCliente());
            System.out.println("Status: " + p.getStatus().getDescricao());
            System.out.println("Itens:");
            p.getItens().forEach(i -> System.out.println("  " + i));
            System.out.printf("Total: %.2f%n", p.calcularTotal());
        }, () -> System.out.println("Pedido não encontrado!"));
    }

    private void carregarProdutos() {
        try {
            File file = new File(ARQ_PRODUTOS);
            if (file.exists()) {
                JsonNode root = mapper.readTree(file);
                for (JsonNode node : root) {
                    Produto p = mapper.treeToValue(node, Produto.class);
                    produtos.put(p.getId(), p);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void salvarProdutos() {
        try {
            mapper.writeValue(new File(ARQ_PRODUTOS), produtos.values());
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }

    private void carregarPedidos() {
        try {
            File file = new File(ARQ_PEDIDOS);
            if (file.exists()) {
                JsonNode root = mapper.readTree(file);
                for (JsonNode node : root) {
                    Pedido p = mapper.treeToValue(node, Pedido.class);
                    pedidos.put(p.getId(), p);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void salvarPedidos() {
        try {
            mapper.writeValue(new File(ARQ_PEDIDOS), pedidos.values());
        } catch (IOException e) {
            System.out.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }
}
