async function validarLogin() {
    try{
       const res = await fetch("http://localhost:8080/api/perfil");
       const dado = await res.json();
       
       console.log("Perfil FRONT: ", dado.perfil);
       
       if(!dado.perfil || dado.perfil.toLowserCase() !== "admin") {
           document.getElementsByClassName(".btn-menu").style.display = "none";
       }
    }catch(e){
        console.error("Erro ao verificar o perfil.", e);
    }
}

validarLogin();