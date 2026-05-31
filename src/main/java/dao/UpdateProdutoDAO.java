package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.CadastroProdutoModel;

public class UpdateProdutoDAO {
    
    public boolean atualizar(CadastroProdutoModel produto) {
        String sql = """
                     UPDATE produtos SET
                        nome_produto = ?,
                        fabricante = ?,
                        marca = ?,
                        data_fabricacao = ?,
                        data_vencimento = ?,
                        quantidade = ?,
                        valor = ?,
                        total = ?,
                        status = ?
                     WHERE codigo_barras = ?
                     """;
        
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
                
            stmt.setString(1, produto.getNomeProduto());
            stmt.setString(2, produto.getFabricante());
            stmt.setString(3, produto.getMarca());
            stmt.setDate(4, java.sql.Date.valueOf(produto.getDataFabricacao()));
            stmt.setDate(5, java.sql.Date.valueOf(produto.getDataVencimento()));
            stmt.setLong(6, produto.getQuantidade());
            stmt.setString(7, produto.getValor());
            stmt.setString(8, produto.getTotal());
            stmt.setString(9, produto.getStatus());
            stmt.setString(10, produto.getCodigoBarras());
            
            return stmt.executeUpdate() > 0;
            
        
    }catch(SQLException e){
        e.printStackTrace();
        return false;
        }
   }
}
     

 