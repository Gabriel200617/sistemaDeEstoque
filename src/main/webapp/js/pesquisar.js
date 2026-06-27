async function pesquisarProduto(evento) {
    evento.preventDefault();

    let nomePesquisado = document.getElementById("nomeDigitado").value;

    let url = "http://localhost:8080/api/pesquisa?codigo_barras=" + encodeURIComponent(nomePesquisado);

    try {
        const response = await fetch(url);

        const dados = await response.json();

        let divProduto = document.getElementById("produto");

        if (response.ok && dados && dados.nome_produto) {
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
            <div class="item-pesquisa">
           <button id="botao-deletar" class="btn-deletar">Deletar</button>
            </div> 
    </div>
        `;

            function mostrarResultado() {
                const model = document.getElementById("formCadastro");
                model.style.display = 'flex';

                document.getElementById("nomeProduto").value = dados.nome_produto;
                document.getElementById("fabricante").value = dados.fabricante;
                document.getElementById("quantidade").value = dados.quantidade;
                document.getElementById("valor").value = dados.valor;
                document.getElementById("total").value = dados.total;
                document.getElementById("marca").value = dados.marca;
                document.getElementById("codigoBarras").value = dados.codigo_barras;
                document.getElementById("status").value = dados.status;
                document.getElementById("dataFabricacao").value = dados.data_fabricacao;
                document.getElementById("dataVencimento").value = dados.data_vencimento;

            }

            document.getElementById("botao-editar").addEventListener("click", mostrarResultado);

            const btnCancelar = document.querySelector(".btn-cancelar");
            if (btnCancelar) {
                btnCancelar.addEventListener("click", (evento) => {
                    evento.preventDefault();
                    document.getElementById("formCadastro").style.display = 'none';
                });
            }
            
            document.getElementById("botao-deletar").addEventListener("click", () => {
            deleteProduto(dados.codigo_barras, dados.nome_produto, divProduto);
        });

        } else {
            alert("Produtos não encontrado no Estoque!!");
            divProduto.innerHTML = "";
        }

    } catch (erro) {
        divProduto.innerHTML = "";
        alert("Produto não encontrado ou dados inválidos!");
    }
}

async function deleteProduto(codigoBarras, nomeProduto, divProduto) {
    divProduto.innerHTML = `
        <div class="modal-confirmacao">
            <p>Tem certeza que deseja deletar <strong>${nomeProduto}</strong>?</p>
            <p class="aviso-delete">Esta ação não pode ser desfeita.</p>
            <div class="acoes-confirmacao">
                <button id="btn-confirmar-delete" class="btn-deletar">Confirmar</button>
                <button id="btn-cancelar-delete" class="btn-cancelar-delete">Cancelar</button>
            </div>
        </div>
    `;

    document.getElementById("btn-cancelar-delete").addEventListener("click", () => {
        document.getElementById("btn-pesquisar").click();
    });

    document.getElementById("btn-confirmar-delete").addEventListener("click", async () => {
        try {
            const response = await fetch("http://localhost:8080/api/produto?codigo_barras=" + encodeURIComponent(codigoBarras),
                    {method: "DELETE"});

            const resultado = await response.json();

            if (response.ok) {
                divProduto.innerHTML = `<p class="msg-sucesso">✔ Produto deletado com sucesso!</p>`;
                document.getElementById("nomeDigitado").value = "";
            } else {
                divProduto.innerHTML = `<p class="msg-erro">✘ Erro: ${resultado.erro}</p>`;
            }
        } catch (erro) {
            console.error("Erro ao deletar:", erro);
            divProduto.innerHTML = `<p class="msg-erro">✘ Erro de comunicação com o servidor.</p>`;
        }
    });
}

document.getElementById("btn-pesquisar").addEventListener("click", pesquisarProduto);
