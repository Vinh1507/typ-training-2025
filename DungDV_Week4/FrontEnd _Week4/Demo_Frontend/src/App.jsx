import { ThemeProvider } from './contexts/ThemeContext'
import UseStateDemo from './components/UseStateDemo'
import UseEffectDemo from './components/UseEffectDemo'
import UseMemoDemo from './components/UseMemoDemo'
import UseCallbackDemo from './components/UseCallbackDemo'
import UseRefDemo from './components/UseRefDemo'
import UseContextDemo from './components/UseContextDemo'
import ComponentPatternsDemo from './components/ComponentPatternsDemo'
import ApiDemo from './components/ApiDemo'
import './App.css'

function App() {
  return (
    <ThemeProvider>
      <div className="container">
        <h1>React Hooks Demo</h1>
        
        <UseStateDemo />
        <UseEffectDemo />
        <UseMemoDemo />
        <UseCallbackDemo />
        <UseRefDemo />
        <UseContextDemo />
        <ComponentPatternsDemo />
        <ApiDemo />
      </div>
    </ThemeProvider>
  )
}

export default App
