async function carregarEstoque() {
    try {
        const response = await fecth("http://localhost:8080/api/estoque");
        const dados = await response.json();
        
        const tabela = document.getElementById("corpoTabela");
        tabela.innerHTML = "";
        
        dados.forEach(item => {
            const linha = `
                    <tr>
                        <td>${item.codigoBarras}</td>
                        <td>${item.nomeProduto}</td>
                        <td>${item.fabricante}</td>
                        <td>${item.marca}</td>
                        <td>${item.dataFabricacao}</td>
                        <td>${item.dataVencimento}</td>
                        <td>${item.quantidade}</td>
                        <td>${item.valor}</td>
                        <td>${item.total}</td>
                        <td>${item.status}</td>
                    </tr>
                    `;
            tabela.innerHTML += linha;
        });
    } catch (erro) {
        console.log("Erro ao carregar os produtos", erro);
    }
}

window.onload = carregarEstoque;