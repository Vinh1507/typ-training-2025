import { useContext, createContext, useState } from "react";

export const ThemeContext = createContext();

export function ThemeProvider({ children }) {
    const  [theme, setTheme] = useState('light');

    const toggleTheme = () => {
        setTheme(theme === 'light' ? 'dark' : 'light');
    };

    const value = { theme, toggleTheme };

    return (
        <ThemeContext.Provider value={value}>
            <div className={`app ${theme}`}>
                {children}
            </div>
        </ThemeContext.Provider>
    );
}

export const useTheme = () => {
    const context = useContext(ThemeContext);
    if (!context) {
        throw new Error('useTheme phải được sử dụng trong ThemeProvider');
    }
    return context;
};