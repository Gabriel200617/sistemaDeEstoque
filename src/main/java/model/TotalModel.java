package model;

public class TotalModel {
    private int id;
    private String total;
    private long quantidadeTotal;
    private long estoqueBaixo;
    
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
   
    public long getQuantidadeTotal() {
        return quantidadeTotal;
    }

   
    public void setQuantidadeTotal(long quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }

   
    public long getEstoqueBaixo() {
        return estoqueBaixo;
    }

    
    public void setEstoqueBaixo(long estoqueBaixo) {
        this.estoqueBaixo = estoqueBaixo;
    }
}
