package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.TotalModel;

public class GerenciamentoDAO {

    public TotalModel buscarTotalAtual() {
        TotalModel total = new TotalModel();
        String sql = "SELECT totalValor, quantidadeEstoque, EstoqueBaixo FROM total WHERE id = 1";

        try (Connection conn = ConnectionFactory.getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql); 
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total.setTotal(rs.getString("totalValor"));
                total.setQuantidadeTotal(rs.getLong("quantidadeEstoque"));
                total.setEstoqueBaixo(rs.getLong("EstoqueBaixo"));
            } else {
                total.setTotal("0.0");
                total.setQuantidadeTotal(0L);
                total.setEstoqueBaixo(0L);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar totais: " + e.getMessage());
            // Inicializa mesmo em caso de erro para evitar Null no Controller
            total.setTotal("0.0");
        }
        return total;
    }
}
