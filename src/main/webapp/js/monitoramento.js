const BASE = "http://localhost:8080";
let todoHistorico = [];
let paginaAtual = 1;
const ITENS_POR_PAGINA = 10;

function formatarData(iso) {
    const d = new Date(iso.replace("T", " "));
    return d.toLocaleDateString("pt-BR");
}

function formatarDataSimples(iso) {
    if (!iso) return "—";
    const [ano, mes, dia] = iso.split("-");
    return `${dia}/${mes}/${ano}`;
}

function renderizarBaixo(lista) {
    const tbody = document.querySelector("#tabelaBaixo tbody");

    if (!lista.length) {
        tbody.innerHTML = `<tr><td colspan="5" class="vazio">Nenhum item com estoque baixo.</td></tr>`;
        return;
    }

    tbody.innerHTML = "";
    lista.forEach(p => {
        const linha = `
            <tr>
                <td>${p.nomeProduto}</td>
                <td>${p.codigoBarras}</td>
                <td>${p.quantidade} un.</td>
                <td><span class="badge badge-baixo">${p.quantidade <= 2 ? "crítico" : "baixo"}</span></td>
                <td>${formatarDataSimples(p.dataVencimento)}</td>
            </tr>
        `;
        tbody.innerHTML += linha;
    });
}

function renderizarHistorico(lista) {
    const tbody = document.querySelector("#tabelaHistorico tbody");

    if (!lista.length) {
        tbody.innerHTML = `<tr><td colspan="6" class="vazio">Nenhuma movimentação encontrada.</td></tr>`;
        atualizarPaginacao(0);
        return;
    }

    // Fatia os 10 itens da página atual
    const inicio = (paginaAtual - 1) * ITENS_POR_PAGINA;
    const fim    = inicio + ITENS_POR_PAGINA;
    const paginada = lista.slice(inicio, fim);

    tbody.innerHTML = "";
    paginada.forEach(m => {
        const linha = `
            <tr>
                <td>${m.nomeProduto}</td>
                <td>${m.codigoBarras}</td>
                <td><span class="badge badge-${m.tipoMovimentacao}">${m.tipoMovimentacao}</span></td>
                <td>${m.quantidade} un.</td>
                <td>R$ ${parseFloat(m.valor).toFixed(2)}</td>
                <td>${formatarData(m.dataHora)}</td>
            </tr>
        `;
        tbody.innerHTML += linha;
    });

    atualizarPaginacao(lista.length);
}

function atualizarPaginacao(totalItens) {
    const totalPaginas = Math.ceil(totalItens / ITENS_POR_PAGINA);

    document.getElementById("btnAnterior").disabled = paginaAtual === 1;
    document.getElementById("btnProximo").disabled  = paginaAtual >= totalPaginas;
    document.getElementById("infoPagina").innerHTML =
        totalItens > 0 ? `Página ${paginaAtual} de ${totalPaginas}` : "";
}

function listaFiltrada() {
    const tipo = document.getElementById("filtroTipo").value;
    return tipo
        ? todoHistorico.filter(m => m.tipoMovimentacao === tipo)
        : todoHistorico;
}

function calcularMetricas(hist, baixo) {
    const hoje = new Date().toLocaleDateString("pt-BR");

    const entradas = hist.filter(m =>
        m.tipoMovimentacao === "entrada" &&
        new Date(m.dataHora.replace("T", " ")).toLocaleDateString("pt-BR") === hoje
    ).length;

    const saidas = hist.filter(m =>
        m.tipoMovimentacao === "saida" &&
        new Date(m.dataHora.replace("T", " ")).toLocaleDateString("pt-BR") === hoje
    ).length;

    const ultima = hist.length ? formatarData(hist[0].dataHora) : "—";

    document.getElementById("metricBaixo").innerHTML    = baixo.length;
    document.getElementById("metricEntradas").innerHTML = entradas;
    document.getElementById("metricSaidas").innerHTML   = saidas;
    document.getElementById("metricUltima").innerHTML   = ultima;
}

async function carregarBaixo() {
    try {
        const response = await fetch(`${BASE}/api/gerenciamento`);
        const dados = await response.json();

        const itensBaixo = dados.estoqueBaixoItens || [];
        renderizarBaixo(itensBaixo);
        calcularMetricas(todoHistorico, itensBaixo);

    } catch (erro) {
        console.log("Erro ao carregar itens com estoque baixo", erro);
    }
}

async function carregarHistorico() {
    try {
        const response = await fetch(`${BASE}/api/monitoramento`);
        todoHistorico = await response.json();

        paginaAtual = 1;
        renderizarHistorico(listaFiltrada());

    } catch (erro) {
        console.log("Erro ao carregar histórico de movimentações", erro);
    }
}

document.getElementById("filtroTipo").addEventListener("change", function () {
    paginaAtual = 1;
    renderizarHistorico(listaFiltrada());
});

document.getElementById("btnAnterior").addEventListener("click", function () {
    if (paginaAtual > 1) {
        paginaAtual--;
        renderizarHistorico(listaFiltrada());
    }
});

document.getElementById("btnProximo").addEventListener("click", function () {
    const totalPaginas = Math.ceil(listaFiltrada().length / ITENS_POR_PAGINA);
    if (paginaAtual < totalPaginas) {
        paginaAtual++;
        renderizarHistorico(listaFiltrada());
    }
});

async function carregarMonitoramento() {
    await carregarHistorico();
    await carregarBaixo();
}