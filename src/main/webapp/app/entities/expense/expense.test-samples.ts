import { IExpense, NewExpense } from './expense.model';

export const sampleWithRequiredData: IExpense = {
  id: 35132,
  expenseCode: 'Borders Baby monitor',
  expenseName: 'Applications HTTP',
};

export const sampleWithPartialData: IExpense = {
  id: 33431,
  expenseCode: 'Car',
  expenseName: 'withdrawal',
  isActive: true,
};

export const sampleWithFullData: IExpense = {
  id: 27404,
  expenseCode: 'Books Small',
  expenseName: 'Down-sized programming Plastic',
  expenseLimit: 14041,
  isActive: true,
};

export const sampleWithNewData: NewExpense = {
  expenseCode: 'Cambridgeshire Balanced',
  expenseName: 'Director',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
