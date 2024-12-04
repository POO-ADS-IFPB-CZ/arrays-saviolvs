package view;

import model.Categoria;
import model.Item;
import model.Produto;
import model.Venda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Venda vendaAtual = new Venda();
    private static List<Venda> historicoVendas = new ArrayList<>();
    private static DefaultTableModel tabelaModel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Vendas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel painelPrincipal = new JPanel(new BorderLayout());

        tabelaModel = new DefaultTableModel(new String[]{"Código", "Descrição", "Quantidade", "Subtotal"}, 0);
        JTable tabelaItens = new JTable(tabelaModel);
        JScrollPane scrollPane = new JScrollPane(tabelaItens);

        // Botões
        JPanel painelBotoes = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar Item");
        JButton btnRemover = new JButton("Remover Item");
        JButton btnFinalizar = new JButton("Finalizar Venda");
        JButton btnHistorico = new JButton("Histórico de Vendas");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnFinalizar);
        painelBotoes.add(btnHistorico);

        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        frame.add(painelPrincipal);

        btnAdicionar.addActionListener(e -> adicionarItem());
        btnRemover.addActionListener(e -> removerItem());
        btnFinalizar.addActionListener(e -> finalizarVenda());
        btnHistorico.addActionListener(e -> exibirHistorico());

        // Exibir a janela
        frame.setVisible(true);
    }

    private static void adicionarItem() {
        JTextField campoCodigo = new JTextField();
        JTextField campoDescricao = new JTextField();
        JTextField campoPreco = new JTextField();
        JTextField campoQuantidade = new JTextField();
        JComboBox<Categoria> comboCategoria = new JComboBox<>(Categoria.values());

        Object[] campos = {
                "Código:", campoCodigo,
                "Descrição:", campoDescricao,
                "Preço:", campoPreco,
                "Quantidade:", campoQuantidade,
                "Categoria:", comboCategoria
        };

        int resultado = JOptionPane.showConfirmDialog(null, campos, "Adicionar Item", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int codigo = Integer.parseInt(campoCodigo.getText());
                String descricao = campoDescricao.getText();
                double preco = Double.parseDouble(campoPreco.getText());
                double quantidade = Double.parseDouble(campoQuantidade.getText());
                Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

                Produto produto = new Produto(codigo, descricao, preco, categoria);
                Item item = new Item(codigo, produto, quantidade);
                vendaAtual.adicionarItem(item);
                atualizarTabela();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha os campos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void removerItem() {
        String codigoStr = JOptionPane.showInputDialog("Digite o código do item a ser removido:");

        if (codigoStr != null) {
            try {
                int codigo = Integer.parseInt(codigoStr);
                boolean sucesso = vendaAtual.removerItem(codigo);

                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Item removido com sucesso!");
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(null, "Item não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Código inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void finalizarVenda() {
        if (vendaAtual.getItens().length > 0) {
            historicoVendas.add(vendaAtual);
            JOptionPane.showMessageDialog(null, "Venda finalizada! Total: R$ " + vendaAtual.getTotal());
            vendaAtual = new Venda(); // Resetar venda
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum item na venda!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void exibirHistorico() {
        StringBuilder historicoTexto = new StringBuilder();

        int vendaNumero = 1;
        for (Venda venda : historicoVendas) {
            historicoTexto.append("Venda #").append(vendaNumero++).append("\n");

            for (Item item : venda.getItens()) {
                historicoTexto.append("Código: ").append(item.getCodigo())
                        .append(", Descrição: ").append(item.getProduto().getDescricao())
                        .append(", Quantidade: ").append(item.getQuantidade())
                        .append(", Subtotal: R$ ").append(item.getSubtotal()).append("\n");
            }

            historicoTexto.append("Total: R$ ").append(venda.getTotal()).append("\n\n");
        }

        JTextArea areaHistorico = new JTextArea(historicoTexto.toString());
        areaHistorico.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaHistorico);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "Histórico de Vendas", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void atualizarTabela() {
        tabelaModel.setRowCount(0);
        for (Item item : vendaAtual.getItens()) {
            tabelaModel.addRow(new Object[]{
                    item.getCodigo(),
                    item.getProduto().getDescricao(),
                    item.getQuantidade(),
                    item.getSubtotal()
            });
        }
    }
}
