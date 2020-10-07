var events = require('events');
//const EventEmitter = require('events');

/*
All event properties and methods are an instance of an EventEmitter object.

e.g. Streams are Event Emitters: process.stdin, process.stdout 
To access these properties use EventEmitter object:
 */
var eventEmitter = new events.EventEmitter();

//event handler:
var myEventHandler = function() {
	console.log('Attention - Match Starts!');
}

//register event:
eventEmitter.on('annonce', myEventHandler);

//fire/spread/release/broadcast the event:
eventEmitter.emit('annonce');
//console.log(''+ eventEmitter.eventNames);