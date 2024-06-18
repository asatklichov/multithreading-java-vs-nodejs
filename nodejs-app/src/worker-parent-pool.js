 
const { StaticPool } = require('node-worker-threads-pool');
const filePath = './worker-pool.js';
const readline = require('readline');
const http = require('http');
const fs = require('fs');

const pool = new StaticPool({  
  size: 15,
  task: filePath,
  workerData: 'workerData!'
 
});

const arr = fs.readFileSync('./domains_list500.txt').toString().split("\n")
	.filter(e => e.trim().length > 0);

const start = new Date()
let lineno = 0;
arr.forEach((line) => { 
  (async () => {
    const num = 40 + Math.trunc(100 * Math.random());

    // This will choose one idle worker in the pool
    // to execute your heavy task without blocking
    // the main thread!
    console.log(++lineno + ": " +line);   
    const res = await pool.exec(num);

    console.log(`Result(${num}): `, res);
  })();

});