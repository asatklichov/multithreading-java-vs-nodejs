//https://www.npmjs.com/package/node-worker-threads-pool
 
// Access the workerData by requiring it.
const { parentPort, workerData } = require('worker_threads');
 


// Something you shouldn"t run in main thread
// since it will block.
function heavySum(a) {
	let sum = (Math.floor(Math.random() * 10) + a) * 1000;
	//console.log('a = '+a);
	for (let i = 0; i < 2147483647; i++) {
		sum += i;
	};
	//console.log(sum);
	return sum;
}; 

// Main thread will pass the data you need
// through this event listener.
parentPort.on('message', (param) => {
  if (typeof param !== 'number') {
    throw new Error('param must be a number.');
  }
  const result = heavySum(param);

  // Access the workerData.
   
  console.log('workerData is', workerData);

  // return the result to main thread.
  parentPort.postMessage(result);
});