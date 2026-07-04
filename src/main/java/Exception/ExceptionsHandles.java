package Exception;

import java.time.LocalDate;

public class ExceptionsHandles {
    
    public static void validarData(LocalDate dataVenci, LocalDate dataFab){
        if(dataVenci.isBefore(dataFab)){
           throw new ValidarDataExceptions("erro ao cadastrar, data de vencimento anterior à data fabricação");
        }
    }
    
    public static void validarQuantidade(Long quantidade){
        if(quantidade <= 0){
           throw new ValidarQuantidadeExceptions("erro ao cadastrar, quantidade menor ou igual a zero");
        }
    }
    
    public static void validarTotal(double total){
        if(total <= 0){
           throw new ValidarTotalExceptions("erro ao cadastrar, total menor ou igual a zero");
        }
    }
}
