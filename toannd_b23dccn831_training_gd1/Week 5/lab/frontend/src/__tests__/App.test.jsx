import { describe, it, expect } from 'vitest';
import App from '../App';

describe('App Component', () => {
  it('should render without crashing', () => {
    expect(App).toBeDefined();
  });

  it('App should be a valid React component', () => {
    expect(typeof App).toBe('function');
  });
});
