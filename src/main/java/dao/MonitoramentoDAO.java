package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.MonitoramentoModel;

public class MonitoramentoDAO {
    
    public boolean registrarMonitoramento(MonitoramentoModel monitorar){
                                          
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
                        return true;
                        
                }catch(SQLException e){
                    e.printStackTrace();
                    return false;
                }
    }
}
