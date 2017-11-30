function logout(){
	let xhttp = new XMLHttpRequest();
	xhttp.onload = (resp) => {
		if (xhttp.status === 200){
			alert('successfully signed out');
			window.location= '../home';
		} else {
			alert('unable to sign out');
		}
	}
	xhttp.open('POST', '../logout');
	xhttp.send();
}