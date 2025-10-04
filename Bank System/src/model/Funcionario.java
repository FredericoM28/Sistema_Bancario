package model;

import java.time.LocalDate;

public class Funcionario {
    

    private int  idFuncionario;
    private String nomeCompletoFunc;
    //usuario
    private String senhaFunc;
    private String cargo;
    private String nivelAcesso;
    private LocalDate dataAdmissao;
    private Boolean statusFunc; //(ativo/inativo


    public Funcionario(int idFuncionario, String nomeCompletoFunc, String senhaFunc, String cargo,
            String nivelAcesso, LocalDate dataAdmissao, Boolean statusFunc) {
        this.idFuncionario = idFuncionario;
        this.nomeCompletoFunc = nomeCompletoFunc;
        this.senhaFunc = senhaFunc;
        this.cargo = cargo;
        this.nivelAcesso = nivelAcesso;
        this.dataAdmissao = dataAdmissao;
        this.statusFunc = statusFunc;
    }

    public Funcionario(String nomeCompletoFunc, String cargo) {
        this.nomeCompletoFunc = nomeCompletoFunc;
        this.cargo = cargo;
    }


    public int getIdFuncionario() {
        return idFuncionario;
    }


    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }


    public String getNomeCompletoFunc() {
        return nomeCompletoFunc;
    }


    public void setNomeCompletoFunc(String nomeCompletoFunc) {
        this.nomeCompletoFunc = nomeCompletoFunc;
    }


    public String getSenhaFunc() {
        return senhaFunc;
    }


    public void setSenhaFunc(String senhaFunc) {
        this.senhaFunc = senhaFunc;
    }


    public String getCargo() {
        return cargo;
    }


    public void setCargo(String cargo) {
        this.cargo = cargo;
    }


    public String getNivelAcesso() {
        return nivelAcesso;
    }


    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }


    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }


    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }


    public Boolean getStatusFunc() {
        return statusFunc;
    }


    public void setStatusFunc(Boolean statusFunc) {
        this.statusFunc = statusFunc;
    }

    
    
}
