package model;

import java.util.Arrays;

public class Venda {

    private Item[] itens;
    private int quantidadeItens;

    public Venda() {
        itens = new Item[3];
        quantidadeItens = 0;
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < quantidadeItens; i++) {
            total += itens[i].getSubtotal();
        }
        return total;
    }

    public boolean adicionarItem(Item item) {
        if (quantidadeItens == itens.length) {
            itens = Arrays.copyOf(itens, itens.length + 3);
        }
        itens[quantidadeItens++] = item;
        return true;
    }

    public boolean removerItem(int codigo) {
        for (int i = 0; i < quantidadeItens; i++) {
            if (itens[i].getCodigo() == codigo) {
                for (int j = i; j < quantidadeItens - 1; j++) {
                    itens[j] = itens[j + 1];
                }
                itens[quantidadeItens - 1] = null;
                quantidadeItens--;
                return true;
            }
        }
        return false;
    }

    public Item[] getItens() {
        return Arrays.copyOf(itens, quantidadeItens);
    }
}
