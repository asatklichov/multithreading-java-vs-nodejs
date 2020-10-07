// using GOT 
const readline = require('readline');
const fs = require('fs');
const got = require('got');
//npm install got@10.6.0


let myInterface = readline.createInterface({
	input: fs.createReadStream('./domains_list.txt')
});
let lineno = 0;
const start = new Date()

myInterface.on('line', function(line) {
	(async () => {
		try {
			const resp = await got(line);
			const val = line.trim()
			const result = 200 === resp.statusCode ? val + " access OK" : val + " access Failed"
			console.log(result + " " + lineno);
	/*		let x = Math.floor(Math.random() * 10) + 1;
			heavySum(x);*/
			if (++lineno == 85) {
				printElapseTime(start);
			}
		} catch (error) {
			console.log(error.message);
		}
	})();
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


