async function pesquisarProduto(evento) {
    evento.preventDefault();

    let nomePesquisado = document.getElementById("nomeDigitado").value;

    let url = "http://localhost:8080/api/pesquisa?nome_produto=" + encodeURIComponent(nomePesquisado);

    try {
        const response = await fetch(url);

        const dados = await response.json();

        let divProduto = document.getElementById("produto");

        divProduto.innerHTML = `
           <h3 class="titulo-pesquisa">Resultado da Busca:</h3>     
           <p class="item-pesquisa">Produto: ${dados.nome}</p>
           <p class="item-pesquisa">Fabricante: ${dados.fabricante}</p>
           <p class="item-pesquisa">Marca: ${dados.marca}</p>
           <p class="item-pesquisa">Quantidade: ${dados.quantidade}</p>
           <p class="item-pesquisa">Valor: ${dados.valor}</p>
           <p class="item-pesquisa">Total no Estoque: ${dados.total}</p>
        `;


    } catch (erro) {
        console.log("Erro na busca de dados", erro);
    }
}

document.getElementById("btn-pesquisar").addEventListener("click", pesquisarProduto);
