### Make bundle
npx react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.bundle --assets-dest android/app/src/main/res/

### Move bundle to server
From path android/app/src/main/res/index.bundle
