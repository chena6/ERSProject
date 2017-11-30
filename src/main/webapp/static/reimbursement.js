function loadReimbursements(){
	
	let xhttp = new XMLHttpRequest();
	xhttp.onload = (resp) => {
		if (xhttp.status === 200){
			let reimbursement = JSON.parse(xhttp.response);
			//console.log(xhttp.response);
			getReimbursements(reimbursement);
		} else {
		alert('could not get reimbursements');
		}
	}
		
	xhttp.open('GET', '../reimbursements/user');
	xhttp.send();
}

function loadAdminReimbursements() {
	let xhttp = new XMLHttpRequest();
	xhttp.onload = (resp) => {
		if (xhttp.status === 200){
			let reimbursements = JSON.parse(xhttp.response);
			//console.log(xhttp.response);
			getAllReimb(reimbursements);
		} else {
			alert('could not get all reimbursements');
		}
	}
	xhttp.open('GET', '../reimbursements/all')
	xhttp.send();
}


function getReimbursements(reimbursement){
	$.each(reimbursement, function(i, item) {
		let tr = $('#reimbursementBody').append('<tr>');
		let type = item.typeID;
		switch (type) {
		case 1: 
			type = 'Lodging';
			break;
		case 2:
			type = 'Travel';
			break;
		case 3:
			type = 'Food';
			break;
		case 4:
			type = 'Other';
			break;
		default:
			console.log('invalid type');
			break;
		}
		
		let status = item.statusID;
		switch (status) {
		case 1:
			status = 'Pending';
			break;
		case 2:
			status = 'Approved';
			break;
		case 3:
			status = 'Denied';
			break;
		default:
			console.log('invalid status');
			break;
		}
		let resolved = '';
		if (item.resolved != null){
			resolved = new Date(Date.parse(item.resolved));
		}
		//console.log(item.submitted);
		let $td = tr.append(
				$('<td>').text(item.reimbID),
				$('<td>').text('$' + item.amount),
				$('<td>').text(new Date(Date.parse(item.submitted))),
				$('<td>').text(resolved),
				$('<td>').text(status),
				$('<td>').text(type),
				$('<td>').text(item.description)
				);
	})
}

function getAllReimb(reimbursement){
	$.each(reimbursement, function(i, item) {
		
		let type = item.typeID;
		switch (type) {
		case 1: 
			type = 'Lodging';
			break;
		case 2:
			type = 'Travel';
			break;
		case 3:
			type = 'Food';
			break;
		case 4:
			type = 'Other';
			break;
		default:
			console.log('invalid type');
			break;
		}
		
		let status = item.statusID;
		switch (status) {
		case 1:
			status = 'Pending';
			break;
		case 2:
			status = 'Approved';
			break;
		case 3:
			status = 'Denied';
			break;
		default:
			console.log('invalid status');
			break;
		}
		let resolved = '';
		if (item.resolved != null){
			resolved = new Date(Date.parse(item.resolved))
		}
		let tb = $('#reimbursementBody');
		let tr = tb.append($('<tr>')
		.append(
				$('<td>').text(item.reimbID).addClass('id'),
				$('<td>').text(item.reimbAuthor).addClass('author'),
				$('<td>').text('$' + item.amount).addClass('amount'),
				$('<td>').text(new Date(Date.parse(item.submitted))).addClass('submit'),
				$('<td>').text(resolved).addClass('resolve'),
				$('<td>').text(status).addClass('status'),
				$('<td>').text(type).addClass('type'),
				$('<td>').text(item.description).addClass('description')
				)
			);
		
		if (status === 'Pending'){
			console.log(status);
			tb.find($('tr').last()).append(
					$('<td>').append(
							$('<select>').append(
									$('<option>').val('1').text('No Action'),
									$('<option>').val('2').text('Confirm'),
									$('<option>').val('3').text('Decline')
									)
								)
					);
		} else {
			tb.find($('tr').last()).append(
					$('<td>').append(
							$('<li>').val(item.statusID).text(status)
							)
					);
		}
	})
}

function approve(){
	let reimbursementList = new Array();
	let table = $('#reimbTable tbody');
	table.find('tr').each(function (i) {
		let $tds = $(this).find('td'),
			reimbID = $tds.eq(0).text(),
			statusID = $tds.eq(7).find(':selected').val()
			if (statusID === undefined){
				statusID = $tds.eq(7).children().val();
			}
			console.log(reimbID);
			console.log(statusID);
		let reimbursement = {
			"reimbID" : reimbID,
			"statusID" : statusID
		}	
		reimbursementList.push(reimbursement);
	});
	
	
	let xhttp = new XMLHttpRequest();
	xhttp.onload = (resp) => {
		if (xhttp.status === 200){
			window.location.reload();
		} else {
		alert('could not approve/decline');
		}
	}
	
	xhttp.open('POST', '../reimbursements/approve');
	xhttp.send(JSON.stringify(reimbursementList));
}

function div_show() {
	document.getElementById('reimbPopUp').style.display = "block";
	}
	// Function to Hide Popup
	function div_hide(){
	document.getElementById('reimbPopUp').style.display = "none";
	}

function addReimb(){
	let amount = $('#reimbAmount').val();
	let type = $('#reimbType').val();
	let description = $('#reimbDescription').val();

	let reimbursement = {
		"amount" : amount,
		"typeID" : type,
		"description" : description
	}
	
	let xhttp = new XMLHttpRequest();
	xhttp.onload = (resp) => {
		if (xhttp.status === 200) {
			alert('success!');
			window.location.reload();
			
		} else {
			alert('failed!');
		}
	}
	
	xhttp.open('POST', '../reimbursements/addReimburse');
	xhttp.send(JSON.stringify(reimbursement));
}

(function(document) {
	'use strict';

	var LightTableFilter = (function(Arr) {

		var _input;

		function _onInputEvent(e) {
			_input = e.target;
			var tables = document.getElementsByClassName(_input.getAttribute('data-table'));
			Arr.forEach.call(tables, function(table) {
				Arr.forEach.call(table.tBodies, function(tbody) {
					Arr.forEach.call(tbody.rows, _filter);
				});
			});
		}

		function _filter(row) {
			var text = row.textContent.toLowerCase(), val = _input.value.toLowerCase();
			row.style.display = text.indexOf(val) === -1 ? 'none' : 'table-row';
		}

		return {
			init: function() {
				var inputs = document.getElementsByClassName('light-table-filter');
				Arr.forEach.call(inputs, function(input) {
					input.oninput = _onInputEvent;
				});
			}
		};
	})(Array.prototype);

	document.addEventListener('readystatechange', function() {
		if (document.readyState === 'complete') {
			LightTableFilter.init();
		}
	});

})(document);

