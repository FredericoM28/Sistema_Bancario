package DAO;

import model.Cliente;
import model.Conta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    public void salvar(Conta conta) {
        String sql = "INSERT INTO conta (idCliente, numeroConta, tipoConta, saldo, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, conta.getIdCliente());
            stmt.setInt(2, conta.getNumeroConta());
            stmt.setString(3, conta.getTipoConta().name());
            stmt.setDouble(4, conta.getSaldo());
            stmt.setString(5, conta.getStatus().name());

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) conta.setIdConta(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Conta buscarPorNumero(String numeroConta) {
    String sql = "SELECT * FROM conta WHERE numeroConta=?";
    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, numeroConta);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // primeiro busca o cliente
                Cliente cliente = new ClienteDAO().buscarPorId(rs.getInt("idCliente")); 

                Conta conta = new Conta(
                    rs.getInt("idConta"),
                    rs.getInt("numeroConta"),
                    Conta.TipoConta.valueOf(rs.getString("tipoConta")),
                    cliente,
                    rs.getInt("niubConta"),
                    rs.getInt("nib")
                );

                // seta saldo e status
                conta.setSaldo(rs.getDouble("saldo"));
                conta.setStatus(Conta.StatusConta.valueOf(rs.getString("status")));

                return conta;
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    public List<Conta> listarTodas() {
        List<Conta> lista = new ArrayList<>();
        String sql = "SELECT * FROM conta";
        
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Busca o cliente correspondente
                Cliente cliente = new ClienteDAO().buscarPorId(rs.getInt("idCliente"));

                // Cria a conta usando o construtor correto
                Conta conta = new Conta(
                    rs.getInt("idConta"),
                    rs.getInt("numeroConta"),
                    Conta.TipoConta.valueOf(rs.getString("tipoConta")),
                    cliente,
                    rs.getInt("niubConta"),
                    rs.getInt("nib")
                );

                // Define saldo e status
                conta.setSaldo(rs.getDouble("saldo"));
                conta.setStatus(Conta.StatusConta.valueOf(rs.getString("status")));

                lista.add(conta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public void atualizar(Conta conta) {
        String sql = "UPDATE conta SET idCliente=?, numeroConta=?, tipoConta=?, saldo=?, status=? WHERE idConta=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, conta.getIdCliente());
            stmt.setInt(2, conta.getNumeroConta());
            stmt.setString(3, conta.getTipoConta().name());
            stmt.setDouble(4, conta.getSaldo());
            stmt.setString(5, conta.getStatus().name());
            stmt.setInt(6, conta.getIdConta());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM conta WHERE idConta=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
