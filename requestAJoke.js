const https = require('https');
let url = 'https://sv443.net/jokeapi/category/Programming?blacklistFlags=nsfw,religious,political'

exports.handler = function (event, context, callback) {
  https.get(url, (res) => {
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
      };
      if (body.joke !== undefined) {
        message = {
          'contentType': 'CustomPayload',
          'content': '{"messages":[' + JSON.stringify(body.joke) + ']}'
        }
      } else if (body.setup !== undefined && body.delivery !== undefined) {
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
      };

      callback(null, dialogAction);

    });
  }).on('error', (e) => {
    callback(Error(e))
  })
};
