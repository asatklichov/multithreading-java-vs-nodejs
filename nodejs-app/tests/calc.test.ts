import calc from '../src/calc';

describe('calc', function() {
    
  it('factorial', function() {
    const result = calc.factorial(5);
    expect(result).toBe(120);
  });

  it('fibonacci', function() {
    const result = calc.fibonacci(4);
    expect(result).toBe(5);
  });
  
});
