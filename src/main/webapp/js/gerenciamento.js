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

async function renderizarGrafico() {
    try {
        const response = await fetch("http://localhost:8080/api/grafico");
        const dadosGrafico = await response.json(); 

        const chartContainer = document.getElementById("meu-grafico");
        chartContainer.innerHTML = ""; 

        // Adiciona a Legenda no título dinamicamente se não existir
        const tituloContainer = document.querySelector(".grafico-title");
        if (!document.querySelector(".legenda-grafico")) {
            tituloContainer.innerHTML = `
                Nível de Estoque por Produto
                <div class="legenda-grafico">
                    <div class="legenda-item"><span class="legenda-cor legenda-entrada"></span> Entrada</div>
                    <div class="legenda-item"><span class="legenda-cor legenda-saida"></span> Saída</div>
                </div>
            `;
        }

        if (!dadosGrafico || dadosGrafico.length === 0) {
            chartContainer.innerHTML = "<p style='color: var(--gray-dark); width: 100%; text-align: center; margin-top: 50px;'>Sem dados para exibir.</p>";
            return;
        }

        let maiorQtd = 0;
        dadosGrafico.forEach(item => {
            if (item.entrada > maiorQtd) maiorQtd = item.entrada;
            if (item.saida > maiorQtd) maiorQtd = item.saida;
        });

        // Monta as barras
        dadosGrafico.forEach((item, index) => {
            const wrapper = document.createElement("div");
            wrapper.className = "bar-wrapper";

            const group = document.createElement("div");
            group.className = "bar-group";

            const alturaEntrada = maiorQtd > 0 ? ((item.entrada / maiorQtd) * 85) : 0;
            const alturaSaida = maiorQtd > 0 ? ((item.saida / maiorQtd) * 85) : 0;

            // Barra Verde
            if (item.entrada > 0) {
                const barEntrada = document.createElement("div");
                barEntrada.className = "bar entrada";
                barEntrada.setAttribute("data-value", item.entrada);
                group.appendChild(barEntrada);
                
                // Dispara a animação com delay escalonado (cada barra sobe uma após a outra)
                setTimeout(() => {
                    barEntrada.style.height = alturaEntrada + "%";
                }, 150 * index); 
            }

            // Barra Vermelha
            if (item.saida > 0) {
                const barSaida = document.createElement("div");
                barSaida.className = "bar saida";
                barSaida.setAttribute("data-value", item.saida);
                group.appendChild(barSaida);
                
                // Dispara a animação
                setTimeout(() => {
                    barSaida.style.height = alturaSaida + "%";
                }, 150 * index);
            }

            const label = document.createElement("span");
            label.className = "bar-label";
            label.innerText = item.nome;
            label.title = item.nome; 

            wrapper.appendChild(group);
            wrapper.appendChild(label);
            chartContainer.appendChild(wrapper);
        });

    } catch (erro) {
        console.log("Erro ao carregar o gráfico", erro);
    }
}

window.onload = () => {
    resumoGerenciamento();
    renderizarGrafico(); // Adicionamos a chamada para o gráfico iniciar ao carregar a página
};

