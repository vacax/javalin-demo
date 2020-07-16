//haciendo la referencia al workers
this.addEventListener('message', function(e) {

    //la información la tenemos en la da.
    var data = e.data;

    //
    switch (data.cmd) {
        case 'inicio':
            console.log("Presiono inicio..");
            setInterval(function () {  //proceso que se ejecuta periodicamente.
                this.postMessage('{"tipo": "fecha","mensaje" : "La fecha es: '+new Date().toUTCString()+'}');
            }, 1000);
            
            this.postMessage('{"tipo": "msg","mensaje" : "WORKER INICIADO: '+data.msg+'}');

            break;
        case 'parada':
            this.postMessage('{"tipo": "msg","mensaje" : "WORKER: '+data.msg+'}');
            this.close(); // Termina el worker.
            break;
        default:
            this.postMessage('{"tipo": "msg","mensaje" : "Mensaje no procesado: '+data.msg+'}');
    };
}, false);