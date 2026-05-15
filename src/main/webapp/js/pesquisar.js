async function pesquisarProduto(evento) {
    evento.preventDefault();

    let nomePesquisado = document.getElementById("nomeDigitado").value;

    let url = "http://localhost:8080/api/pesquisa?nome_produto=" + encodeURIComponent(nomePesquisado);

    try {
        const response = await fetch(url);

        const dados = await response.json();

        let divProduto = document.getElementById("produto");

        if (dados !== null) {
            divProduto.innerHTML = `
           <h3 class="titulo-pesquisa">Resultado da busca</h3>
        <div class="resultado-grid">
        <div class="item-pesquisa">
            <span class="item-label">Produto</span>
            <span class="item-valor">${dados.nome_produto}</span>
        </div>
        <div class="item-pesquisa">
            <span class="item-label">Fabricante</span>
            <span class="item-valor">${dados.fabricante}</span>
        </div>
        <div class="item-pesquisa">
            <span class="item-label">Marca</span>
            <span class="item-valor">${dados.marca}</span>
        </div>
        <div class="item-pesquisa">
            <span class="item-label">Quantidade</span>
            <span class="item-valor">${dados.quantidade} un.</span>
        </div>
        <div class="item-pesquisa">
            <span class="item-label">Valor</span>
            <span class="item-valor">R$ ${dados.valor}</span>
        </div>
        <div class="item-pesquisa">
            <span class="item-label">Total no estoque</span>
            <span class="item-valor">R$ ${dados.total}</span>
        </div>
            <div class="item-pesquisa">
                <button id="botao-editar" class="btn-editar">Editar</button>
            </div>
    </div>
        `;
            
        const model = document.getElementById("formCadastro");
        const cancelar = document.getElementsByClassName("btn-cancelar");

        document.getElementById("botao-editar").addEventListener("click", () => {
            model.style.display = 'flex';

            cancelar.addEventListener("click", () => {
                event.preventDefault();
                model.style.display = 'none';
            });

        });
        } else {
            alert("Dados não encontrado!!");
        }

    } catch (erro) {
        console.log("Erro na busca de dados", erro);
    }
}

document.getElementById("btn-pesquisar").addEventListener("click", pesquisarProduto);
