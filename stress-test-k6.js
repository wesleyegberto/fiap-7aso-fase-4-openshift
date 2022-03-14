import http from 'k6/http';

export const options = {
  discardResponseBodies: true,
  stages: [
    { duration: '30s', target: 100 },
    { duration: '1m', target: 100 },
    { duration: '30s', target: 200 },
    { duration: '1m', target: 200 },
    { duration: '30s', target: 300 },
    { duration: '1m', target: 300 },
    { duration: '30s', target: 400 },
    { duration: '1m', target: 400 },
    { duration: '1m', target: 0 },
  ],
};

export default function () {
  const URL = 'http://api-produtos-entrega.apps.na46.prod.nextcle.com/products';

  const payload = JSON.stringify({ name: 'PS5', description: 'Console PS5' });
  const params = { headers: { 'Content-Type': 'application/json' } };

  http.post(URL, payload, params);
}
