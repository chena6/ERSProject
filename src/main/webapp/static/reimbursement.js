function loadReimbursements(){
	
	let xhttp = new XMLHttpRequest();
	xhttp.onload = (resp) => {
		if (xhttp.status === 200){
			let reimbursement = JSON.parse(xhttp.response);
			getReimbursements(reimbursement);
		} else {
		alert('could not get reimbursements');
		}
	}
		
	xhttp.open('GET', '../reimbursements/user');
	xhttp.send();
}


function getReimbursements(reimbursement){
	$.each(reimbursement, function(i, item) {
		let tr = $('#reimbursementBody').append('<tr>');
		let submittedDay = item.submitted.dayOfWeek;
		let submittedMonth = item.submitted.month;
		let submittedDayMonth = item.submitted.dayOfMonth;
		let submittedYear = item.submitted.year;
		console.log(item.submitted);
		let resolvedDay = '';
		let resolvedMonth = '';
		let resolvedYear = '';
		let resolvedDayMonth = '';
		if (item.resolved != null){
			resolvedDay = item.resolved.dayOfWeek;
			resolvedMonth = item.resolved.month;
			resolvedYear = item.resolved.year;
			resolvedYear = item.resolved.dayOfMonth;
		}
		let $td = tr.append(
				$('<td>').text(item.reimbID),
				$('<td>').text('$' + item.amount),
				$('<td>').text(submittedDay + ' ' + submittedMonth + ' ' + submittedDayMonth + ', ' + submittedYear),
				$('<td>').text(resolvedDay + ' ' + resolvedMonth + submittedDayMonth + ', ' + resolvedYear),
				$('<td>').text(item.statusID),
				$('<td>').text(item.typeID),
				$('<td>').text(item.description)
				);
	})
}

function div_show() {
	document.getElementById('reimbPopUp').style.display = "block";
	}
	// Function to Hide Popup
	function div_hide(){
	document.getElementById('reimbPopUp').style.display = "none";
	}

