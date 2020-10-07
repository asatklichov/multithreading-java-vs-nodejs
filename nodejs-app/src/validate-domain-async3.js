// Asynchronous Form:
//npm install p-time
const readline = require('readline');
const http = require('http');
const fs = require('fs');

let myInterface = readline.createInterface({
	input: fs.createReadStream('./domains_list.txt')
});

const start = new Date()
let lineno = 1;
myInterface.on('line', function(line) {
	getContent(line)
		.then((statusCode) => {
			const val = line.trim();
			const result = 200 == statusCode ? val + " access OK" : val + " access Failed"
			console.log(result + " " + lineno);
			/*let x = Math.floor(Math.random() * 10) + 1;
			heavySum(x);*/
			if (++lineno == 99) {
				printElapseTime(start);
			}
		})
		.catch((err) => {
			console.log(err);
		});
}).on('end', () => {
	console.log('-- END -- ');
});


const getContent = function(url) {
	return new Promise((resolve, reject) => {
		const lib = url.startsWith('https') ? require('https') : require('http');
		const request = lib.get(url, (response) => {
			/*if (response.statusCode < 200 || response.statusCode > 299) {
				reject(new Error('Failed to load page, status code: ' + response.statusCode));
			}*/
			const body = [];
			response.on('data', (chunk) => body.push(chunk));
			response.on('end', () => resolve(response.statusCode));
		});
		request.on('error', (err) => reject(err))
	})
};



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


