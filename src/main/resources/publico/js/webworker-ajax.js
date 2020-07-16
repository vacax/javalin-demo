/**
 * Web Worker para realizar llamadas Ajax utilizando una librería
 * que simplifica la llamada vía el objeto XMLHttpRequest.
 */
//importando el script
importScripts('/js/axios.min.js'); // JQuery trabaja con el DOM no puede ser utilizada
// ver en https://github.com/axios/axios

//eventos recuperados entre la ventana principal y el worker.
this.addEventListener('message', function(e) {

    //la información la tenemos en la propiedad data.
    var data = e.data;

    //
    switch (data.cmd) {
        case 'fecha':
            console.log("Buscando la fecha desde el servidor...");
            axios.get('/fecha')
                .then(function (response) {
                    // handle success
                    console.log("Respuesta:");
                    console.log(response);
                    //enviando la información a la venta principal
                    postMessage({'cmd': 'respuesta', 'data': response.data});
                })
                .catch(function (error) {
                    // handle error
                    console.log("Error:");
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
            break;
        default:
            this.postMessage('{"tipo": "msg","mensaje" : "Mensaje no procesado: '+data.msg+'}');
    };
}, false);