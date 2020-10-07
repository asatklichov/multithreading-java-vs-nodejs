// Asynchronous Form:
const readline = require('readline');
const http = require('http');
const fs = require('fs');

let myInterface = readline.createInterface({
	input: fs.createReadStream('files/domains_list.txt')
});

const start = new Date()
let lineno = 0;
myInterface.on('line', function(line) {
	http.get(line, (resp) => {
		val = line.trim()
		const result = 200 === resp.statusCode ? val + " access OK" : val + " access Failed"
		console.log(result + " " + lineno);
		 let x = Math.floor(Math.random() * 10) + 1;
		heavySum(x); 
		if (++lineno == 99) {
			printElapseTime(start);
		}
	}).on("error", (err) => {
		console.log("Error: " + err.message);
	});
});


function printElapseTime(start) {
	var end = new Date() - start
	console.info('Elapsed time: %dms', end)
}


const heavySum = (s) => {
	let sum = (Math.floor(Math.random() * 10) + s) * 1000;
	for (let i = 0; i < 2147483647; i++) {
		sum += i;
	};
	console.log(sum);
};


