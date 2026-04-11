package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
    
public class ConnectionFactory {

        private static final String URL = ("DB_URL");
        private static final String USER = ("DB_USER");
        private static final String PASSWORD = ("DB_PASS");
        private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

        public static Connection getConnection(){
            Connection con = null;
            
            try {
                if(URL == null || USER == null || PASSWORD == null){
                    System.out.println("Variável de ambiente com problema");
                    return null;
                }

                Class.forName(DRIVER);

                con = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Banco de dados conectado.");

                }catch(ClassNotFoundException e) {
                    System.out.println("Erro no JDBC.");
                    e.printStackTrace();
                }catch(SQLException e){
                    System.out.println("Erro no SQL.");
                    e.printStackTrace();
                }catch (Exception e) {
                    System.out.println("Banco de dados não conectado");
                    e.printStackTrace();
                }
            
            return con;
             
        }
}