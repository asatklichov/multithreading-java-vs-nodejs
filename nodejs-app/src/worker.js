const { workerData, parentPort } = require('worker_threads')

console.log('Worker is proceessing ..  ');

parentPort.postMessage(
	heavySum(workerData.a)
)


function heavySum(a) {
	let sum = (Math.floor(Math.random() * 10) + a) * 1000;
	for (let i = 0; i < 2147483647; i++) {
		sum += i;
	};
	//console.log(sum);
	return sum;
};