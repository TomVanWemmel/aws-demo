const https = require('https');

exports.handler = function (event, context, callback) {
    const url = 'https://sv443.net';
    const path = '/jokeapi/category/';
    const query = '?blacklistFlags=nsfw,religious,political';

    let category = 'Programming';
    if (event['currentIntent']['slots']['Category'] !== null) {
        category = event['currentIntent']['slots']['Category'];
    }
    https.get(url + path + category + query, (res) => {
        let body = '';
        res.on('data', (d) => {
            body += d
        });
        res.on('end', () => {
            console.log('Successfully processed HTTPS response');
            const message = convertJokeIntoBotMessage(body);
            const dialogAction = {
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

function convertJokeIntoBotMessage (body) {
  body = JSON.parse(body);
  if (body['joke'] !== undefined) {
    return {
      'contentType': 'CustomPayload',
      'content': '{"messages":[' + JSON.stringify(body['joke']) + ']}'
    }
  } else if (body['setup'] !== undefined && body['delivery'] !== undefined) {
    return {
      'contentType': 'CustomPayload',
      'content': '{"messages":[' + JSON.stringify(body['setup']) + ',' + JSON.stringify(body['delivery']) + ']}'
    }
  }
  return {
    'contentType': 'PlainText',
    'content': '{"messages":["I don\'t have a joke"]}'
  };
}
