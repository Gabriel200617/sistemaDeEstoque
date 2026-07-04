package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MonitoramentoModel;

public class MonitoramentoDAO {
    
    public String registrarMonitoramento(MonitoramentoModel monitorar){

    String sql = """
                 INSERT INTO monitoramento(codigo_barras, nome_produto, tipo_movimentacao, quantidade, valor, data_hora)
                 VALUES(?, ?, ?, ?, ?, NOW())
                 """;
    try(Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, monitorar.getCodigoBarras());
            stmt.setString(2, monitorar.getNomeProduto());
            stmt.setString(3, monitorar.getTipoMovimentacao());
            stmt.setLong(4, monitorar.getQuantidade());
            stmt.setString(5, monitorar.getValor());
            stmt.executeUpdate();
            return null; // null = sucesso

    }catch(SQLException e){
        e.printStackTrace();
        return e.getMessage(); // aqui vem a mensagem do SIGNAL, se a trigger disparou
    }
}
    
    public List<MonitoramentoModel> listarTodos() {
        List<MonitoramentoModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM monitoramento ORDER BY data_hora DESC";
        
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                lista.add(mapear(rs));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public List<MonitoramentoModel> listarPorItem(String tipo){
        List<MonitoramentoModel> lista = new ArrayList<>();
        String sql = "SELECT * FROM monitoramento WHERE tipo_movimentacao = ? ORDER BY data_hora DESC";
        
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                lista.add(mapear(rs));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lista;
    }
    
    private MonitoramentoModel mapear(ResultSet rs) throws Exception {
        MonitoramentoModel model = new MonitoramentoModel();
        model.setId(rs.getInt("id"));
        model.setCodigoBarras(rs.getString("codigo_barras"));
        model.setNomeProduto(rs.getString("nome_produto"));
        model.setTipoMovimentacao(rs.getString("tipo_movimentacao"));
        model.setQuantidade(rs.getLong("quantidade"));
        model.setValor(rs.getString("valor"));
        model.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime().toString());
        return model;
    }
    
    
}
