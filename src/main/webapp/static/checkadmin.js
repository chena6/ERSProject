function checkAdmin(){
	let xhttp = new XMLHttpRequest();
	xhttp.onload = (resp) => {
		if (xhttp.status === 200){
			window.location = xhttp.response;
		} else {
			alert('could not validate role');
		}
	}
	
	xhttp.open('GET', '../checkAdmin');
	xhttp.send();
}