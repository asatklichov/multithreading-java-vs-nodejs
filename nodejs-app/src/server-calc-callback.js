const http = require('http');
const server = http.createServer();


const heavySum = () => {
  let sum = 0;
  for (let i = 0; i < 1e9; i++) {
    sum += i;
  };
  return sum;
};

server.on('request', (req, res) => {
  if (req.url === '/sum') {
    const sum = heavySum();
	//callback,  
    return res.end(`Sum is ${sum}`);// response.write(data, encoding)
  } else {
    res.end('Ok')
  }
}); 

server.listen(3000);

