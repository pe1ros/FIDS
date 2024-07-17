import React from 'react';
import {StyleSheet} from 'react-native';
import WebView from 'react-native-webview';

function App(): JSX.Element {
  return (
    <WebView
      style={styles.webView}
      source={{uri: 'http://10.96.70.204:9002'}}
    />
  );
}

const styles = StyleSheet.create({
  webView: {
    width: '100%',
    height: '100%',
  },
});

export default App;
