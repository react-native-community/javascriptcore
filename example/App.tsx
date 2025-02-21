import React, { useState } from 'react';
import {
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import {
  Colors,
  Header,
} from 'react-native/Libraries/NewAppScreen';

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  const [testResult, setTestResult] = useState('No test run yet');
  const [testName, setTestName] = useState('');

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  const testConsoleLog = () => {
    console.log('Hello from JSC');
    setTestName('Console Test Result');
    setTestResult('Hello from JSC');
  };

  const testBasicOperations = () => {
    const mathResult = 2 + 2;
    const stringResult = 'Hello ' + 'World';
    const arrayResult = [1, 2, 3].map(x => x * 2);

    const result = `Math: ${mathResult}\nString: ${stringResult}\nArray: ${arrayResult}`;
    console.log(result);
    setTestName('Basic Operations Result');
    setTestResult(result);
  };

  const testComplexOperations = () => {
    const obj = { a: 1, b: 2 };
    const square = (x: number) => x * x;
    const squareResult = square(4);

    let result = `Object: ${JSON.stringify(obj)}\nSquare(4): ${squareResult}`;

    try {
      // eslint-disable-next-line no-eval
      const dynamicFn = eval('(x) => x * 3');
      const dynamicResult = dynamicFn(4);
      result += `\nDynamic function(4): ${dynamicResult}`;
    } catch (error) {
      result += `\nDynamic function error: ${error}`;
    }

    console.log(result);
    setTestName('Complex Operations Result');
    setTestResult(result);
  };

  const testGlobalAccess = () => {
    const result = `SetTimeout exists: ${typeof global.setTimeout === 'function'}`;
    console.log(result);
    setTestName('Global Access Result');
    setTestResult(result);
  };

  const testErrorHandling = () => {
    let results: string[] = [];

    try {
      throw new Error('Custom error');
    } catch (error) {
      if (error instanceof Error) {
        results.push(`Regular error: ${error.message}`);
      }
    }

    try {
      const undefined1 = undefined;
      // @ts-ignore
      undefined1.someMethod();
    } catch (error) {
      if (error instanceof Error) {
        results.push(`Type error: ${error.message}`);
      }
    }

    try {
      // eslint-disable-next-line no-eval
      eval('syntax error{');
    } catch (error) {
      if (error instanceof Error) {
        results.push(`Eval error: ${error.message}`);
      }
    }

    const result = results.join('\n');
    console.log(result);
    setTestName('Error Handling Result');
    setTestResult(result);
  };

  const testAsync = async () => {
    try {
      const result = await new Promise((resolve) => {
        setTimeout(() => resolve('Regular async completed'), 1000);
      });
      console.log('Regular async result:', result);
      setTestName('Async Test Result');
      setTestResult(String(result));
    } catch (error) {
      setTestName('Async Error');
      setTestResult(String(error));
    }
  };

  const testMemoryAndPerformance = () => {
    const arr = new Array(1000000);
    for (let i = 0; i < arr.length; i++) {
      arr[i] = i;
    }
    const result = `Array length: ${arr.length}`;

    console.log(result);
    setTestName('Memory & Performance Result');
    setTestResult(result);
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Header />
        <View
          style={[
            styles.container,
            {backgroundColor: isDarkMode ? Colors.black : Colors.white},
          ]}>
          <Button title="Console Log Test" onPress={testConsoleLog} />
          <Button title="Basic Operations" onPress={testBasicOperations} />
          <Button title="Complex Operations" onPress={testComplexOperations} />
          <Button title="Global Access Test" onPress={testGlobalAccess} />
          <Button title="Error Handling Test" onPress={testErrorHandling} />
          <Button title="Async Test" onPress={testAsync} />
          <Button title="Memory & Performance" onPress={testMemoryAndPerformance} />
          <View style={styles.resultContainer}>
            <Text style={styles.resultTitle} testID="resultTitle">
              {testName || 'Test Results'}
            </Text>
            <Text style={styles.resultContent} testID="resultContent">
              {testResult}
            </Text>
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 12,
  },
  resultContainer: {
    marginTop: 20,
    padding: 10,
    backgroundColor: '#f0f0f0',
  },
  resultTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  resultContent: {
    fontSize: 14,
  },
});

export default App;
