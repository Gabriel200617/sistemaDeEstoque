/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author EJADEN0058
 */
public class ConnectionFactory {

    //Dados da conexão
        private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
        private static final String URL =
            "jdbc:mysql://localhost:3306/estoque_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        private static final String USER = "root";
        private static final String PASSWORD = "cimatec";
    //Métodos
        
        public static Connection getConnection(){
            Connection con = null;
            
            try {
                Class.forName(DRIVER);
                con = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Banco de dados conectado.");
            } catch (Exception e) {
                System.out.println("Banco de dados não conectado");
                e.printStackTrace();
            }
            
            return con;
             
        }
}