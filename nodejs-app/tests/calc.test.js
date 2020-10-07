"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var calc_1 = __importDefault(require("../src/calc"));
describe('calc', function () {
    it('factorial', function () {
        var result = calc_1.default.factorial(5);
        expect(result).toBe(120);
    });
    it('fibonacci', function () {
        var result = calc_1.default.fibonacci(4);
        expect(result).toBe(5);
    });
});
//# sourceMappingURL=calc.test.js.map