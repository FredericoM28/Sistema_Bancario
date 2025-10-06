package model;



import java.util.ArrayList;
import java.util.List;

public class Banco {
    private String nome = "Nexus BANK";
    private double capital;
    private double lucro;
    private List<Conta> contas;

    private double lucroTaxas; // lucro apenas de transações
public void registrarLucroTaxa(double valor) {
    lucroTaxas += valor;
    registrarLucro(valor); // aumenta capital e lucro total
}
//public double getLucroTaxas() { return lucroTaxas; }


    public Banco(String nome, double capitalInicial) {
        this.nome = nome;
        this.capital = capitalInicial;
        this.lucro = 0;
        this.contas = new ArrayList<>();
    }

    public String getNome() {
    return nome;
}

public double getLucroTaxas() {
    return lucroTaxas;
}


    public void adicionarConta(Conta conta) {
        contas.add(conta);
    }

    public void registrarLucro(double valor) {
        lucro += valor;
        capital += valor;
    }

    public double getCapital() {
        return capital;
    }

    public double getLucro() {
        return lucro;
    }

    public List<Conta> getContas() {
        return contas;
    }

    
}
