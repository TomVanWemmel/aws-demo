const https = require('https')
let url = 'https://sv443.net'
let path = '/jokeapi/category/'
let query = '?blacklistFlags=nsfw,religious,political'

exports.handler = function(event, context, callback) {
  let category = 'Programming'
  if(event.currentIntent.slots.Category !== null){
    category = event.currentIntent.slots.Category
  }
  https.get(url + path + category + query, (res) => {
    let body = '';
    res.on('data', (d) => {
      body += d
    });
    res.on('end', () => {
      console.log('Successfully processed HTTPS response');
      body = JSON.parse(body);
      let message = {
        'contentType': 'PlainText',
        'content': '{"messages":["I don\'t have a joke"]}'
      }
      if (body.joke !== undefined) {
        message = {
          'contentType': 'CustomPayload',
          'content': '{"messages":[' + JSON.stringify(body.joke) + ']}'
        }
      }
      else if (body.setup !== undefined && body.delivery !== undefined) {
        message = {
          'contentType': 'CustomPayload',
          'content': '{"messages":[' + JSON.stringify(body.setup) + ',' + JSON.stringify(body.delivery) + ']}'
        }
      }
      let dialogAction = {
        'dialogAction': {
          'type': 'Close',
          'fulfillmentState': 'Fulfilled',
          'message': message
        }
      }
      callback(null, dialogAction);

    });
  }).on('error', (e) => {
    callback(Error(e))
  })
}
