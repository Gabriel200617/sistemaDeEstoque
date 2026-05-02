async function resumoGerenciamento() {
    try {
        const response = await fetch("http://localhost:8080/api/gerenciamento");
        const dados = await response.json();

        document.getElementById("itens-quantidade").innerHTML = dados.totalItens;   
        document.getElementById("itens-estoque").innerHTML = dados.estoqueBaixo;    
        document.getElementById("valor-dinamico").innerHTML = dados.valorTotal;     

    } catch (erro) {
        console.log("Erro na consulta dos dados", erro);
    }
}

window.onload = () => {
    resumoGerenciamento();
};