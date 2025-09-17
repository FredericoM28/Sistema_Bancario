package model;

import java.time.LocalDate;

public class Conta { 
    private int idConta;
    private int numeroConta;
    private TipoConta tipoConta;
    private int saldo;
    private LocalDate dataAbertura;
    private StatusConta status; 
    private Cliente clienteId;
    private int niubConta;
    private int nib;

    public enum TipoConta {
        POUPANCA, CORRENTE, DEBITO
    }

    public enum StatusConta {
        ATIVA, INATIVA
    }

    public Conta(int idConta, int numeroConta, TipoConta tipoConta,
                 Cliente clienteId, int niubConta, int nib) {
        this.idConta = idConta;
        this.numeroConta = numeroConta;
        this.tipoConta = tipoConta;
        this.saldo = 0;
        this.dataAbertura = LocalDate.now();
        this.status = StatusConta.INATIVA;   // <- começa inativa
        this.clienteId = clienteId;
        this.niubConta = niubConta;
        this.nib = nib;
    }

    public void depositar(int valor) {
        this.saldo += valor;
        if (this.status == StatusConta.INATIVA && this.saldo >= 500) {
            this.status = StatusConta.ATIVA; // <- ativa ao atingir 500
        }
    }

    // Getters e Setters
    public int getIdConta() { return idConta; }
    public int getNumeroConta() { return numeroConta; }
    public TipoConta getTipoConta() { return tipoConta; }
    public int getSaldo() { return saldo; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public StatusConta getStatus() { return status; }
    public Cliente getClienteId() { return clienteId; }
    public int getNiubConta() { return niubConta; }
    public int getNib() { return nib; }

    public void setStatus(StatusConta status) { this.status = status; }
}
