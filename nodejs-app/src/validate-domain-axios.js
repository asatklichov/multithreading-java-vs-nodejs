// using GOT 
const readline = require('readline');
const fs = require('fs');
//npm install axios@0.19.2
const axios = require('axios');

let myInterface = readline.createInterface({
	input: fs.createReadStream('./domains_list.txt')
});

const start = new Date()
let lineno = 0;
myInterface.on('line', function(line) {
	axios.get(line).then(resp => {
		const val = line.trim()
		const result = 200 == resp.status ? val + " access OK" : val + " access Failed"
		console.log(result + " " + lineno);
		/*let x = Math.floor(Math.random() * 10) + 1;
		heavySum(x);*/
		if (lineno++ == 84) {
			printElapseTime(start);
		}
	});
}).on('end', function() {
	//printElapseTime(start);
});



function printElapseTime(start) {
	var end = new Date() - start
	console.info('Elapsed time: %dms', end)
}





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


