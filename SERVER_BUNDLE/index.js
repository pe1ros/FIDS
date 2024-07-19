const express = require('express');
const path = require('path');

const app = express();
const port = 3000;

app.get('/get_bundle', (req, res) => {
  // const bundlePath = path.join(__dirname, 'FIDS.apk');
  const bundlePath = path.join(__dirname, 'index.bundle');
  console.log('SENDING: ', bundlePath);
  res.sendFile(bundlePath);
});

app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});

