package model;

import java.time.LocalDateTime;

public class Transacoes {

    // Atributos
    private int idTransacao;
    private TipoTransacao tipoTransacao;
    private double valor;
    private LocalDateTime dataTransacao;
    private int contaOrigemId;
    private Integer contaDestinoId; 
    private StatusTransacao status;
    private String descricaoTrancacao;
    
    

    // Enum para tipos de transacoes
    public enum TipoTransacao {
        DEPOSITO, SAQUE, TRANSFERENCIA
    }

    // Enum para status 
    public enum StatusTransacao {
        CONCLUIDA, PENDENTE, CANCELADA
    }

    // Construtor
    public Transacoes(int idTransacao, TipoTransacao tipoTransacao, double valor,
                      LocalDateTime dataTransacao, int contaOrigemId, Integer contaDestinoId,
                      StatusTransacao status) {
        this.idTransacao = idTransacao;
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.dataTransacao = dataTransacao;
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.status = status;
    }

    // Getters e Setters
    public int getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(int idTransacao) {
        this.idTransacao = idTransacao;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDateTime dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public int getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(int contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }

    public Integer getContaDestinoId() {
        return contaDestinoId;
    }

    public void setContaDestinoId(Integer contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transacoes{" +
                "idTransacao=" + idTransacao +
                ", tipoTransacao=" + tipoTransacao +
                ", valor=" + valor +
                ", dataTransacao=" + dataTransacao +
                ", contaOrigemId=" + contaOrigemId +
                ", contaDestinoId=" + contaDestinoId +
                ", status=" + status +
                '}';
    }
}
