// Asynchronous Form:
const fs = require('fs');
const readline = require('readline');
const read_stream = fs.createReadStream("./domains_list.txt");
const rl = readline.createInterface({
    input: read_stream
}); 
rl.on('line', function(line){	
    console.log(line);
	let x = Math.floor(Math.random() * 10) + 1;
	heavySum(x);
})
.on('end', function(){
      rl.close();
})

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


