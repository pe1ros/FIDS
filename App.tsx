import React, {useState} from 'react';
import {
  TextInput,
  View,
  Text,
  TouchableOpacity,
  ImageBackground,
  StyleSheet,
} from 'react-native';
import WebView from 'react-native-webview';

function App(): JSX.Element {
  const [width, setWidth] = useState(375);
  const [height, setHeight] = useState(812);
  const [url, setUrl] = useState('http://10.96.70.204:3000/?id=4');
  const [isVisible, setIsVisible] = useState(true);

  const handleChangeVisible = () => {
    setIsVisible(!isVisible);
  };

  const handleChangeUrl = (val: string) => {
    setUrl(val);
  };

  const styleWithKey = (
    key: keyof ReturnType<typeof styles>,
    isBox?: boolean,
  ) => {
    return [
      styles(width)[key],
      isBox && styles(width).styleBorderBox,
      !isVisible && styles(width).transparent,
    ];
  };

  return (
    <View
      style={styleWithKey('container')}
      onLayout={({nativeEvent}) => {
        setWidth(nativeEvent.layout.width);
        setHeight(nativeEvent.layout.height);
      }}>
      <View style={styleWithKey('navBar')}>
        <TextInput
          style={styleWithKey('input')}
          value={url}
          onChangeText={handleChangeUrl}
        />
        <View style={styleWithKey('sizeBlock', true)}>
          <Text style={styleWithKey('sizeText')}>w: {Math.round(width)}</Text>
          <Text style={styleWithKey('sizeText')}>h: {Math.round(height)}</Text>
        </View>
        <TouchableOpacity
          style={styleWithKey('sizeBlock', true)}
          onPress={handleChangeVisible}>
          {isVisible && (
            <ImageBackground
              source={require('./ArrowTop.png')}
              style={styleWithKey('image')}
            />
          )}
        </TouchableOpacity>
      </View>
      <WebView style={styleWithKey('webView')} source={{uri: url}} />
    </View>
  );
}

const styles = (width: number) =>
  StyleSheet.create({
    container: {
      width: '100%',
      height: '100%',
      backgroundColor: 'white',
    },
    webView: {
      width: '100%',
      height: '100%',
    },
    navBar: {
      flexDirection: 'row',
      position: 'absolute',
      top: 0,
      left: 0,
      zIndex: 999,
      backgroundColor: 'white',
      height: 40,
    },
    input: {
      height: 40,
      color: '#060708',
      width: width - 110,
      borderWidth: 1,
      borderColor: '#060708',
    },
    sizeBlock: {
      width: 70,
      height: 40,
      justifyContent: 'center',
    },
    sizeText: {
      lineHeight: 15,
      fontSize: 12,
      alignSelf: 'center',
      color: '#060708',
    },
    transparent: {
      color: 'transparent',
      backgroundColor: 'transparent',
      borderColor: 'transparent',
    },
    button: {
      justifyContent: 'center',
      alignItems: 'center',
      maxWidth: 40,
      maxHeight: 40,
    },
    image: {
      width: 36,
      height: 36,
      resizeMode: 'contain',
    },
    styleBorderBox: {
      borderTopWidth: 1,
      borderTopColor: '#060708',
      borderRightWidth: 1,
      borderRightColor: '#060708',
      borderBottomWidth: 1,
      borderBottomColor: '#060708',
    },
  });

export default App;
