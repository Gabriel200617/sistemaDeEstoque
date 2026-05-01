package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CadastroProdutoModel;
import model.TotalModel;

public class CadastroProdutosDAO {

    public boolean salvar(CadastroProdutoModel produto, TotalModel total) {
        String sql = "INSERT INTO produtos "
                + "(codigo_barras,nome_produto,fabricante,marca,data_fabricacao,data_vencimento,quantidade,valor,total,status)"
                + "VALUE(?,?,?,?,?,?,?,?,?,?)";

        String sqlTotal = "UPDATE total SET totalValor = ?, quantidadeEstoque = ?,EstoqueBaixo = ? WHERE id = 1";
        
       try (Connection conn = ConnectionFactory.getConnection()) {
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtProd = conn.prepareStatement(sql)) {
                stmtProd.setString(1, produto.getCodigoBarras());
                stmtProd.setString(2, produto.getNomeProduto());
                stmtProd.setString(3, produto.getFabricante());
                stmtProd.setString(4, produto.getMarca());
                stmtProd.setDate(5, java.sql.Date.valueOf(produto.getDataFabricacao()));
                stmtProd.setDate(6, java.sql.Date.valueOf(produto.getDataVencimento()));
                stmtProd.setLong(7, produto.getQuantidade());
                stmtProd.setString(8, produto.getValor()); 
                stmtProd.setString(9, produto.getTotal());
                stmtProd.setString(10, produto.getStatus());
                stmtProd.executeUpdate();
            }

            try (PreparedStatement stmtTotal = conn.prepareStatement(sqlTotal)) {
                stmtTotal.setString(1, total.getTotal()); 
                stmtTotal.setLong(2, total.getQuantidadeTotal());
                stmtTotal.setLong(3, total.getEstoqueBaixo());
                
                stmtTotal.executeUpdate();
            }

            conn.commit();
            System.out.println("Sucesso: Produto e Resumo atualizados!");
            return true;

        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public List<CadastroProdutoModel> listarComFiltro(String nome, String tipo, String data) {
        List<CadastroProdutoModel> lista = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder("SELECT * FROM produtos WHERE 1=1");
        
        if(nome != null && !nome.isEmpty()){
            sql.append(" AND LOWER(nome_produto) LIKE ?");
        }
        if(tipo != null && !tipo.isEmpty()){
            sql.append(" AND status = ?");
        }
        if(data != null && !data.isEmpty()){
            sql.append(" AND data_fabricacao = ?");
        }

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString());) {
            int index = 1;
            
            if(nome != null && !nome.isEmpty()){
                stmt.setString(index++, "%" + nome.toLowerCase() + "%");
            }
            if(tipo != null && !tipo.isEmpty()){
                stmt.setString(index++, tipo);
            }
            if(data != null && !data.isEmpty()){
                stmt.setString(index++, data);
            }
                
            ResultSet rs = stmt.executeQuery(); 

            while (rs.next()) {
                CadastroProdutoModel p = new CadastroProdutoModel();
                
                p.setCodigoBarras(rs.getString("codigo_barras"));
                p.setNomeProduto(rs.getString("nome_produto"));
                p.setFabricante(rs.getString("fabricante"));
                p.setMarca(rs.getString("marca"));
                p.setDataFabricacao(rs.getDate("data_fabricacao").toLocalDate().toString());
                p.setDataVencimento(rs.getDate("data_vencimento").toLocalDate().toString());
                p.setQuantidade(rs.getLong("quantidade"));
                p.setValor(rs.getString("valor"));
                p.setTotal(rs.getString("total"));
                p.setStatus(rs.getString("status"));

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
}
